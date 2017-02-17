package com.knoldus.producer.impl

import akka.Done
import com.knoldus.producer.api.TwitterProducerService
import com.knoldus.producer.api.models.Tweet
import com.knoldus.producer.impl.commands.PutTweet
import com.knoldus.producer.impl.entities.TwitterEntity
import com.knoldus.producer.impl.events.{TweetEvent, TweetEventTag, TweetSaved}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Created by harmeet on 16/2/17.
  */
class TwitterProducerServiceImpl(registry: PersistentEntityRegistry) extends TwitterProducerService {

  override def addNewTweet: ServiceCall[Tweet, Done] = ServiceCall { tweet =>
    val ref = registry.refFor[TwitterEntity]("1")
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
