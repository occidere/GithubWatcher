package org.occidere.githubwatcher.service

import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.response.BotApiResponse
import org.occidere.githubwatcher.logger.GithubWatcherLogger

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
object LineMessengerService extends GithubWatcherLogger {
  private lazy val BOT_ID = s"${sys.env.getOrElse("gw_line_bot_id", "")}"
  private lazy val lineMessagingClient: LineMessagingClient = LineMessagingClient
    .builder(s"${sys.env.getOrElse("gw_line_channel_token", "")}")
    .build()

  def sendFollowerMessageIfExist(newFollowerLogins: List[String], deletedFollowerLogins: List[String]): Unit =
    sendMessageIfExist(createFollowerMessage(newFollowerLogins, deletedFollowerLogins))

  def sendRepositoryMessageIfExist(/* RepositoryDiff */): Unit = {} // TODO

  private def sendMessageIfExist(msg: String): Unit = if (msg.nonEmpty) {
    val resp: BotApiResponse = lineMessagingClient.pushMessage(new PushMessage(BOT_ID, new TextMessage(msg))).get()
    logger.info(s"Message send response: ${resp}")
  }

  private def createFollowerMessage(newFollowerLogins: List[String], deletedFollowerLogins: List[String]): String =
    s"""${
      if (newFollowerLogins.isEmpty) ""
      else s"[${newFollowerLogins.size} of new Followers]\n${newFollowerLogins.sorted.mkString("\n")}\n"
    }${
      if (deletedFollowerLogins.isEmpty) ""
      else s"[${deletedFollowerLogins.size} of deleted followers]\n${deletedFollowerLogins.sorted.mkString("\n")}\n"
    }
       |""".stripMargin

  private def createRepositoryMessage(/* RepositoryDiff */): Unit = {} // TODO

}
