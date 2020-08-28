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
case class User(id: Long = -1L,
                login: String = "",
                name: String = "",
                company: String = "",
                blog: String = "",
                location: String = "",
                email: String = "",
                bio: String = "",
                @JsonProperty("avatar_url") var avatarUrl: String = "",
                @JsonProperty("html_url") var htmlUrl: String = "") {
  var followerLogins: Iterable[String] = List[String]()
  var repositories: Iterable[Repository] = List[Repository]()
}
