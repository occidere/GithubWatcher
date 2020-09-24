package org.occidere.githubwatcher.task

import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.service.{ElasticService, GithubApiService, LineMessengerService}
import org.occidere.githubwatcher.util.CopyUtils._
import org.occidere.githubwatcher.vo.ReactionDiff

import scala.util.{Failure, Success, Try}

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-23
 */
object ReactionWatchTask extends Task with GithubWatcherLogger {
  override def run(userId: String): Unit = {
    logger.info(s"User ID: $userId")

    // Fetch reactions from Issues
    val reactionsFromIssues = GithubApiService.getReactionsOfIssuesCreatedByUser

    // Merge issues' reactions with fetched reactions from Repo
    val latestReactions = reactionsFromIssues.distinctBy(x => s"${x.repoOwnerLogin}/${x.repoName}".hashCode)
      .flatMap(x => GithubApiService.getReactionsOfCommentsInRepository(x.repoOwnerLogin, x.repoName)) ++ reactionsFromIssues

    // Data from DB (Elasticsearch)
    val prevReactions = ElasticService.findAllReactionsByLogin(userId).map(x => x.uniqueKey -> x).toMap

    // Diff & Send line message
    val changedReactions = latestReactions.filter(latest => {
      val diff = ReactionDiff(prevReactions.getOrElse(latest.uniqueKey, copyReactionWithoutCounts(latest)), latest)
      Try(if (diff.hasChanged) LineMessengerService.sendReactionMessage(diff)) match {
        case Failure(e) =>
          logger.error(s"${latest.uniqueKey} process failed (htmlUrl: ${latest.htmlUrl})", e)
          false
        case Success(_) =>
          logger.info(s"${latest.uniqueKey} process success (htmlUrl: ${latest.htmlUrl})")
          diff.hasChanged
      }
    })

    // Update DB to latest data
    ElasticService.deleteAllReactionsByUniqueKeys(prevReactions.keySet -- latestReactions.map(_.uniqueKey)) // Delete reactions
    ElasticService.saveAllReactions(changedReactions) // Upsert reactions
  }
}
