package com.knoldus.producer.api

import akka.{Done, NotUsed}
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.transport.Method.POST
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * Created by harmeet on 16/2/17.
  */
trait TwitterProducerService extends Service {

  def addNewTweet: ServiceCall[Tweet, Done]

  def twitterTweets: Topic[Tweet]

  override def descriptor = {
    import Service._

    named("tweeter").withCalls(
      restCall(POST, "/api/tweet", addNewTweet _)
    ).withTopics(
      topic(TwitterProducerService.TOPIC_NAME, twitterTweets)
    ).withAutoAcl(true)
  }

}

object TwitterProducerService {
  val TOPIC_NAME = "tweets"
}