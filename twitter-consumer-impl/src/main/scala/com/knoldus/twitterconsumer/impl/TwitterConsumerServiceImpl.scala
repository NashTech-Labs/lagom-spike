package com.knoldus.twitterconsumer.impl

import akka.NotUsed
import com.knoldus.twitterconsumer.api.TwitterConsumerService
import com.knoldus.twitterconsumer.impl.repositories.TwitterRepository
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.ExecutionContext


/**
  * Created by Knoldus on 20/2/17.
  */
class TwitterConsumerServiceImpl(twitterRepository: TwitterRepository)(implicit ec: ExecutionContext)
  extends TwitterConsumerService {

  override def findLatestTweets(limit: Int): ServiceCall[NotUsed, Seq[Tweet]] = ServiceCall { _ =>
    twitterRepository.fetchAllLatestTweets(limit)
  }

}
