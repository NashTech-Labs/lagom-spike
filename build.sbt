import scoverage.ScoverageKeys.coverageFailOnMinimum

organization in ThisBuild := "com.knoldus"

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
val cassandraApi = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.0.0"
val mockito = "org.mockito" % "mockito-all" % "1.10.19" % Test
val logback = "ch.qos.logback" % "logback-classic" % "1.2.1"
val log     = "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"
val twitter = "org.twitter4j" % "twitter4j-core" % "4.0.6"
val sbr = "com.lightbend.akka" %% "akka-split-brain-resolver" % "1.0.3"

lazy val `lagom-spike` = (project in file("."))
  .aggregate(`helloworld-producer-api`, `helloworld-producer-impl`, `helloworld-consumer-api`,
    `helloworld-consumer-impl`, `twitter-producer-api`, `twitter-producer-impl`,
    `twitter-consumer-api`, `twitter-consumer-impl`)


lazy val `helloworld-producer-api` = (project in file("helloworld-producer-api"))
  .enablePlugins(CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi ,
      logback, log
    )
  )

lazy val `helloworld-producer-impl` = (project in file("helloworld-producer-impl"))
  .enablePlugins(LagomScala,CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      lagomScaladslKafkaClient,
      lagomScaladslBroker,
      cassandraApi,
      macwire,
      scalaTest,
      logback ,log, sbr
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`helloworld-producer-api`)

lazy val `helloworld-consumer-api` = (project in file("helloworld-consumer-api"))
  .enablePlugins(CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      logback ,log
    )
  )

lazy val `helloworld-consumer-impl` = (project in file("helloworld-consumer-impl"))
  .enablePlugins(LagomScala,CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      lagomScaladslKafkaClient,
      lagomScaladslBroker,
      cassandraApi,
      macwire,
      scalaTest,
      mockito,
      logback ,log, sbr
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`helloworld-consumer-api`, `helloworld-producer-api`, `helloworld-producer-impl`)

lazy val `twitter-producer-api` = (project in file("twitter-producer-api"))
  .enablePlugins(CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi ,
      logback ,log
    )
  )

lazy val `twitter-producer-impl` = (project in file("twitter-producer-impl"))
  .enablePlugins(LagomScala,CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      macwire,
      scalaTest,
      mockito,
      twitter,
      logback , log
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`twitter-producer-api`)

lazy val `twitter-consumer-api` = (project in file("twitter-consumer-api"))
  .enablePlugins(CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      logback ,log
    )
  )
  .dependsOn(`twitter-producer-api`)

lazy val `twitter-consumer-impl` = (project in file("twitter-consumer-impl"))
  .enablePlugins(LagomScala,CopyPasteDetector)
  .settings(lagomForkedTestSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      macwire,
      mockito,
      scalaTest,
      logback ,log
    )
  )
  .dependsOn(`twitter-consumer-api`)

// scoverage exludes files configuration according to projects
coverageExcludedPackages in `twitter-producer-impl` :=
  """com.knoldus.twitterproducer.impl.util.TwitterUtil.*;
    |com.knoldus.twitterproducer.impl.TwitterProducerLoader.*;com.knoldus.twitterproducer.impl.TwitterProducerComponents.*;
    |com.knoldus.twitterproducer.impl.TwitterProducerApplication.*;""".stripMargin

coverageExcludedPackages in `twitter-consumer-impl` :=
  """com.knoldus.twitterconsumer.impl.TwitterConsumerLoader;com.knoldus.twitterconsumer.impl.TwitterConsumerComponents;
    |com.knoldus.twitterconsumer.impl.TwitterConsumerApplication;com.knoldus.twitterconsumer.impl.events.TweetEvent;
  """.stripMargin

coverageExcludedPackages in `helloworld-consumer-impl` :=
"""sample.helloworldconsumer.impl.HelloConsumerLoader""".stripMargin

coverageExcludedPackages in `helloworld-producer-impl` :=
"""sample.helloworld.impl.HelloLoader""".stripMargin
// End => scoverage exludes files configuration according to projects


lagomCassandraEnabled in ThisBuild := false

lagomUnmanagedServices in ThisBuild := Map("cas_native" -> "http://localhost:9042")

lagomKafkaEnabled in ThisBuild := false

lagomKafkaAddress in ThisBuild := "localhost:9092"
