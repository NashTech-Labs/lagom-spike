package com.knoldus.producer.impl

import com.knoldus.producer.api.{TwitterProducerService, TwitterSchedulerService}
import com.knoldus.producer.impl.entities.TwitterEntity
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

/**
  * Created by harmeet on 16/2/17.
  */
class TwitterLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TwitterApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TwitterApplication(context) with LagomDevModeComponents
}


abstract class TwitterApplication(context: LagomApplicationContext) extends LagomApplication(context)
  with CassandraPersistenceComponents with AhcWSComponents with LagomKafkaComponents {

  lazy val twitterService = serviceClient.implement[TwitterProducerService]

  override lazy val lagomServer = LagomServer.forServices(
    bindService[TwitterProducerService].to(wire[TwitterProducerServiceImpl]),
    bindService[TwitterSchedulerService].to(wire[TwitterSchedulerServiceImpl])
  )
  override lazy val jsonSerializerRegistry = TwitterSerializerRegistry

  persistentEntityRegistry.register(wire[TwitterEntity])
}