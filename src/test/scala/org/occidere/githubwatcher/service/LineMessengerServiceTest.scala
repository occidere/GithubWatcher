package org.occidere.githubwatcher.service

import org.occidere.githubwatcher.util.MessageBuilderUtils
import org.occidere.githubwatcher.vo._
import org.scalatest.PrivateMethodTester
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.util.{Success, Try}

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
class LineMessengerServiceTest extends AnyFlatSpec with PrivateMethodTester with should.Matchers {

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

  "sendMessageIfExist with token" should "be succeeded" in {
    Try(LineMessengerService invokePrivate PrivateMethod('sendMessageIfExist)("test")) match {
      case Success(_) => println("Test message send success")
    }
  }

  "sendFollowerMessage with token" should "be succeeded" in {
    Try(LineMessengerService.sendFollowerMessage(FollowerDiff(List("a", "b"), List("a", "c")))) match {
      case Success(_) => println(s"Message send success")
    }
  }

  "sendRepositoryMessage with token" should "be succeeded" in {
    Try(LineMessengerService.sendRepositoryMessage(
      RepositoryDiff(
        new Repository("1", "test", "desc") {
          stargazerLogins = List("a", "b")
          forkLogins = List("a", "b")
          watcherLogins = List("a", "b")
        },
        new Repository("1", "test", "desc") {
          stargazerLogins = List("b", "c")
          forkLogins = List("b", "c")
          watcherLogins = List("b", "c")
        }
      ))
    ) match {
      case Success(_) => println(s"Message send success")
    }
  }

  "sendReactionMessage with token" should "be succeeded" in {
    Try(LineMessengerService.sendReactionMessage(
      ReactionDiff(
        new Reaction(-1, "issue") {
          thumbUp = 0
          hooray = 0
          eyes = 0
          heart = 0
          rocket = 0
          laugh = 0
          thumbDown = 1
          confused = 1
          title = "테스트 제목"
          body = "테스트 내용"
          htmlUrl = "https://github.com/occidere"
        }, new Reaction(-1, "issue") {
          thumbUp = 1
          hooray = 1
          eyes = 1
          heart = 1
          rocket = 1
          laugh = 1
          thumbDown = 0
          confused = 0
          title = "테스트 제목"
          body = "테스트 내용"
          htmlUrl = "https://github.com/occidere"
        })
    )) match {
      case Success(_) => println("Message send success")
    }
  }
}
