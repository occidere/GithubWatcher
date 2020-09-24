package org.occidere.githubwatcher.vo

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-09
 */
class DiffTest extends AnyFlatSpec with should.Matchers {
  "Elements of FollowerDiff" should "have at least 1 element and marked as changed" in {
    val followerDiff = FollowerDiff(List("a", "b"), List("a", "c"))

    followerDiff.newFollowerLogins.length should be >= 1
    followerDiff.deletedFollowerLogins.length should be >= 1
    followerDiff.notChangedFollowerLogins.length should be >= 1
    followerDiff.hasChanged shouldEqual true
  }

  "Each elements of RepositoryDiff" should "have at least 1 element and marked as changed" in {
    val repositoryDiff = RepositoryDiff(
      new Repository("1", "test", "test") {
        stargazerLogins = List("a", "b")
        forkLogins = List("a", "b")
        watcherLogins = List("a", "b")
      },
      new Repository("1", "test", "test") {
        stargazerLogins = List("a", "c")
        forkLogins = List("a", "c")
        watcherLogins = List("a", "c")
      }
    )

    repositoryDiff.newStargazerLogins.length should be >= 1
    repositoryDiff.newForkLogins.length should be >= 1
    repositoryDiff.newWatcherLogins.length should be >= 1

    repositoryDiff.delStargazerLogins.length should be >= 1
    repositoryDiff.delForkLogins.length should be >= 1
    repositoryDiff.delWatcherLogins.length should be >= 1

    repositoryDiff.hasNewChanged shouldEqual true
    repositoryDiff.hasDelChanged shouldEqual true
    repositoryDiff.hasChanged shouldEqual true
  }

  "ReactionDiff" should "has changed in confusedDelta" in {
    val prev = new Reaction(-1, "issue") {
      confused = 1
    }
    val latest = new Reaction(-1, "issue") {
      confused = 0
    }

    val diff = ReactionDiff(prev, latest)

    println(diff.hasChanged)
    diff.hasChanged shouldBe true
  }
}
