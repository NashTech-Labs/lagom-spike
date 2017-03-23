package com.knoldus.twitterconsumer.impl.events

import java.util.concurrent.atomic.AtomicInteger

import akka.persistence.query.Sequence
import com.knoldus.twitterconsumer.impl.{ReadSideTestDriver, TwitterConsumerComponents, TwitterServiceStub}
import com.knoldus.twitterkafkaproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.Future

/**
  * Created by harmeet on 24/2/17.
  */

class TweetEventProcessorSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  var producerStub: ProducerStub[Tweet] = _

  private lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new TwitterConsumerComponents(ctx) with LocalServiceLocator {

      val stubFactory = new ProducerStubFactory(actorSystem, materializer)
      producerStub = stubFactory.producer[Tweet](TwitterProducerService.TOPIC_NAME)
      override lazy val twitterService = new TwitterServiceStub(producerStub)

      override lazy val readSide: ReadSideTestDriver = new ReadSideTestDriver
    }
  }

  private val testDriver = server.application.readSide
  private val repository = server.application.twitterRepository
  private val offset = new AtomicInteger()

  override protected def beforeAll(): Unit = server

  "tweet event processor" should {
    "create insert a tweet" in {
      val tweet = Tweet(833556819314409473l, 1487570407000l, 206645598, "javinpaul", "12 Advanced Java Programming " +
        "Books for Experienced Programmer", 7880)

      for {
        _ <- feed(tweet.tweetId, TweetSaved(tweet))
        items <- repository.fetchAllLatestTweets(10)
      } yield items.head should ===(tweet)
    }
  }

  private def feed(tweetId: Long, event: TweetEvent) = {
    testDriver.feed(tweetId.toString, event, Sequence(offset.getAndIncrement()))
  }

  override protected def afterAll(): Unit = server.stop()
}
