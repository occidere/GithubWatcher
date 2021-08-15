name := "GithubWatcher"

version := "1.0.0"

scalaVersion := "2.13.4"

test in assembly := {}
mainClass in assembly := Some("org.occidere.githubwatcher.GithubWatcher")
assemblyJarName in assembly := "GithubWatcher.jar"
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case "unwanted.txt" => MergeStrategy.discard
  case _ => MergeStrategy.last
}

coverageMinimumStmtTotal := 50
coverageMinimumBranchTotal := 50
coverageHighlighting := true
coverageFailOnMinimum := false
coverageEnabled.in(Test, test) := true
coverageEnabled.in(Compile, compile) := false

val elastic4sVersion = "7.13.0"
val jacksonVersion = "2.12.4"
val lineBotVersion = "4.4.0"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.32",
  "ch.qos.logback" % "logback-classic" % "1.2.5",
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion % Provided,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion % Provided,
  "com.linecorp.bot" % "line-bot-api-client" % lineBotVersion,
  "com.linecorp.bot" % "line-bot-model" % lineBotVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)
