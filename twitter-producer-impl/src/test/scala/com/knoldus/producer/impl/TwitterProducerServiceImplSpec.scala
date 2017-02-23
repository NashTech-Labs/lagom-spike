package com.knoldus.producer.impl

import akka.Done
import akka.stream.testkit.scaladsl.TestSink
import com.knoldus.producer.api.TwitterProducerService
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ServiceTest, TestTopicComponents}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import play.api.libs.ws.ahc.AhcWSComponents

/**
  * Created by Knoldus on 21/2/17.
  */
class TwitterProducerServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  var producerStub: ProducerStub[Tweet] = _

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new TwitterProducerComponents(ctx) with LocalServiceLocator with AhcWSComponents with TestTopicComponents
  }

  override protected def beforeAll(): Unit = server


  lazy val client = server.serviceClient.implement[TwitterProducerService]

  "The add new tweet service " should {
    "save new tweet" in {
      val tweet = Tweet(833556819314409473l, 1487570407000l, 206645598, "javinpaul", "12 Advanced Java Programming " +
        "Books for Experienced Programmer", 7880)
      val value = client.addNewTweet.invoke(tweet)
      client.addNewTweet.invoke(tweet).map { response =>
        response should ===(Done)
      }
    }
  }

  "The kafka producer" should {
    "produce messages to topic" in {

      implicit val system = server.actorSystem
      implicit val materializer = server.materializer

      val tweet = Tweet(833528862332092417l, 1487563741000l, 572225652, "Peter Lawrey", "RT @Independent: Donald Trump " +
        "hasn't filled 94 per cent of federal positions a month into his presidency ", 7880l)

      val client = server.serviceClient.implement[TwitterProducerService]
      client.addNewTweet.invoke(tweet)

      val source = client.twitterTweets.subscribe.atMostOnceSource
        .dropWhile(_.tweetId != tweet.tweetId)
      source.runWith(TestSink.probe[Tweet])
        .request(1)
        .expectNext should === (tweet)
    }
  }

  override protected def afterAll(): Unit = server.stop()
}
