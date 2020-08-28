package org.occidere.githubwatcher.logger

import org.slf4j
import org.slf4j.LoggerFactory

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
trait GithubWatcherLogger {
  lazy val logger: slf4j.Logger = LoggerFactory.getLogger(getClass)

  implicit private def log(x: GithubWatcherLogger): slf4j.Logger = x.logger
}
