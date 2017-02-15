organization in ThisBuild := "com.knoldus"

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild :=  "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
val cassandraApi = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.0.0";

lazy val `lagom-spike` = (project in file("."))
    .aggregate(`consumer-api`, `consumer-impl`, `producer-api`, `producer-impl`)


lazy val `consumer-api` = (project  in file("consumer-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `consumer-impl` = (project in file("consumer-impl"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      cassandraApi,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`consumer-api`)

lazy val `producer-api` = (project  in file("producer-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `producer-impl` = (project in file("producer-impl"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      cassandraApi,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`producer-api`)