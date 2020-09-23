package org.occidere.githubwatcher.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.searches.SearchIterator
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.vo._

import scala.concurrent.duration.{Duration, MINUTES}
import scala.util.Try

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
object ElasticService extends GithubWatcherLogger {
  private lazy val client = ElasticClient(JavaClient(ElasticProperties(s"http://${sys.env.getOrElse("gw_es_endpoint", "localhost:9200")}")))
  private lazy val MAPPER = new ObjectMapper().registerModule(DefaultScalaModule)

  private val GITHUB_USERS = "github-users"
  private val GITHUB_REPOS = "github-repos"
  private val GITHUB_REACTIONS = "github-reactions"

  import com.sksamuel.elastic4s.ElasticDsl._

  def findUserByLogin(login: String): User = Try(
    MAPPER.convertValue(
      client.execute {
        search(GITHUB_USERS).bool(boolQuery().filter(query(s"login.keyword:$login"))).limit(1)
      }.await.result.hits.hits.head.sourceAsMap, classOf[User])
  ).getOrElse(User())

  def saveUser(user: User): Unit = client.execute {
    indexInto(GITHUB_USERS)
      .id(user.id.toString)
      .fields(MAPPER.convertValue(user, classOf[Map[String, Any]]))
      .refreshImmediately
  }.await

  def findAllReposByOwnerLogin(ownerLogin: String): List[Repository] = Try(
    SearchIterator.hits(client,
      search(GITHUB_REPOS)
        .bool(boolQuery().filter(query(s"ownerLogin.keyword:$ownerLogin")))
        .keepAlive("1m")
        .size(100))(Duration(5, MINUTES))
      .map(src => MAPPER.convertValue(src.sourceAsMap, classOf[Repository])).toList
  ).getOrElse(List())

  def saveAllRepos(repos: List[Repository]): Unit = client.execute {
    bulk(repos
      .map(MAPPER.convertValue(_, classOf[Map[String, Any]]))
      .map(repoMap => indexInto(GITHUB_REPOS)
        .id(repoMap("id").toString)
        .fields(repoMap))
    ).refreshImmediately
  }.await

  def findAllReactionsByLogin(login: String): List[Reaction] = Try(
    SearchIterator.hits(client,
      search(GITHUB_REACTIONS)
        .bool(boolQuery().filter(query(s"login.keyword:$login"))) // TODO: Deduplicate scroll search function
        .keepAlive("1m")
        .size(100))(Duration(5, MINUTES))
      .map(src => MAPPER.convertValue(src.sourceAsMap, classOf[Reaction])).toList
  ).getOrElse(List())

  def saveAllReactions(reactions: List[Reaction]): Unit = client.execute {
    bulk(reactions
      .map(MAPPER.convertValue(_, classOf[Map[String, Any]]))
      .map(reactionMap => indexInto(GITHUB_REACTIONS)
        .id(reactionMap("uniqueKey").toString)
        .fields(reactionMap))
    ).refreshImmediately
  }.await

  def close(): Unit = client.close()
}
