package org.occidere.githubwatcher.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.occidere.githubwatcher.vo.{Reaction, Repository, User}
import scalaj.http._

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-28
 */
object GithubApiService {
  private lazy val TOKEN = sys.env.getOrElse("gw_github_api_token", "")
  private lazy val MAPPER = new ObjectMapper().registerModule(DefaultScalaModule)

  def getUser(login: String): User = MAPPER.convertValue(getBody(s"users/$login"), classOf[User])

  def getFollowerLogins(login: String): List[String] = getBodies(s"users/$login/followers")
    .map(_.getOrElse("login", "").toString)
    .filter(_.nonEmpty)

  def getReactionsOfIssuesCreatedByUser: List[Reaction] = getBodies("issues", Map("per_page" -> "100", "filter" -> "created", "state" -> "all"))
    .map(MAPPER.convertValue(_, classOf[Reaction]))

  def getReactionsOfIssuesInRepository(login: String, repo: String): List[Reaction] = getBodies(s"repos/$login/$repo/issues/comments")
    .map(MAPPER.convertValue(_, classOf[Reaction]))
    .filter(_.login == login)

  def getRepositories(login: String): List[Repository] = getBodies(s"users/$login/repos").map(MAPPER.convertValue(_, classOf[Repository]))

  def getStargazerLogins(login: String, repo: String): List[String] = getLogins(login, repo)("stargazers")

  def getWatcherLogins(login: String, repo: String): List[String] = getLogins(login, repo)("watchers")

  def getForkLogins(login: String, repo: String): List[String] = getLogins(login, repo)("forks")

  private def getLogins(login: String, repo: String)(actionType: String): List[String] = getBodies(s"repos/$login/$repo/$actionType")
    .map(_.getOrElse("login", "").toString)
    .filter(_.nonEmpty)

  private def getBody(url: String, params: Map[String, String] = Map()): Map[String, Any] = MAPPER.readValue(getResponse(url, params).body, classOf[Map[String, Any]])

  private def getBodies(url: String, params: Map[String, String] = Map()): List[Map[String, Any]] = Iterator.from(1)
    .map(page => MAPPER.readValue(
      getResponse(url, params + ("page" -> page.toString)).body,
      classOf[List[Map[String, Any]]]))
    .takeWhile(_.nonEmpty)
    .flatten
    .toList

  private def getResponse(url: String, params: Map[String, String] = Map()): HttpResponse[String] = Http(s"https://api.github.com/$url${toUrlEncoded(params)}")
    .option(HttpOptions.followRedirects(true))
    .headers(
      Seq(
        ("User-Agent", "Mozilla/5.0"),
        ("Accept", "application/vnd.github.squirrel-girl-preview"),
        ("Authorization", if (TOKEN.nonEmpty) s"Bearer $TOKEN" else "")
      ).filter(_._2.nonEmpty)
    ).asString

  private def toUrlEncoded(params: Map[String, String]): String =
    if (params.nonEmpty) s"?${params.map(kv => s"${kv._1}=${kv._2}").mkString("&")}"
    else ""
}
