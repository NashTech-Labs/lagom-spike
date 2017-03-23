package com.knoldus.twitterkafkaconsumer.impl

import com.knoldus.twitterkafkaconsumer.api.TwitterConsumerService
import com.knoldus.twitterkafkaconsumer.impl.repositories.TwitterRepository
import com.knoldus.twitterproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.Future

/**
  * Created by harmeet on 24/2/17.
  */
class TwitterConsumerServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with MockitoSugar {

  val limit = 10
  var producerStub: ProducerStub[Tweet] = _
  val tweet = Tweet(833556819314409473l, 1487570407000l, 206645598, "javinpaul", "12 Advanced Java Programming " +
    "Books for Experienced Programmer", 7880)

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new TwitterKafkaConsumerComponents(ctx) with LocalServiceLocator with AhcWSComponents {

      val stubFactory = new ProducerStubFactory(actorSystem, materializer)
      producerStub = stubFactory.producer[Tweet](TwitterProducerService.TOPIC_NAME)
      override lazy val twitterService = new TwitterServiceStub(producerStub)

      override lazy val twitterRepository = mock[TwitterRepository]
      when(twitterRepository.fetchAllLatestTweets(limit)).thenReturn(Future.successful(Seq(tweet)))
    }
  }

  override protected def beforeAll(): Unit = server

  val client = server.serviceClient.implement[TwitterConsumerService]

  "latest tweets " should {
    "fetch with limit " in {

      client.findLatestTweets(limit).invoke().map{ tweets =>
        tweets.head should === (tweet)
      }
    }
  }

  override protected def afterAll(): Unit = server.stop()
}
