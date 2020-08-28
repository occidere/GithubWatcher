name := "GithubWatcher"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.2",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test
)
