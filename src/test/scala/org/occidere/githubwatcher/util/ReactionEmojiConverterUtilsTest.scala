package org.occidere.githubwatcher.util

import org.scalatest.PrivateMethodTester
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ReactionEmojiConverterUtilsTest extends AnyFlatSpec with PrivateMethodTester with should.Matchers {

  "getEmoji() with supported names" should "convert to emojis without exception" in {
    val supportedReactions = List("thumbUp", "thumbDown", "hooray", "confused", "eyes", "rocket", "heart", "laugh")

    val emojis = supportedReactions.map(ReactionEmojiConvertUtils.getEmoji)

    println(emojis)
    emojis.size shouldEqual supportedReactions.size
  }

  "getEmoji() with unsupported names" should "throw NoSuchElementException" in {
    val unsupportedReactions = "occidere"
    assertThrows[NoSuchElementException] {
      ReactionEmojiConvertUtils.getEmoji(unsupportedReactions)
    }
  }
}
