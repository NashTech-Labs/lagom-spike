package com.knoldus.twitterkafkaconsumer.impl

import akka.stream.scaladsl.Flow
import com.knoldus.twitterkafkaconsumer.impl.commands.SaveNewTweet
import com.knoldus.twitterkafkaconsumer.impl.entities.TweetEntity
import com.knoldus.twitterproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import org.slf4j.LoggerFactory

/**
  * Created by Knoldus on 19/2/17.
  */
class KafkaTweetTopicListener(registry: PersistentEntityRegistry,
                              twitterProducerService: TwitterProducerService) {

  val logger = LoggerFactory.getLogger(classOf[KafkaTweetTopicListener])

  private var message: Tweet = _

  twitterProducerService.twitterTweets.subscribe.atLeastOnce(Flow[Tweet].mapAsync(1) {
    case tweet: Tweet =>
      logger.info("observe new tweet {}", tweet)
      message = tweet
      entityRef(tweet.tweetId.toString).ask(SaveNewTweet(tweet))
  })

  private def entityRef(id: String) = registry.refFor[TweetEntity](id)
}
