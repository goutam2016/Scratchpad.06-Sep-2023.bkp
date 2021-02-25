import Dependencies._

lazy val root = (project in file(".")).
  enablePlugins(JmhPlugin).
  settings(
      inThisBuild(List(
          organization := "org.gb.sample.scala",
          scalaVersion := "2.13.4",
          version := "0.1.0-SNAPSHOT"
      )),
      name := "simple-scala",
      libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0",
      libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"
  )
