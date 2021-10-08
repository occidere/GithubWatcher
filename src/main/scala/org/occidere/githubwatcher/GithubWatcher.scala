package org.occidere.githubwatcher

import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.service.ElasticService
import org.occidere.githubwatcher.task.GithubWatcherTask

import scala.util.{Failure, Success, Try}

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
object GithubWatcher extends App with GithubWatcherTask with GithubWatcherLogger {
  private val userId = sys.env.getOrElse("gw_user_id", "")
  private val skipAlert = sys.env.getOrElse("gw_skip_alert", "false").toBoolean
  sys.env.getOrElse("gw_tasks", "").split(",").map(_.trim).filterNot(taskName => taskName.isBlank)
    .foreach(task => {
      if (!AVAILABLE_TASKS.contains(task)) logger.warn(s"Unsupported task: $task")
      else Try(AVAILABLE_TASKS(task).run(userId, skipAlert)) match {
        case Success(_) => logger.info(s"$task Success!")
        case Failure(e) => logger.error(s"$task Failed!", e)
      }
    })

  ElasticService.close()
}
