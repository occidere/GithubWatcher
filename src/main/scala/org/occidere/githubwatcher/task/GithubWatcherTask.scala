package org.occidere.githubwatcher.task

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-01
 */
trait GithubWatcherTask {
  protected val FOLLOWER_WATCH_TASK = "followerWatchTask"
  protected val REPOSITORY_WATCH_TASK = "repositoryWatchTask"

  protected val AVAILABLE_TASKS = Set(FOLLOWER_WATCH_TASK, REPOSITORY_WATCH_TASK)
}
