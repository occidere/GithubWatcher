package org.occidere.githubwatcher.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.occidere.githubwatcher.vo.{Reaction, ReactionDiff}
import org.scalatest.PrivateMethodTester
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class MessageBuilderUtilsTest extends AnyFlatSpec with PrivateMethodTester with should.Matchers {

  "createReactionMessage" should "contain title, body, thumbUp, htmlUrl" in {
    val prev = new Reaction(-1, "issue") {
      thumbUp = 0
      title = "테스트 제목"
      body = "테스트 내용"
      htmlUrl = "https://github.com/occidere"
    }
    val latest = new Reaction(-1, "issue") {
      thumbUp = 1
      title = "테스트 제목"
      body = "테스트 내용"
      htmlUrl = "https://github.com/occidere"
    }
    val diff = ReactionDiff(prev, latest)

    val msg = MessageBuilderUtils.createReactionMessage(diff)

    println(msg)
    msg.contains(latest.title) shouldBe true
    msg.contains(latest.body) shouldBe true
    msg.contains(latest.htmlUrl) shouldBe true
    msg.contains(s"${ReactionEmojiConvertUtils.getEmoji("thumbUp")}") shouldBe true
    msg.contains(s"${ReactionEmojiConvertUtils.getEmoji("thumbDown")}") shouldBe false
  }

  "Just Test" should "test" in {
    val MAPPER = new ObjectMapper().registerModule(DefaultScalaModule)
    println(MAPPER.readValue("""[{"a": 1}]""", classOf[List[Map[String, Any]]]))
    println(MAPPER.readValue("""[]""", classOf[List[Map[String, Any]]]))
    println(MAPPER.readValue("""""", classOf[List[Map[String, Any]]]))
  }
}
