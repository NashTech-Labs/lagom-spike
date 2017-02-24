package com.knoldus.twitterconsumer.impl

import akka.stream.scaladsl.Flow
import akka.{Done, NotUsed}
import com.knoldus.twitterconsumer.api.TwitterProducerSubscriberService
import com.knoldus.twitterconsumer.impl.commands.SaveNewTweet
import com.knoldus.twitterconsumer.impl.entities.TweetEntity
import com.knoldus.twitterproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.api.models.Tweet
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
  })

  private def entityRef(id: String) = registry.refFor[TweetEntity](id)

  override def latestTweet: ServiceCall[NotUsed, Tweet] = ServiceCall { _ =>
    Future.successful {
      message
    }
  }
}
