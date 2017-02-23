package com.knoldus.consumer.impl

import akka.{Done, NotUsed}
import akka.stream.scaladsl.Flow
import com.knoldus.consumer.api.TwitterProducerSubscriberService
import com.knoldus.consumer.impl.commands.SaveNewTweet
import com.knoldus.consumer.impl.entities.TweetEntity
import com.knoldus.producer.api.TwitterProducerService
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import org.slf4j.LoggerFactory

import scala.concurrent.Future

/**
  * Created by Knoldus on 19/2/17.
  */
class TwitterProducerSubscriberServiceImpl(registry: PersistentEntityRegistry,
                                           twitterProducerService: TwitterProducerService) extends TwitterProducerSubscriberService {

  val logger = LoggerFactory.getLogger(classOf[TwitterProducerSubscriberServiceImpl])

  private var message: Tweet = _

  twitterProducerService.twitterTweets.subscribe.atLeastOnce(Flow[Tweet].mapAsync(1) {
    case tweet: Tweet =>
      logger.info("observe new tweet {}", tweet)
      message = tweet
      entityRef(tweet.tweetId.toString).ask(SaveNewTweet(tweet))
    case _ =>
      logger.info("unknown message")
      Future.successful(Done)
  })

  private def entityRef(id: String) = registry.refFor[TweetEntity](id)

  override def latestTweet: ServiceCall[NotUsed, Tweet] = ServiceCall { _ =>
    Future.successful {
      message
    }
  }
}
