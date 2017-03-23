package com.knoldus.twitterkafkaconsumer.api

import akka.NotUsed
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * Created by harmeet on 23/2/17.
  */
trait TwitterProducerSubscriberService extends Service {

  override def descriptor: Descriptor = {
    import Service._

    named("tweet").withCalls(
      pathCall("/latest-tweet", latestTweet _)
    ).withAutoAcl(true)
  }

  def latestTweet: ServiceCall[NotUsed, Tweet]
}
