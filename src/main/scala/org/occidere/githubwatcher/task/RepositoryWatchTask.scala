package org.occidere.githubwatcher.task

import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.service._
import org.occidere.githubwatcher.vo.Repository

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
    val prevRepos: Map[String, Repository] = ElasticService.findAllReposByOwnerLogin(userId)
      .groupMapReduce(_.id)(_)
    logger.info(s"The number of repos from ES: ${prevRepos.size}")

    // DIFF & Send message if changed
    latestRepos.filter(latestRepo => {
      val prevRepo = prevRepos(latestRepo.id)

      val newForkLogins = latestRepo.forkLogins diff prevRepo.forkLogins
      val newWatcherLogins = latestRepo.watcherLogins diff prevRepo.watcherLogins
      val newStargazerLogins = latestRepo.stargazerLogins diff prevRepo.stargazerLogins
      val hasNewChanged = newForkLogins.nonEmpty || newWatcherLogins.nonEmpty || newStargazerLogins.nonEmpty

      val delForkLogins = prevRepo.forkLogins diff latestRepo.forkLogins
      val delWatcherLogins = prevRepo.watcherLogins diff latestRepo.watcherLogins
      val delStargazerLogins = prevRepo.stargazerLogins diff latestRepo.stargazerLogins
      val hasDelChanged = delForkLogins.nonEmpty || delWatcherLogins.nonEmpty || delStargazerLogins.nonEmpty

      logger.info(s"${latestRepo.name} (changed: ${hasNewChanged || hasDelChanged})")

      // TODO: 효율적인 diff 결과 메시지 전송 방법 고려 (파라미터 최소화)

      false
    })

    // Save latest repos

  }
}
