package org.occidere.githubwatcher.service

import org.occidere.githubwatcher.util.MessageBuilderUtils
import org.occidere.githubwatcher.vo.{Repository, RepositoryDiff}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
class LineMessengerServiceTest extends AnyFlatSpec with should.Matchers {

  "Message formatting test" should "Always success" in {
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
  }
}
