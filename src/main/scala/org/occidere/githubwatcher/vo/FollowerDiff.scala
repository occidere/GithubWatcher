package org.occidere.githubwatcher.vo

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-01
 */
case class FollowerDiff(prevFollowerLogins: List[String], latestFollowerLogins: List[String]) {
  val newFollowerLogins: List[String] = latestFollowerLogins diff prevFollowerLogins
  val deletedFollowerLogins: List[String] = prevFollowerLogins diff latestFollowerLogins
  val notChangedFollowerLogins: List[String] = prevFollowerLogins intersect latestFollowerLogins
  val hasChanged: Boolean = newFollowerLogins.nonEmpty || deletedFollowerLogins.nonEmpty
}
