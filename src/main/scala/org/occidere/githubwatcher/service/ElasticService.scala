package org.occidere.githubwatcher.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.vo._

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
object ElasticService extends GithubWatcherLogger {
  private lazy val client = ElasticClient(JavaClient(ElasticProperties(
    s"http://${sys.env.getOrElse("gw_es_endpoint", "localhost:9200")}")))

  private lazy val MAPPER = new ObjectMapper().registerModule(DefaultScalaModule)

  private val GITHUB_USERS = "github-users"
  private val GITHUB_REPOS = "github-repos"

  import com.sksamuel.elastic4s.ElasticDsl._

  def findUserByLogin(login: String): User = MAPPER.convertValue(
    client.execute {
      search(GITHUB_USERS).bool(boolQuery().filter(query(s"login:$login"))).limit(1)
    }.await.result.hits.hits.head.sourceAsMap, classOf[User])

  def saveUser(user: User): Unit = client.execute {
    indexInto(GITHUB_USERS)
      .id(user.id.toString)
      .fields(MAPPER.convertValue(user, classOf[Map[String, Any]]))
      .refreshImmediately
  }.await

  def findAllReposByOwnerLogin(ownerLogin: String): Iterable[Repository] = client.execute {
    search(GITHUB_REPOS).bool(boolQuery().filter(query(s"ownerLogin:$ownerLogin")))
  }.await.result.hits.hits.map(src => MAPPER.convertValue(src.sourceAsMap, classOf[Repository]))

  def saveAllRepos(repos: Iterable[Repository]): Unit = client.execute {
    bulk(repos
      .map(MAPPER.convertValue(_, classOf[Map[String, Any]]))
      .map(repoMap => indexInto(GITHUB_REPOS)
        .id(repoMap("id").toString)
        .fields(repoMap))
    ).refreshImmediately
  }.await
}
