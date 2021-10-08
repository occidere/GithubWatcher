package org.occidere.githubwatcher.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.sksamuel.elastic4s.requests.searches.SearchIterator
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.vo._

import scala.concurrent.duration.{Duration, MINUTES}
import scala.util.Try

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
object ElasticService extends GithubWatcherLogger {
  private lazy val MAPPER = new ObjectMapper().registerModule(DefaultScalaModule)

  private val esUsername = sys.env.get("gw_es_username")
  private val esPassword = sys.env.get("gw_es_password")

  private lazy val client = ElasticClient(
    com.sksamuel.elastic4s.http.JavaClient(
      ElasticProperties(s"http://${sys.env.getOrElse("gw_es_endpoint", "localhost:9200")}"),
      (requestConfigBuilder: RequestConfig.Builder) => requestConfigBuilder
        .setSocketTimeout(60000)
        .setConnectTimeout(10000)
        .setConnectionRequestTimeout(10000),
      // Set credentials if exist
      (httpClientBuilder: HttpAsyncClientBuilder) => httpClientBuilder.setDefaultCredentialsProvider(
        new BasicCredentialsProvider() {
          if (hasCredentials) setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esUsername.get, esPassword.get))
        }
      )
    )
  )

  private val GITHUB_USERS = "github-users"
  private val GITHUB_REPOS = "github-repos"
  private val GITHUB_REACTIONS = "github-reactions"

  import com.sksamuel.elastic4s.ElasticDsl._

  def findUserByLogin(login: String): User = Try(
    MAPPER.convertValue(
      client.execute {
        search(GITHUB_USERS).bool(boolQuery().filter(query(s"login.keyword:$login"))).limit(1)
      }.await.result.hits.hits.head.sourceAsMap, classOf[User])
  ).getOrElse {
    logger.error("findUserByLogin failed!")
    User()
  }

  def saveUser(user: User): Unit = client.execute {
    indexInto(GITHUB_USERS)
      .id(user.id.toString)
      .fields(MAPPER.convertValue(user, classOf[Map[String, Any]]))
      .refreshImmediately
  }.await

  def findAllReposByOwnerLogin(ownerLogin: String): List[Repository] = Try(
    scrollFilterSearch(GITHUB_REPOS, s"ownerLogin.keyword:$ownerLogin", classOf[Repository])
  ).getOrElse {
    logger.error("findAllReposByOwnerLogin failed!")
    List()
  }

  def saveAllRepos(repos: Iterable[Repository]): Unit = bulkIndex(GITHUB_REPOS, "id", repos)

  def deleteAllReposById(ids: Iterable[String]): Unit = client.execute(bulk(ids.map(deleteById(GITHUB_REPOS, _))).refreshImmediately).await

  def findAllReactionsByLogin(login: String): List[Reaction] = Try(
    scrollFilterSearch(GITHUB_REACTIONS, s"login.keyword:$login", classOf[Reaction])
  ).getOrElse {
    logger.error("findAllReactionsByLogin failed!")
    List()
  }

  def saveAllReactions(reactions: Iterable[Reaction]): Unit = bulkIndex(GITHUB_REACTIONS, "uniqueKey", reactions)

  def deleteAllReactionsByUniqueKeys(uniqueKeys: Iterable[String]): Unit =
    client.execute(bulk(uniqueKeys.map(deleteById(GITHUB_REACTIONS, _))).refreshImmediately).await

  def close(): Unit = client.close()

  private def hasCredentials = esUsername.isDefined && esPassword.isDefined;

  private def scrollFilterSearch[T](index: String, queryString: String, dataType: Class[T]): List[T] = SearchIterator.hits(client,
    search(index)
      .bool(boolQuery().filter(query(queryString)))
      .keepAlive("1m")
      .size(100))(Duration(5, MINUTES))
    .map(src => MAPPER.convertValue(src.sourceAsMap, dataType)).toList

  private def bulkIndex[T](index: String, idFieldName: String, data: Iterable[T]): Unit = client.execute {
    bulk(data
      .map(MAPPER.convertValue(_, classOf[Map[String, Any]]))
      .map(dataMap => indexInto(index)
        .id(dataMap(idFieldName).toString)
        .fields(dataMap))
    ).refreshImmediately
  }.await
}
