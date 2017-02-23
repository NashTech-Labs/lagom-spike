package com.knoldus.consumer.impl

import com.knoldus.consumer.api.TwitterConsumerService
import com.knoldus.consumer.impl.entities.TweetEntity
import com.knoldus.consumer.impl.events.TweetEventProcessor
import com.knoldus.consumer.impl.repositories.TwitterRepository
import com.knoldus.producer.api.TwitterProducerService
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

/**
  * Created by Knoldus on 19/2/17.
  */

class TwitterConsumerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TwitterConsumerApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TwitterConsumerApplication(context) with LagomDevModeComponents
}

abstract class TwitterConsumerApplication(context: LagomApplicationContext) extends LagomApplication(context)
  with CassandraPersistenceComponents with AhcWSComponents with LagomKafkaComponents {

  lazy val twitterService = serviceClient.implement[TwitterProducerService]
  lazy val twitterRepository = wire[TwitterRepository]

  override lazy val lagomServer = LagomServer.forServices(
    bindService[TwitterConsumerService].to(wire[TwitterConsumerServiceImpl])
  )
  override lazy val jsonSerializerRegistry = TwitterSerializerRegistry

  persistentEntityRegistry.register(wire[TweetEntity])
  wire[TwitterProducerSubscriber]
  readSide.register(wire[TweetEventProcessor])
}