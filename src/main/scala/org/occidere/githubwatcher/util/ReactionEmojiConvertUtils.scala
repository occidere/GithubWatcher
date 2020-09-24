package org.occidere.githubwatcher.util

object ReactionEmojiConvertUtils {
  private val EmojiMap = Map(
    "thumbUp" -> "\uD83D\uDC4D",
    "thumbDown" -> "\uD83D\uDC4E",
    "laugh" -> "\uD83D\uDE04",
    "confused" -> "\uD83D\uDE15",
    "heart" -> "❤️",
    "hooray" -> "\uD83C\uDF89",
    "rocket" -> "\uD83D\uDE80",
    "eyes" -> "\uD83D\uDC40",
  )

  def getEmoji(name: String): String =
    if (EmojiMap.contains(name)) EmojiMap(name)
    else throw new NoSuchElementException(s"Not supported emoji: $name")
}
