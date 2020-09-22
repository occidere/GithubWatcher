package org.occidere.githubwatcher.task

import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.service.{ElasticService, GithubApiService, LineMessengerService}
import org.occidere.githubwatcher.vo.FollowerDiff

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
object FollowerWatchTask extends Task with GithubWatcherLogger {

  override def run(userId: String): Unit = {
    logger.info(s"User ID: $userId")

    // Data from GitHub API
    val latestUser = GithubApiService.getUser(userId)
    val latestFollowerLogins = GithubApiService.getFollowerLogins(userId)
    logger.info(s"The number of follower from API: ${latestFollowerLogins.size}")

    // Data from DB (Elasticsearch)
    val prevUser = ElasticService.findUserByLogin(userId)
    logger.info(s"The number of follower stored in DB: ${prevUser.followerLogins.size}")

    // Diff
    val diff = FollowerDiff(prevUser.followerLogins, latestFollowerLogins)
    logger.info(s"Recently added new followers: ${diff.newFollowerLogins.size}")
    logger.info(s"Recently deleted followers: ${diff.deletedFollowerLogins.size}")
    logger.info(s"Not changed followers: ${diff.notChangedFollowerLogins.size}")

    // Send line message if follower changed
    if (diff.hasChanged) LineMessengerService.sendFollowerMessage(diff)

    // Update DB to latest data including repos
    latestUser.followerLogins = diff.newFollowerLogins ++ diff.notChangedFollowerLogins
    ElasticService saveUser latestUser
    logger.info(s"Task finished ($userId)")
  }
}
