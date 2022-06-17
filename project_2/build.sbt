ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

ThisBuild / name := "akka_flyway_project"

enablePlugins(FlywayPlugin)


flywayUrl := "jdbc:postgresql://localhost:5432/akkaProjectDB"
flywayUser := "postgres"
flywayPassword := "admin"
flywayLocations += "db/migration"


lazy val library = new {

  object Version {
    val akka = "2.6.19"
    val akkaHttp = "10.2.9"
    val slick = "3.3.3"
  }

  lazy val akka: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % Version.akka,
    "com.typesafe.akka" %% "akka-stream" % Version.akka,
    "com.typesafe.akka" %% "akka-actor-typed" % Version.akka
  )

  val akkaHttp: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
    "com.typesafe.akka" %% "akka-http-core" % Version.akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttp
  )

  val slick: Seq[ModuleID] = Seq(
    "com.typesafe.slick" %% "slick" % Version.slick,
    "com.typesafe.slick" %% "slick-hikaricp" % Version.slick,
    "org.postgresql" % "postgresql" % "42.3.6"
  )
}

lazy val root = (project in file("."))
  .settings(
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.11",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    libraryDependencies += "com.typesafe" % "config" % "1.4.2",
    libraryDependencies ++= library.slick ++ library.akkaHttp ++ library.akka,
  )