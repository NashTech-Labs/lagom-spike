package com.knoldus.consumer.impl

import akka.Done
import akka.stream.scaladsl.Flow
import com.knoldus.consumer.impl.commands.SaveNewTweet
import com.knoldus.consumer.impl.entities.TweetEntity
import com.knoldus.producer.api.TwitterProducerService
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.Future

/**
  * Created by harmeet on 19/2/17.
  */
class TwitterProducerSubscriber(registery: PersistentEntityRegistry,
                                twitterProducerService: TwitterProducerService) {

  twitterProducerService.twitterTweets.subscribe.atLeastOnce(Flow[Tweet].mapAsync(1) {
    case tweet: Tweet =>
      println(s"obersrve new tweet ${tweet}")
      entityRef(tweet.tweetId.toString).ask(SaveNewTweet(tweet))
    case _ =>
      println(s"unknown message")
      Future.successful(Done)
  })

  private def entityRef(id: String) = registery.refFor[TweetEntity](id)
}
