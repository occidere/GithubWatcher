package org.occidere.githubwatcher.vo

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-23
 */
case class ReactionDiff(prevReaction: Reaction, latestReaction: Reaction) {
  val thumbUpDelta: Int = prevReaction.thumbUp - latestReaction.thumbUp
  val thumbDownDelta: Int = prevReaction.thumbDown - latestReaction.thumbDown
  val laughDelta: Int = prevReaction.laugh - latestReaction.laugh
  val hoorayDelta: Int = prevReaction.hooray - latestReaction.hooray
  val heartDelta: Int = prevReaction.heart - latestReaction.heart
  val rocketDelta: Int = prevReaction.rocket - latestReaction.rocket
  val eyesDelta: Int = prevReaction.eyes - latestReaction.eyes
  val confusedDelta: Int = prevReaction.confused - latestReaction.confused

  val hasChanged: Boolean = thumbUpDelta != 0 || thumbDownDelta != 0 || laughDelta != 0 ||
    hoorayDelta != 0 || heartDelta != 0 || rocketDelta != 0 || eyesDelta != 0 || confusedDelta != 0
}
