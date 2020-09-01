package org.occidere.githubwatcher

import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.task.{FollowerWatchTask, GithubWatcherTask, RepositoryWatchTask}

import scala.util.{Failure, Success, Try}

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
object GithubWatcher extends App with GithubWatcherTask with GithubWatcherLogger {
  private val userId = sys.env.getOrElse("gw_user_id", "")
  sys.env.getOrElse("gw_tasks", "").split(",").map(_.trim)
    .foreach(task => {
      if (AVAILABLE_TASKS.contains(task)) {
        if (task == FOLLOWER_WATCH_TASK) {
          Try(FollowerWatchTask.run(userId)) match {
            case Success(_) => logger.info(s"$FOLLOWER_WATCH_TASK Success!")
            case Failure(exception) => logger.error(s"$FOLLOWER_WATCH_TASK Failed!", exception)
          }
        }

        if (task == REPOSITORY_WATCH_TASK) {
          Try(RepositoryWatchTask.run(userId)) match {
            case Success(_) => logger.info(s"$REPOSITORY_WATCH_TASK Success!")
            case Failure(exception) => logger.error(s"$REPOSITORY_WATCH_TASK Failed!", exception)
          }
        }
      } else {
        logger.warn(s"Unsupported Task: $task")
      }
    })

  sys.exit(0)
}
