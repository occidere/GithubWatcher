package org.occidere.githubwatcher.service

import org.occidere.githubwatcher.vo.{Reaction, User}
import org.scalatest.PrivateMethodTester
import org.scalatest.flatspec._
import org.scalatest.matchers._

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
class GithubApiServiceTest extends AnyFlatSpec with PrivateMethodTester with should.Matchers {
  private val githubApiService = GithubApiService

  "getUser" should "return User type vo" in {
    val user = githubApiService.getUser("occidere")

    println(user)
    user.shouldBe(a[User])
  }

  "getRepositories" should "return nonNull Iterable of Repository" in {
    val repositories = githubApiService.getRepositories("occidere")

    println(repositories.size)
    repositories.shouldNot(be(null))
  }

  "getFollowerLogins" should "return nonNull Iterable of Strings" in {
    val followerLogins = githubApiService.getFollowerLogins("occidere")

    println(followerLogins)
    followerLogins.shouldNot(be(null))
  }

  "getStargazerLogins" should "return nonNull Iterable of Strings" in {
    val stargazerLogins = githubApiService.getStargazerLogins("occidere", "MMDownloader")

    println(stargazerLogins)
    stargazerLogins.shouldNot(be(null))
  }

  "getWatcherLogins" should "return nonNull Iterable of Strings" in {
    val watcherLogins = githubApiService.getWatcherLogins("occidere", "MMDownloader")

    println(watcherLogins)
    watcherLogins.shouldNot(be(null))
  }

  "getForkLogins" should "return nonNull Iterable of Strings" in {
    val getForkLogins = githubApiService.getForkLogins("occidere", "MMDownloader")

    println(getForkLogins)
    getForkLogins.shouldNot(be(null))
  }

  "getReactionsOfIssuesCreatedByUser" should "return nonNull Iterable of Reaction" in {
    val reactions = githubApiService.getReactionsOfIssuesCreatedByUser

    println(reactions.size)
    println(reactions.find(_.totalCount > 0).orNull)
    reactions shouldNot be(null)
  }

  "getReactionsOfIssuesInRepository" should "return nonNull List of Reactions" in {
    val reactions = githubApiService.getReactionsOfIssuesInRepository("occidere", "TIL")

    println(reactions.size)
    println(reactions.find(_.totalCount > 0).orNull)
    reactions shouldNot be(null)
  }

  "Call toUrlEncoded with nonEmpty params" should "start with ? and separated by & each" in {
    val params = Map(
      "page" -> "1",
      "per_page" -> "100",
      "filter" -> "created"
    )

    val urlEncoded: String = githubApiService invokePrivate PrivateMethod('toUrlEncoded)(params)

    println(urlEncoded)
    urlEncoded should startWith("?")
    urlEncoded split "&" should have length 3
  }

  "Call toUrlEncoded with empty params" should "return empty string" in {
    val urlEncoded: String = githubApiService invokePrivate PrivateMethod('toUrlEncoded)(Map())

    println(s"urlEncoded = $urlEncoded")
    urlEncoded shouldBe empty
  }
}
