package com.knoldus.consumer.api

import akka.NotUsed
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * Created by harmeet on 20/2/17.
  */
trait TwitterConsumerService extends Service {

  override def descriptor = {
    import Service._

    named("tweets").withCalls(
      restCall(Method.GET, "/api/latest-tweets?limit", findLatestTweets _)
    ).withAutoAcl(true)
  }

  def findLatestTweets(limit: Int): ServiceCall[NotUsed, Seq[Tweet]]
}
