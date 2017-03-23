package com.knoldus.twitterkafkaconsumer.impl.repositories

import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.ReadSide
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble

/**
  * Created by harmeet on 23/2/17.
  */
class TwitterRepositorySpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  var producerStub: ProducerStub[Tweet] = _

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new TwitterConsumerComponents(ctx) with LocalServiceLocator with AhcWSComponents {

      val stubFactory = new ProducerStubFactory(actorSystem, materializer)

      producerStub = stubFactory.producer[Tweet](TwitterProducerService.TOPIC_NAME)

      override lazy val twitterService = new TwitterServiceStub(producerStub)

      override lazy val readSide: ReadSide = new ReadSideTestDriver
    }
  }

  val cassandraSession = server.application.cassandraSession

  override protected def beforeAll(): Unit = {


    val f = cassandraSession.executeCreateTable(
      """
          CREATE TABLE IF NOT EXISTS tweets (
           tweet_id bigint,
           created_at bigint,
           user_id bigint,
           tweet_user_name text,
           text text,
           friends_count bigint,
           PRIMARY KEY (tweet_id, created_at)) WITH CLUSTERING ORDER BY (created_at DESC)
      """)

    Await.result(f, 10 seconds)
  }

  "records " should {
    "fetch from tweets table " in {

      val f = cassandraSession.prepare(
        """INSERT INTO
          |tweets(tweet_id, created_at, user_id, tweet_user_name, text, friends_count)
          |VALUES(?, ?, ?, ?, ?, ?)
          |""".stripMargin)
        .map(statement => {
          statement.bind(long2Long(833556819314409473l), long2Long(1487570407000l), long2Long(206645598),
            "javinpaul", "12 Advanced Java Programming Books for Experienced Programmer", long2Long(7880))
        })

      for{
        statement <- f
        _ <- cassandraSession.executeWrite(statement)
      } yield {
        val tweet = Tweet(833556819314409473l, 1487570407000l, 206645598, "javinpaul", "12 Advanced Java Programming " +
          "Books for Experienced Programmer", 7880)

        val fResults = server.application.twitterRepository.fetchAllLatestTweets(10)
        val results = Await.result(fResults, 10 seconds).toList

        results.head should === (tweet)
      }
    }
  }
}
