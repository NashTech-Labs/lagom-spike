package com.knoldus.producer.impl

import akka.Done
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.knoldus.producer.api.models.Tweet
import com.knoldus.producer.api.{TwitterProducerService, TwitterSchedulerService}
import com.knoldus.producer.impl.util.TwitterUtil
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

/**
  * Created by Knoldus on 22/2/17.
  */

class TwitterSchedulerServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with MockitoSugar {

  val mockTwitterService = mock[TwitterProducerService]

  when(mockTwitterService.addNewTweet).thenReturn(ServiceCall[Tweet, Done] { tweet => Future.successful(Done) })

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new TwitterProducerApplication(ctx) with LocalServiceLocator {

      override lazy val twitterService = mockTwitterService
    }
  }

  override protected def beforeAll(): Unit = server

  lazy val client = server.serviceClient.implement[TwitterSchedulerService]

  "twitter scheduler" should {
    "start for fetching tweets" in {

      client.scheduler.invoke().map { response =>
        response should ===(Done)
      }
    }
  }

  "twitter actor" should {
    "invoke twitterService" in {
      implicit val system = ActorSystem()
      implicit val timeout = Timeout(10 seconds)

      val tweet = Tweet(833556819314409473l, 1487570407000l, 206645598, "javinpaul", "12 Advanced Java Programming " +
        "Books for Experienced Programmer", 7880)

      val tweetutils = mock[TwitterUtil]
      when(tweetutils.fetchTweets).thenReturn(List(tweet))

      val actorRef = TestActorRef(new TwitterActor(mockTwitterService, tweetutils))

      val future = actorRef ? Start
      val ff = future.mapTo[Future[Done]]
      ff.map { f =>
        val done = Await.result(f, 5 seconds)
        done should ===(Done)
      }
    }
  }

  override protected def afterAll(): Unit = server.stop()
}
