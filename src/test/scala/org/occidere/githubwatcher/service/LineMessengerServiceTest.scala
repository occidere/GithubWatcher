package org.occidere.githubwatcher.service

import org.occidere.githubwatcher.util.MessageBuilderUtils
import org.occidere.githubwatcher.vo.{FollowerDiff, Repository, RepositoryDiff}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
class LineMessengerServiceTest extends AnyFlatSpec with should.Matchers {

  "createRepositoryMessage" should "Contain at least 15 lines" in {
    val prevRepo = new Repository("1", "test1", "desc1") {
      stargazerLogins = List("a", "b")
      forkLogins = List("a", "b")
      watcherLogins = List("a", "b")
    }

    val latestRepo = new Repository("1", "test1", "desc1") {
      stargazerLogins = List("b", "c")
      forkLogins = List("b", "c")
      watcherLogins = List("b", "c")
    }

    val msg = MessageBuilderUtils.createRepositoryMessage(RepositoryDiff(prevRepo, latestRepo))

    println(msg)
    msg.split("\n").length should be >= 15
  }

  "createFollowerMessage" should "Contain at least 4 lines" in {
    val followerDiff = FollowerDiff(List("a", "b"), List("a", "c"))

    val followerMessage = MessageBuilderUtils.createFollowerMessage(followerDiff)

    println(followerMessage)
    followerMessage.split("\n").length should be >= 4
  }
}
