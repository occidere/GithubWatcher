package org.occidere.githubwatcher.vo

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonInclude, JsonProperty}

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-09-23
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
case class Reaction(id: Int, originDataType: String) {
  @JsonProperty("html_url")
  var htmlUrl: String = ""
  var login: String = ""
  var title: String = ""
  var body: String = ""
  var repoName: String = ""
  var repoOwnerLogin: String = ""

  var totalCount: Int = 0
  var laugh: Int = 0
  var hooray: Int = 0
  var confused: Int = 0
  var heart: Int = 0
  var rocket: Int = 0
  var eyes: Int = 0
  var thumbUp: Int = 0
  var thumbDown: Int = 0

  lazy val uniqueKey: String = s"$originDataType$id".hashCode.toString

  @JsonProperty("reactions")
  private def unpackReactions(reactions: Map[String, Any]): Unit = {
    totalCount = reactions("total_count").asInstanceOf[Int]
    laugh = reactions("laugh").asInstanceOf[Int]
    hooray = reactions("hooray").asInstanceOf[Int]
    confused = reactions("confused").asInstanceOf[Int]
    heart = reactions("heart").asInstanceOf[Int]
    rocket = reactions("rocket").asInstanceOf[Int]
    eyes = reactions("eyes").asInstanceOf[Int]
    thumbUp = reactions("+1").asInstanceOf[Int]
    thumbDown = reactions("-1").asInstanceOf[Int]
  }

  @JsonProperty("repository")
  private def unpackRepository(repository: Map[String, Any]): Unit = {
    repoName = repository("name").toString
    repoOwnerLogin = repository("owner").asInstanceOf[Map[String, Any]]("login").toString
  }

  @JsonProperty("user")
  private def unpackUser(user: Map[String, Any]): Unit = login = user("login").toString

  @JsonProperty("title")
  private def shortenTitle(title: String): Unit = this.title = shortenString(title.trim)

  @JsonProperty("body")
  private def shortenBody(body: String): Unit = this.body = shortenString(body.trim)

  private def shortenString(str: String, maxLen: Int = 20): String = if (str.length > maxLen) str.substring(0, maxLen - 3) + "..." else str
}
