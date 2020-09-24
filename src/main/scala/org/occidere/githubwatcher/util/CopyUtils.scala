package org.occidere.githubwatcher.util

import org.occidere.githubwatcher.vo.Reaction

object CopyUtils {

  def copyReactionWithoutCounts(src: Reaction): Reaction = new Reaction(src.id, src.originDataType) {
    htmlUrl = src.htmlUrl
    login = src.login
    title = src.title
    body = src.body
    repoName = src.repoName
  }
}
