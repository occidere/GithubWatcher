package org.occidere.githubwatcher.service

import org.occidere.githubwatcher.vo.{Repository, User}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers._

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
class ElasticServiceTest extends AnyFlatSpec with should.Matchers {

  private val elasticService = ElasticService

  "findUserByLogin(occidere)" should "return User" in {
    val user = elasticService.findUserByLogin("occidere")

    user shouldBe a[User]
    println(user)
  }

  "findUserByLogin(!@#$)" should "throw Exception" in {
    an[NoSuchElementException] should be thrownBy elasticService.findUserByLogin("!@#$")
  }

  "saveUser(test)" should "save name to TEST" in {
    val user = User(99999, "test", "TEST")

    elasticService.saveUser(user)

    val res = elasticService.findUserByLogin("test")
    println(s"user = $user, res = $res")
    user.id.shouldEqual(res.id)
  }

  "findAllReposByOwnerLogin(occidere)" should "return Iterable of Repository vo" in {
    val repos: Iterable[Repository] = elasticService.findAllReposByOwnerLogin("occidere")

    repos.shouldBe(a[Iterable[Repository]])
    println(repos)
  }

  "saveAllReposByOwnerLogin(test)" should "save 3 repos" in {
    val repo1 = new Repository("999", "testRepo1", "테스트") {
      ownerLogin = "test"
    }
    val repo2 = new Repository("998", "testRepo2", "테스트") {
      ownerLogin = "test"
    }
    val repo3 = new Repository("997", "testRepo3", "테스트") {
      ownerLogin = "test"
    }

    elasticService.saveAllRepos(Iterable(repo1, repo2, repo3))

    val repos = elasticService.findAllReposByOwnerLogin("test")
    repos.size should be >= 3
    println(repos)
  }
}
