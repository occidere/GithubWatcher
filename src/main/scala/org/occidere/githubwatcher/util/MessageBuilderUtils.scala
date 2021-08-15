package org.occidere.githubwatcher.util

import org.occidere.githubwatcher.vo.{FollowerDiff, ReactionDiff, RepositoryDiff}
import org.occidere.githubwatcher.util.ReactionEmojiConvertUtils._

/**
 * @author occidere
 * @Blog: https://blog.naver.com/occidere
 * @Github: https://github.com/occidere
 * @since 2020-09-01
 */
object MessageBuilderUtils {
  def createFollowerMessage(diff: FollowerDiff): String =
    s"""${
      if (diff.newFollowerLogins.isEmpty) ""
      else s"[${diff.newFollowerLogins.size} of new Followers]\n${diff.newFollowerLogins.sorted.mkString("\n")}\n"
    }${
      if (diff.deletedFollowerLogins.isEmpty) ""
      else s"[${diff.deletedFollowerLogins.size} of deleted followers]\n${diff.deletedFollowerLogins.sorted.mkString("\n")}\n"
    }
       |""".stripMargin.strip

  def createRepositoryMessage(diff: RepositoryDiff): String =
    s"""[${diff.repoName}] ${
      if (diff.hasNewChanged) "\n[NEW]" +
        s"${if (diff.newStargazerLogins.isEmpty) "" else s"\n- STAR (${diff.newStargazerLogins.size})\n${prettyLoginList(diff.newStargazerLogins)}"}" +
        s"${if (diff.newForkLogins.isEmpty) "" else s"\n- FORK (${diff.newForkLogins.size})\n${prettyLoginList(diff.newForkLogins)}"}" +
        s"${if (diff.newWatcherLogins.isEmpty) "" else s"\n- WATCH (${diff.newWatcherLogins.size})\n${prettyLoginList(diff.newWatcherLogins)}"}"
      else ""
    }${
      if (diff.hasDelChanged) "\n[DELETED]" +
        s"${if (diff.delStargazerLogins.isEmpty) "" else s"\n- STAR (${diff.delStargazerLogins.size})\n${prettyLoginList(diff.delStargazerLogins)}"}" +
        s"${if (diff.delForkLogins.isEmpty) "" else s"\n- FORK (${diff.delForkLogins.size})\n${prettyLoginList(diff.delForkLogins)}"}" +
        s"${if (diff.delWatcherLogins.isEmpty) "" else s"\n- WATCH (${diff.delWatcherLogins.size})\n${prettyLoginList(diff.delWatcherLogins)}"}"
      else ""
    }
       |""".stripMargin.strip

  def createReactionMessage(diff: ReactionDiff): String =
    s"""[Reaction Changed]
       |${diff.latestReaction.title}
       |${diff.latestReaction.body}
       |
       |${
      (if (diff.thumbUpDelta == 0) "" else s"${getEmoji("thumbUp")} ${diff.prevReaction.thumbUp} -> ${diff.latestReaction.thumbUp}\n") +
        (if (diff.laughDelta == 0) "" else s"${getEmoji("laugh")} ${diff.prevReaction.laugh} -> ${diff.latestReaction.laugh}\n") +
        (if (diff.heartDelta == 0) "" else s"${getEmoji("heart")} ${diff.prevReaction.heart} -> ${diff.latestReaction.heart}\n") +
        (if (diff.hoorayDelta == 0) "" else s"${getEmoji("hooray")} ${diff.prevReaction.hooray} -> ${diff.latestReaction.hooray}\n") +
        (if (diff.rocketDelta == 0) "" else s"${getEmoji("rocket")} ${diff.prevReaction.rocket} -> ${diff.latestReaction.rocket}\n") +
        (if (diff.eyesDelta == 0) "" else s"${getEmoji("eyes")} ${diff.prevReaction.eyes} -> ${diff.latestReaction.eyes}\n") +
        (if (diff.thumbDownDelta == 0) "" else s"${getEmoji("thumbDown")} ${diff.prevReaction.thumbDown} -> ${diff.latestReaction.thumbDown}\n") +
        (if (diff.confusedDelta == 0) "" else s"${getEmoji("confused")} ${diff.prevReaction.confused} -> ${diff.latestReaction.confused}\n")
    }
       |${diff.latestReaction.htmlUrl}
       |""".stripMargin

  private def prettyLoginList(logins: List[String], indent: Int = 2): String = {
    val prefix = " ".repeat(Math.max(0, indent)) + "-"
    logins.sorted.map(login => s"$prefix $login").mkString("\n")
  }
}
