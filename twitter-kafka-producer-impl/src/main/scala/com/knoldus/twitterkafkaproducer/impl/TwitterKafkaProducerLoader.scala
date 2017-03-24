package com.knoldus.twitterkafkaproducer.impl

import com.knoldus.twitterkafkaproducer.api.TwitterSchedulerService
import com.knoldus.twitterproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.impl.util.TwitterUtil
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import com.typesafe.conductr.bundlelib.lagom.scaladsl.ConductRApplicationComponents
import play.api.libs.ws.ahc.AhcWSComponents

import scala.collection.immutable.Seq

/**
  * Created by Knoldus on 19/2/17.
  */

class TwitterKafkaProducerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TwitterKafkaProducerApplication(context) with ConductRApplicationComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TwitterKafkaProducerApplication(context) with LagomDevModeComponents

  override def describeServices: Seq[Descriptor] = List(
    readDescriptor[TwitterSchedulerService]
  )
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
