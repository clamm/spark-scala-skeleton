import sbt.Build
import sbt._
import sbt.Keys._

object Build extends Build {

  lazy val commonSettings = Seq(
    scalaVersion := "2.11.7",
    organization := "com.skeleton",
    // see `scalac -help` for options
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-Xlint:_",
      "-Ywarn-dead-code",
      "-Yclosure-elim",
      "-Yinline",
      "-Xverify"
    ),
    libraryDependencies ++= coreDependencies,
    parallelExecution in Test := false
  )

  lazy val skeleton = Project(
    id = "skeleton",
    base = file("."),
    settings = Defaults.coreDefaultSettings ++ commonSettings
  )

  lazy val sparkVersion = "1.6.0"

  lazy val coreDependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0" excludeAll (ExclusionRule(organization = "org.slf4j"), ExclusionRule(organization = "org.scala-lang")),
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.json4s" %% "json4s-native" % "3.3.0",
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "com.holdenkarau" % "spark-testing-base_2.11" % "1.6.0_0.3.1",
    "com.github.nscala-time" %% "nscala-time" % "2.6.0",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2",
    "org.scalactic" %% "scalactic" % "2.2.6",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )
}

