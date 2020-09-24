package org.occidere.githubwatcher.util

import org.occidere.githubwatcher.vo.Reaction
import org.scalatest.PrivateMethodTester
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class CopyUtilsTest extends AnyFlatSpec with PrivateMethodTester with should.Matchers {
  "copyReactionWithoutCounts" should "have all zero value of reactions(emoji)" in {
    val a = new Reaction(-1, "issue") {
      htmlUrl = "http://occidere.blog.me"
      login = "occidere"
      title = "copy test"
      body = "copy test"
      repoName = "occidere"

      laugh = 1
      hooray = 1
      confused = 1
      heart = 1
      rocket = 1
      eyes = 1
      thumbUp = 1
      thumbDown = 1
      totalCount = 8
    }

    val b = CopyUtils.copyReactionWithoutCounts(a)

    b.uniqueKey shouldEqual a.uniqueKey
    List(b.htmlUrl, b.login, b.title, b.body, b.repoName).find(_.isBlank) shouldBe None
    List(b.totalCount, b.laugh, b.hooray, b.confused, b.heart, b.rocket, b.eyes, b.thumbUp, b.thumbDown).find(_ != 0) shouldBe None
  }
}
