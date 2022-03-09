import sbt._

object Dependencies {
  lazy val fastparse         = "com.lihaoyi"       %% "fastparse"          % "2.3.3"
  lazy val twirlApi          = "com.typesafe.play" %% "twirl-api"          % "1.5.1"
  lazy val scalaTest         = "org.scalatest"     %% "scalatest"          % "3.2.11"
  lazy val scalaTestFlatspec = "org.scalatest"     %% "scalatest-flatspec" % "3.2.11"
}
