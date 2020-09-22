package org.occidere.githubwatcher.task

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-22
 */
trait Task {
  def run(userId: String): Unit
}