import sbt._
import Keys._

object HelloBuild extends Build {
  lazy val stdlib = (
    project in file("stdlib")
  )
  lazy val compiler = (
    project in file("compiler")
      dependsOn stdlib
  )
}