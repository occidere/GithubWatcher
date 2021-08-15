package org.occidere.githubwatcher.vo

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-09-23
 */
case class ReactionDiff(prevReaction: Reaction, latestReaction: Reaction) {
  val thumbUpDelta: Int = latestReaction.thumbUp - prevReaction.thumbUp
  val thumbDownDelta: Int = latestReaction.thumbDown - prevReaction.thumbDown
  val laughDelta: Int = latestReaction.laugh - prevReaction.laugh
  val hoorayDelta: Int = latestReaction.hooray - prevReaction.hooray
  val heartDelta: Int = latestReaction.heart - prevReaction.heart
  val rocketDelta: Int = latestReaction.rocket - prevReaction.rocket
  val eyesDelta: Int = latestReaction.eyes - prevReaction.eyes
  val confusedDelta: Int = latestReaction.confused - prevReaction.confused

  val hasChanged: Boolean = thumbUpDelta != 0 || thumbDownDelta != 0 || laughDelta != 0 ||
    hoorayDelta != 0 || heartDelta != 0 || rocketDelta != 0 || eyesDelta != 0 || confusedDelta != 0
}
