//name := "playground"
//
//version := "0.1"
//
//scalaVersion := "2.12.1"
//
//libraryDependencies += "org.typelevel" %% "cats" % "0.9.0"
//
//libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test"

lazy val compilerSettings = Seq(
  scalacOptions ++= Seq(
    "-language:higherKinds",
    "-encoding", "UTF-8",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-language:existentials"
  )
//  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3"),
//  addCompilerPlugin("com.github.mpilquist" %% "simulacrum" % "0.10.0")
)

lazy val buildSettings = Seq(
  scalaVersion := "2.13.2",
  name := "exercises",
  version := "0.1.0-SNAPSHOT"
)

lazy val catsVersion = "2.1.1"

lazy val commonSettings = Seq(
//  resolvers := commonResolvers,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-free" % catsVersion,

    "org.scalatestplus" %% "scalacheck-1-14" % "3.1.1.1" % Test
  )
) ++ compilerSettings

lazy val root = (project in file(".")).settings(
  buildSettings,
  commonSettings
)
