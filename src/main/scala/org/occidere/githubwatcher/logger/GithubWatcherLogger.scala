package org.occidere.githubwatcher.logger

import org.slf4j
import org.slf4j.LoggerFactory

import scala.language.implicitConversions

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
trait GithubWatcherLogger {
  lazy val logger: slf4j.Logger = LoggerFactory.getLogger(getClass)

  implicit private def log(x: GithubWatcherLogger): slf4j.Logger = x.logger
}
