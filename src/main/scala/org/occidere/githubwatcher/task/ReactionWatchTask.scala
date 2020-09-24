package org.occidere.githubwatcher.task

import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.service.{ElasticService, GithubApiService}

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-23
 */
object ReactionWatchTask extends Task with GithubWatcherLogger {
  override def run(userId: String): Unit = {
    logger.info(s"User ID: $userId")

    // Data from GitHub API
    // Fetch reactions from Issues
    val reactionsFromIssues = GithubApiService.getReactionsOfIssuesCreatedByUser

    // Fetch reactions from Repo
    val latestReactions = reactionsFromIssues.map(_.repoName)
      .distinct
      .flatMap(GithubApiService.getReactionsOfCommentsInRepository(userId, _)) ++ reactionsFromIssues

    // Data from DB (Elasticsearch)
    val prevReactions = ElasticService.findAllReactionsByLogin(userId)

    // Diff
    // TODO: Implement efficient diff

    // Send line message

    // Update DB to latest data
  }
}
