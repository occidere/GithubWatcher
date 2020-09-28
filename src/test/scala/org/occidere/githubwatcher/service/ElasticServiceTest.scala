package org.occidere.githubwatcher.service

import org.occidere.githubwatcher.vo.{Reaction, Repository, User}
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

  "findUserByLogin(!@#$)" should "return Empty User" in {
    elasticService.findUserByLogin("!@#$") shouldBe a[User]
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

  "findAllReposByOwnerLogin(!@#$)" should "return empty List" in {
    elasticService.findAllReposByOwnerLogin("!@#$").length shouldBe 0
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

    elasticService.saveAllRepos(List(repo1, repo2, repo3))

    val repos = elasticService.findAllReposByOwnerLogin("test")
    repos.size should be >= 3
    println(repos)
  }

  "deleteAllReposByIds" should "delete 2 repo" in {
    val repos = List(
      new Repository("20200928000001", "deleteAllReposByIdsTestRepo1", "테스트") {
        ownerLogin = "deleteAllReposByIdsTest"
      }, new Repository("20200928000002", "deleteAllReposByIdsTestRepo1", "테스트") {
        ownerLogin = "deleteAllReposByIdsTest"
      }
    )
    elasticService.saveAllRepos(repos)

    elasticService.deleteAllReposById(repos.map(_.id))

    val resp = elasticService.findAllReposByOwnerLogin("deleteAllReposByIdsTest")
    println(s"resp=$resp")
    resp.size shouldBe 0
  }

  "fineAllReactionsByLogin with not exist user" should "return empty list" in {
    val reactions: List[Reaction] = elasticService.findAllReactionsByLogin("!@#$")

    println(s"reactions = $reactions")
    reactions.size shouldBe 0
  }

  "findAllReactionsByLogin with exist user" should "return nonEmpty list" in {
    // BUILD
    val testLogin = "gw_test_id"
    val reactions = List(new Reaction(-1, "issue") {
      login = testLogin
      title = "Test Title"
      body = "Test Body"
    })
    elasticService.saveAllReactions(reactions)

    // OPERATE
    val resp = elasticService.findAllReactionsByLogin(testLogin)

    // CHECK
    println(resp)
    resp.length should be > 0

    // Manual rollback
    elasticService.deleteAllReactionsByUniqueKeys(reactions.map(_.uniqueKey))
  }
}
