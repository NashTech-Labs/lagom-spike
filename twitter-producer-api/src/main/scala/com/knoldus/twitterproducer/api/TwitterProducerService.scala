package com.knoldus.twitterproducer.api

import akka.Done
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.transport.Method.POST
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * Created by Knoldus on 16/2/17.
  */
trait TwitterProducerService extends Service {

  def addNewTweet: ServiceCall[Tweet, Done]

  def twitterTweets: Topic[Tweet]

  override def descriptor: Descriptor = {
    import Service._

    named("twitter").withCalls(
      restCall(POST, "/api/tweet", addNewTweet _)
    ).withTopics(
      topic(TwitterProducerService.TOPIC_NAME, twitterTweets)
    ).withAutoAcl(true)
  }

}

object TwitterProducerService {
  val TOPIC_NAME = "tweets"
}