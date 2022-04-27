import sbt._

object Dependencies {
  lazy val fastparse    = "com.lihaoyi"                %% "fastparse"       % "2.3.3"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.4"
  lazy val logback      = "ch.qos.logback"              % "logback-classic" % "1.2.11"

  lazy val slf4j             = "org.slf4j"      % "slf4j-simple"       % "1.7.36"
  lazy val scalaTest         = "org.scalatest" %% "scalatest"          % "3.2.11"
  lazy val scalaTestFlatspec = "org.scalatest" %% "scalatest-flatspec" % "3.2.11"
}
