package org.occidere.githubwatcher.task

import org.occidere.githubwatcher.GithubWatcher
import org.scalatest.PrivateMethodTester
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-09
 */
class GithubWatcherTaskTest extends AnyFlatSpec with PrivateMethodTester with should.Matchers {
  "All tasks test" should "finished successfully" in {
    GithubWatcher.main(Array())
  }
}
