package com.knoldus.producer.impl

import akka.{Done, NotUsed}
import akka.event.EventStream
import com.knoldus.producer.api.TwitterService
import com.knoldus.producer.api.models.Tweet
import com.knoldus.producer.impl.events.{TweetFetched, TwitterEvent, TwitterEventTag}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

import scala.concurrent.Future

/**
  * Created by harmeet on 16/2/17.
  */
class TwitterServiceImpl(registry: PersistentEntityRegistry) extends TwitterService {

  override def fetchTweetFromTwitter: ServiceCall[Tweet, Done] = ServiceCall { tweet =>
    Future.successful { Done }
  }

  override def twitterTweets: Topic[Tweet] = TopicProducer.singleStreamWithOffset {
    offset => registry.eventStream(TwitterEventTag.INSTANCE, offset)
        .map(event => (convertEvent(event), offset))
  }

  private def convertEvent(twitterEvent: EventStreamElement[TwitterEvent]): Tweet = {
    twitterEvent.event match {
      case TweetFetched(tweet) => tweet
    }
  }
}
