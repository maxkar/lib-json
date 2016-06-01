crossPaths := false

name := "lib-json"

organization := "ru.maxkar"

description := 
  """
  Easy-to-use and lightweight JSON parsing/generation library.
  Provides nice syntax for the library user.
  """

version := "0.0.2"

scalaSource in Compile := baseDirectory.value / "src"

scalaSource in Test := baseDirectory.value / "test"

target := baseDirectory.value / ".target"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.2" % "test"

scalacOptions += "-feature"
