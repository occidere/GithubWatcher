package org.occidere.githubwatcher.enums


/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-09-23
 */
object GithubDataType extends Enumeration {

  type ReactionOriginDataType = Value

  val ISSUE: Value = Value("issue")
  val COMMENT: Value = Value("comment")
}
