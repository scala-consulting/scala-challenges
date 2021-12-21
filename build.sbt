val scala3Version = "3.1.0"

ThisBuild / scalaVersion := scala3Version

val catsEffect = "org.typelevel" %% "cats-effect" % "3.3.0"

val commonSettings = Seq(
  scalaVersion := scala3Version
)

lazy val root = (project in file("."))
  .aggregate(
    concurrency
  )
  .settings(
    name := "scala-io-example"
  )

lazy val concurrency = project
  .in(file("6_scala-concurrency"))
  .settings(
    name := "concurrency",
    libraryDependencies ++= Seq(catsEffect)
  )
  .settings(commonSettings :_*)

