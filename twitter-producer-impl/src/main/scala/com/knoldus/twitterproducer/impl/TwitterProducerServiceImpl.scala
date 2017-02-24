package com.knoldus.twitterproducer.impl

import akka.Done
import com.knoldus.twitterproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.api.models.Tweet
import com.knoldus.twitterproducer.impl.commands.PutTweet
import com.knoldus.twitterproducer.impl.entities.TwitterEntity
import com.knoldus.twitterproducer.impl.events.{TweetEvent, TweetEventTag, TweetSaved}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Created by Knoldus on 16/2/17.
  */
class TwitterProducerServiceImpl(registry: PersistentEntityRegistry) extends TwitterProducerService {

  override def addNewTweet: ServiceCall[Tweet, Done] = ServiceCall { tweet =>
    val ref = registry.refFor[TwitterEntity](tweet.tweetId.toString)
    ref.ask(PutTweet(tweet))
  }

  override def twitterTweets: Topic[Tweet] = TopicProducer.singleStreamWithOffset {
    offset =>
      registry.eventStream(TweetEventTag.INSTANCE, offset)
        .map(event => (convertEvent(event), offset))
  }

  private def convertEvent(twitterEvent: EventStreamElement[TweetEvent]): Tweet = {
    twitterEvent.event match {
      case TweetSaved(tweet) => tweet
    }
  }
}
