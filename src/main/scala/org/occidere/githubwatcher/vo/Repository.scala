package org.occidere.githubwatcher.vo

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonInclude, JsonProperty}

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
case class Repository(id: String = "",
                      name: String = "",
                      description: String = "") {
  var ownerId: String = ""
  var ownerLogin: String = ""
  var forkLogins: List[String] = List()
  var watcherLogins: List[String] = List()
  var stargazerLogins: List[String] = List()

  @JsonProperty("owner")
  private def unpackOwner(owner: Map[String, Any]): Unit = {
    ownerId = owner("id").toString
    ownerLogin = owner("login").toString
  }
}
