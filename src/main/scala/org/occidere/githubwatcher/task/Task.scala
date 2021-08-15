package org.occidere.githubwatcher.task

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-09-22
 */
trait Task {
  def run(userId: String): Unit
}