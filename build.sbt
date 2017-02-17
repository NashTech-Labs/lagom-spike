organization in ThisBuild := "com.knoldus"

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild :=  "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lazy val `lagom-spike` = (project in file("."))
      .aggregate(`twitter-api`, `twitter-impl`)


lazy val `twitter-api` = (project  in file("twitter-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `twitter-impl` = (project in file("twitter-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      macwire,
      scalaTest,
      "org.twitter4j" % "twitter4j-core" % "4.0.6"
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`twitter-api`)

lagomKafkaEnabled in ThisBuild := false

lagomKafkaAddress in ThisBuild := "localhost:9092"