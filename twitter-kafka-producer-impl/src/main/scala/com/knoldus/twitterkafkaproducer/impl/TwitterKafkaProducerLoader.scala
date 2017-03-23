package com.knoldus.twitterkafkaproducer.impl

import com.knoldus.twitterkafkaproducer.api.TwitterSchedulerService
import com.knoldus.twitterproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.impl.util.TwitterUtil
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

/**
  * Created by Knoldus on 19/2/17.
  */

class TwitterKafkaProducerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TwitterKafkaProducerApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TwitterKafkaProducerApplication(context) with LagomDevModeComponents
}

abstract class TwitterKafkaProducerComponents(context: LagomApplicationContext) extends LagomApplication(context)
  with CassandraPersistenceComponents with AhcWSComponents {

  lazy val twitterService = serviceClient.implement[TwitterProducerService]
  lazy val twitterUtil = wire[TwitterUtil]

  override lazy val lagomServer = LagomServer.forServices(
    bindService[TwitterSchedulerService].to(wire[TwitterSchedulerServiceImpl])
  )

  override lazy val jsonSerializerRegistry = new JsonSerializerRegistry {
    override def serializers = Vector.empty
  }
}

abstract class TwitterKafkaProducerApplication(context: LagomApplicationContext) extends TwitterKafkaProducerComponents(context)
  with LagomKafkaComponents
