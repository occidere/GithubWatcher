package org.occidere.githubwatcher.service

import org.occidere.githubwatcher.vo.User
import org.scalatest.flatspec._
import org.scalatest.matchers._

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
class GithubApiServiceTest extends AnyFlatSpec with should.Matchers {
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
}
