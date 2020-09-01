package org.occidere.githubwatcher.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.occidere.githubwatcher.vo.{Repository, User}
import scalaj.http._


/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
object GithubApiService {
  private lazy val TOKEN = scala.util.Properties.envOrElse("gw_github_api_token", "")
  private lazy val MAPPER = new ObjectMapper().registerModule(DefaultScalaModule)

  def getUser(login: String): User = MAPPER.convertValue(getBody(s"users/$login"), classOf[User])

  def getFollowerLogins(login: String): List[String] = getBodies(s"users/$login/followers")
    .map(_.getOrElse("login", "").toString)
    .filter(_.nonEmpty)

  def getRepositories(login: String): List[Repository] = getBodies(s"users/$login/repos").map(MAPPER.convertValue(_, classOf[Repository]))

  def getStargazerLogins(login: String, repo: String): List[String] = getLogins(login, repo)("stargazers")

  def getWatcherLogins(login: String, repo: String): List[String] = getLogins(login, repo)("watchers")

  def getForkLogins(login: String, repo: String): List[String] = getLogins(login, repo)("forks")

  private def getLogins(login: String, repo: String)(userType: String): List[String] = getBodies(s"repos/$login/$repo/$userType")
    .map(_.getOrElse("login", "").toString)
    .filter(_.nonEmpty)

  private def getBody(url: String): Map[String, Any] = MAPPER.readValue(getResponse(url).body, classOf[Map[String, Any]])

  private def getBodies(url: String): List[Map[String, Any]] = Iterator.from(1)
    .map(page => MAPPER.readValue(
      getResponse(s"$url?page=$page").body,
      classOf[List[Map[String, Any]]])
    ).takeWhile(_.nonEmpty)
    .flatten
    .toList

  private def getResponse(url: String): HttpResponse[String] = Http(s"https://api.github.com/$url")
    .option(HttpOptions.followRedirects(true))
    .headers(
      Seq(
        ("User-Agent", "Mozilla/5.0"),
        ("Accept", "application/vnd.github.v3+json"),
        ("Authorization", if (TOKEN.nonEmpty) s"Bearer $TOKEN" else "")
      ).filter(_._2.nonEmpty)
    ).asString
}
