package com.knoldus.producer.api

import akka.{Done, NotUsed}
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.transport.Method.POST
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * Created by harmeet on 16/2/17.
  */
trait TwitterService extends Service {

  def fetchTweetFromTwitter: ServiceCall[Tweet, Done]

  def twitterTweets: Topic[Tweet]

  override def descriptor = {
    import Service._

    named("tweeter").withCalls(
      restCall(POST, "/api/tweet", fetchTweetFromTwitter _)
    ).withTopics(
      topic(TwitterService.TOPIC_NAME, twitterTweets)
    ).withAutoAcl(true)
  }

}

object TwitterService {
  val TOPIC_NAME = "tweets"
}