package com.knoldus.twitterconsumer.impl

import akka.Done
import com.knoldus.twitterkafkaproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.testkit.ProducerStub

/**
  * Created by harmeet on 24/2/17.
  */

class TwitterServiceStub(stub: ProducerStub[Tweet]) extends TwitterProducerService {

  override def addNewTweet: ServiceCall[Tweet, Done] = ???

  override def twitterTweets: Topic[Tweet] = stub.topic
}