crossPaths := true

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.6")

name := "lib-json"

organization := "ru.maxkar"

description := 
  """
  Easy-to-use and lightweight JSON parsing/generation library.
  Provides nice syntax for the library user.
  """

version := "0.0.5-SNAPSHOT"

scalaSource in Compile := baseDirectory.value / "src"

scalaSource in Test := baseDirectory.value / "test"

target := baseDirectory.value / ".target"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

scalacOptions += "-feature"
