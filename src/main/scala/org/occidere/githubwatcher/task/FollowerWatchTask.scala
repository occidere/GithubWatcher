package org.occidere.githubwatcher.task

import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.service.{ElasticService, GithubApiService, LineMessengerService}

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
object FollowerWatchTask extends GithubWatcherLogger {

  def run(userId: String): Unit = {
    logger.info(s"User ID: $userId")

    // Data from GitHub API
    val latestUser = GithubApiService.getUser(userId)
    val latestFollowerLogins = GithubApiService.getFollowerLogins(userId)
    logger.info(s"The number of follower from API: ${latestFollowerLogins.size}")

    // Data from DB (Elasticsearch)
    val prevUser = ElasticService.findUserByLogin(userId)
    logger.info(s"The number of follower stored in DB: ${prevUser.followerLogins.size}")

    // Diff
    val newFollowerLogins: List[String] = latestFollowerLogins diff prevUser.followerLogins
    logger.info(s"Recently added new followers: ${newFollowerLogins.size}")
    val deletedFollowerLogins: List[String] = prevUser.followerLogins diff latestFollowerLogins
    logger.info(s"Recently deleted followers: ${deletedFollowerLogins.size}")
    val notChangedFollowerLogins: List[String] = prevUser.followerLogins intersect latestFollowerLogins
    logger.info(s"Not changed followers: ${notChangedFollowerLogins.size}")

    // Send line message if follower changed
    LineMessengerService.sendFollowerMessageIfExist(newFollowerLogins, deletedFollowerLogins)

    // Update DB to latest data including repos
    latestUser.followerLogins = newFollowerLogins ++ notChangedFollowerLogins
    latestUser.repositories = prevUser.repositories
    ElasticService saveUser latestUser
    logger.info(s"Task finished ($userId)")
  }
}
