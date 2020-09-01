package org.occidere.githubwatcher.util

import org.occidere.githubwatcher.vo.{FollowerDiff, RepositoryDiff}

/**
 * @author occidere
 * @Blog: https://occidere.blog.me
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
        s"${if (diff.newStargazerLogins.isEmpty) "" else s"\n- STAR(${diff.newStargazerLogins.size})\n${prettyLoginList(diff.newStargazerLogins)}"}" +
        s"${if (diff.newForkLogins.isEmpty) "" else s"\n- FORK(${diff.newForkLogins.size})\n${prettyLoginList(diff.newForkLogins)}"}" +
        s"${if (diff.newWatcherLogins.isEmpty) "" else s"\n- WATCH(${diff.newWatcherLogins.size})\n${prettyLoginList(diff.newWatcherLogins)}"}"
      else ""
    }${
      if (diff.hasDelChanged) "\n[DELETED]" +
        s"${if (diff.delStargazerLogins.isEmpty) "" else s"\n- STAR(${diff.delStargazerLogins.size})\n${prettyLoginList(diff.delStargazerLogins)}"}" +
        s"${if (diff.delForkLogins.isEmpty) "" else s"\n- FORK(${diff.delForkLogins.size})\n${prettyLoginList(diff.delForkLogins)}"}" +
        s"${if (diff.delWatcherLogins.isEmpty) "" else s"\n- WATCH(${diff.delWatcherLogins.size})\n${prettyLoginList(diff.delWatcherLogins)}"}"
      else ""
    }
       |""".stripMargin.strip

  private def prettyLoginList(logins: List[String], indent: Int = 2): String = {
    val prefix = " ".repeat(Math.max(0, indent)) + "-"
    logins.sorted.map(login => s"$prefix $login").mkString("\n")
  }
}
