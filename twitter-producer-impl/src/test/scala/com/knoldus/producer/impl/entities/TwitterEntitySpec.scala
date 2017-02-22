package com.knoldus.producer.impl.entities

import akka.Done
import akka.actor.ActorSystem
import com.knoldus.producer.api.models.Tweet
import com.knoldus.producer.impl.TwitterSerializerRegistry
import com.knoldus.producer.impl.commands.PutTweet
import com.knoldus.producer.impl.events.TweetSaved
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalactic.{ConversionCheckedTripleEquals, TypeCheckedTripleEquals}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by harmeet on 21/2/17.
  */
class TwitterEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll with TypeCheckedTripleEquals {

  val system = ActorSystem("TwitterEntitySpec", JsonSerializerRegistry.actorSystemSetupFor(TwitterSerializerRegistry))

  override protected def afterAll(): Unit = {
    Await.ready(system.terminate(), 10 seconds)
  }

  "twitter entity " must {
    "handle PutTweet command" in {
      val driver = new PersistentEntityTestDriver(system, new TwitterEntity, "tweet-1")
      val tweet = Tweet(833556819314409473l, 1487570407000l, 206645598, "javinpaul", "12 Advanced Java Programming " +
        "Books for Experienced Programmer", 7880)
      val outcome = driver.run(PutTweet(tweet))
      outcome.events should === (List(TweetSaved(tweet)))
      outcome.state.tweet should === (Option(tweet))
      outcome.replies should === (List(Done))
      outcome.issues should be (Nil)
    }
  }

}
