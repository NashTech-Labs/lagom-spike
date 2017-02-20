package com.knoldus.consumer.impl

import akka.NotUsed
import com.knoldus.consumer.api.TwitterConsumerService
import com.knoldus.consumer.impl.repositories.TwitterRepository
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.ExecutionContext


/**
  * Created by harmeet on 20/2/17.
  */
class TwitterConsumerServiceImpl(twitterRepository: TwitterRepository)(implicit ec: ExecutionContext)
  extends TwitterConsumerService {

  def findLatestTweets(limit: Int): ServiceCall[NotUsed, Seq[Tweet]] = ServiceCall { _ =>
    twitterRepository.fetchAllLatestTweets(limit)
  }

}
