package org.occidere.githubwatcher.task

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-09-01
 */
trait GithubWatcherTask {
  protected val AVAILABLE_TASKS: Map[String, Task] = Map(
    "followerWatchTask" -> FollowerWatchTask,
    "repositoryWatchTask" -> RepositoryWatchTask,
    "reactionWatchTask" -> ReactionWatchTask,
  )
}
