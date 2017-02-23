package com.knoldus.consumer.impl

import akka.Done
import com.knoldus.producer.api.TwitterProducerService
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest, TestTopicComponents}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.Future

/**
  * Created by harmeet on 23/2/17.
  */
class TwitterProducerSubscriberSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  var producerStub: ProducerStub[Tweet] = _

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new TwitterConsumerApplication(ctx) with LocalServiceLocator with TestTopicComponents {

      val stubFactory = new ProducerStubFactory(actorSystem, materializer)

      producerStub = stubFactory.producer[Tweet](TwitterProducerService.TOPIC_NAME)

      println(s">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ${producerStub}")

      override lazy val twitterService = new TwitterServiceStub(producerStub)
    }
  }

  val client = server.serviceClient.implement[TwitterProducerService]

  override protected def beforeAll(): Unit = server

  "some text" should {
    "equals some text" in {
      Future { "a" should === ("a") }
    }
  }
 /* "Twitter kafka consumer " should {
    "consume tweets from topic" in {

      val tweet = Tweet(833556819314409473l, 1487570407000l, 206645598, "javinpaul", "12 Advanced Java Programming " +
        "Books for Experienced Programmer", 7880)

      producerStub.send(tweet)

      client.addNewTweet.invoke(tweet).map { resp =>
        resp should === (tweet)
      }
    }
  }*/

  override protected def afterAll(): Unit = server.stop()
}

class TwitterServiceStub(stub: ProducerStub[Tweet]) extends TwitterProducerService {

  def addNewTweet: ServiceCall[Tweet, Done] = ???

  def twitterTweets: Topic[Tweet] = stub.topic
}
