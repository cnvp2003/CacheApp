import sbt.Keys.libraryDependencies

name := "AgodaAssignment"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % Test,
  "net.sf.ehcache" % "ehcache" % "2.10.2.2.21",
  "joda-time" % "joda-time" % "2.9.7",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)