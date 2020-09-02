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
object RepositoryWatchTask extends GithubWatcherLogger {

  def run(userId: String): Unit = {
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
    for (latestRepo <- latestRepos) Try {
      val prevRepo = prevRepos.getOrElse(latestRepo.id, Repository(latestRepo.id, latestRepo.name, latestRepo.description))
      val diff = RepositoryDiff(prevRepo, latestRepo)
      logger.info(s"Repo: ${diff.repoName} (changed: ${diff.hasChanged})")

      // Send message
      if (diff.hasChanged) LineMessengerService.sendRepositoryMessage(diff)
    } match {
      case Failure(exception) => logger.error(s"${latestRepo.name} process failed", exception)
      case Success(_) => // Save latest repos
        ElasticService.saveAllRepos(latestRepos)
        logger.info(s"${latestRepo.name} process success")
    }
  }
}
