package org.occidere.githubwatcher.vo

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-01
 */
case class RepositoryDiff(prevRepo: Repository, latestRepo: Repository) {
  val repoName: String = latestRepo.name

  val newForkLogins: List[String] = latestRepo.forkLogins diff prevRepo.forkLogins
  val newWatcherLogins: List[String] = latestRepo.watcherLogins diff prevRepo.watcherLogins
  val newStargazerLogins: List[String] = latestRepo.stargazerLogins diff prevRepo.stargazerLogins
  val hasNewChanged: Boolean = newForkLogins.nonEmpty || newWatcherLogins.nonEmpty || newStargazerLogins.nonEmpty

  val delForkLogins: List[String] = prevRepo.forkLogins diff latestRepo.forkLogins
  val delWatcherLogins: List[String] = prevRepo.watcherLogins diff latestRepo.watcherLogins
  val delStargazerLogins: List[String] = prevRepo.stargazerLogins diff latestRepo.stargazerLogins
  val hasDelChanged: Boolean = delForkLogins.nonEmpty || delWatcherLogins.nonEmpty || delStargazerLogins.nonEmpty

  val hasChanged: Boolean = hasNewChanged || hasDelChanged
}
