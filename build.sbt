name := "Grafy"

version := "1.0"

publishMavenStyle := true

organization := "com.lifeTweet"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature")

val playVersion = "2.5.3"


resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.play" %% "play-json" % playVersion,
  "com.typesafe.play" %% "play-ws" % playVersion,
  "com.typesafe.play" %% "play-iteratees" % playVersion
)


parallelExecution in Test := false
//sbt.version=0.13.11

//mainClass := Some("Application.scala")