package org.occidere.githubwatcher.service

import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.response.BotApiResponse
import org.occidere.githubwatcher.logger.GithubWatcherLogger
import org.occidere.githubwatcher.util.MessageBuilderUtils._
import org.occidere.githubwatcher.vo.{FollowerDiff, ReactionDiff, RepositoryDiff}

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-08-31
 */
object LineMessengerService extends GithubWatcherLogger {
  private lazy val BOT_ID = s"${sys.env.getOrElse("gw_line_bot_id", "")}"
  private lazy val lineMessagingClient: LineMessagingClient = LineMessagingClient
    .builder(s"${sys.env.getOrElse("gw_line_channel_token", "")}")
    .build()

  def sendFollowerMessage(diff: FollowerDiff): Unit = sendMessageIfExist(createFollowerMessage(diff))

  def sendRepositoryMessage(diff: RepositoryDiff): Unit = sendMessageIfExist(createRepositoryMessage(diff))

  def sendReactionMessage(diff: ReactionDiff): Unit = sendMessageIfExist(createReactionMessage(diff))

  private def sendMessageIfExist(msg: String): Unit = if (msg.nonEmpty) {
    val resp: BotApiResponse = lineMessagingClient.pushMessage(new PushMessage(BOT_ID, new TextMessage(msg))).get()
    logger.info(s"Message send response: $resp")
  }
}
