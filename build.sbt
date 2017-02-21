import scoverage.ScoverageKeys.coverageFailOnMinimum

organization in ThisBuild := "com.knoldus"

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
val cassandraApi = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.0.0"

lazy val `lagom-spike` = (project in file("."))
  .aggregate(`helloworld-producer-api`, `helloworld-producer-impl`, `helloworld-consumer-api`,
    `helloworld-consumer-impl`, `twitter-producer-api`, `twitter-producer-impl`,
    `twitter-consumer-api`, `twitter-producer-impl`)


lazy val `helloworld-producer-api` = (project in file("helloworld-producer-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `helloworld-producer-impl` = (project in file("helloworld-producer-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      lagomScaladslKafkaClient,
      lagomScaladslBroker,
      cassandraApi,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`helloworld-producer-api`)

lazy val `helloworld-consumer-api` = (project in file("helloworld-consumer-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `helloworld-consumer-impl` = (project in file("helloworld-consumer-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      lagomScaladslKafkaClient,
      lagomScaladslBroker,
      cassandraApi,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`helloworld-consumer-api`, `helloworld-producer-api`, `helloworld-producer-impl`)

lazy val `twitter-producer-api` = (project in file("twitter-producer-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `twitter-producer-impl` = (project in file("twitter-producer-impl"))
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
  .dependsOn(`twitter-producer-api`)

lazy val `twitter-consumer-api` = (project in file("twitter-consumer-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
  .dependsOn(`twitter-producer-api`)

lazy val `twitter-consumer-impl` = (project in file("twitter-consumer-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`twitter-consumer-api`)

lagomCassandraEnabled in ThisBuild := false

lagomUnmanagedServices in ThisBuild := Map("cas_native" -> "http://localhost:9042")

lagomKafkaEnabled in ThisBuild := false

lagomKafkaAddress in ThisBuild := "localhost:9092"
