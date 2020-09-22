package org.occidere.githubwatcher.task

import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.service._
import org.occidere.githubwatcher.vo.{Repository, RepositoryDiff}

import scala.util.{Failure, Success, Try}

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
object RepositoryWatchTask extends Task with GithubWatcherLogger {

  override def run(userId: String): Unit = {
    logger.info(s"User ID: $userId")

    // Latest repos from API
    val latestRepos = GithubApiService.getRepositories(userId)
      .map(repo => {
        repo.forkLogins = GithubApiService.getForkLogins(repo.ownerLogin, repo.name)
        repo.watcherLogins = GithubApiService.getWatcherLogins(repo.ownerLogin, repo.name)
        repo.stargazerLogins = GithubApiService.getStargazerLogins(repo.ownerLogin, repo.name)
        repo
      })
    logger.info(s"The number of repos from API: ${latestRepos.size}")

    // Prev repos from ES
    val prevRepos: Map[String, Repository] = ElasticService.findAllReposByOwnerLogin(userId).groupMapReduce(_.id)(repo => repo)((r1, _) => r1)
    logger.info(s"The number of repos from ES: ${prevRepos.size}")

    // Diff & Send message if changed
    val changedLatestRepos = latestRepos.filter(latest => {
      val prev = prevRepos.getOrElse(latest.id, Repository(latest.id, latest.name, latest.description))
      val diff = RepositoryDiff(prev, latest)
      logger.info(s"Repo: ${diff.repoName} (changed: ${diff.hasChanged})")

      // Send message
      Try(if (diff.hasChanged) LineMessengerService.sendRepositoryMessage(diff)) match {
        case Failure(exception) =>
          logger.error(s"${latest.name} process failed", exception)
          false
        case Success(_) =>
          logger.info(s"${latest.name} process success")
          diff.hasChanged // Save to ES which only chang occurred
      }
    })

    if (changedLatestRepos.nonEmpty) ElasticService.saveAllRepos(changedLatestRepos)
  }
}
