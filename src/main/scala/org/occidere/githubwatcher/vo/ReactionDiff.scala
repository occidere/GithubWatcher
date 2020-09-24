package org.occidere.githubwatcher.vo

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-23
 */
case class ReactionDiff(prevReaction: Reaction, latestReaction: Reaction) {
  val repoName: String = latestReaction.repoName

}
