package com.knoldus.consumer.impl

import akka.Done
import akka.stream.scaladsl.Flow
import com.knoldus.consumer.impl.commands.SaveNewTweet
import com.knoldus.consumer.impl.entities.TweetEntity
import com.knoldus.producer.api.TwitterProducerService
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import org.slf4j.LoggerFactory

import scala.concurrent.Future

/**
  * Created by Knoldus on 19/2/17.
  */
class TwitterProducerSubscriber(registry: PersistentEntityRegistry,
                                twitterProducerService: TwitterProducerService) {

  val logger = LoggerFactory.getLogger(classOf[TwitterProducerSubscriber])

  twitterProducerService.twitterTweets.subscribe.atLeastOnce(Flow[Tweet].mapAsync(1) {
    case tweet: Tweet =>
      logger.info("observe new tweet {}", tweet)
      entityRef(tweet.tweetId.toString).ask(SaveNewTweet(tweet))
    case _ =>
      logger.info("unknown message")
      Future.successful(Done)
  })

  private def entityRef(id: String) = registry.refFor[TweetEntity](id)
}
