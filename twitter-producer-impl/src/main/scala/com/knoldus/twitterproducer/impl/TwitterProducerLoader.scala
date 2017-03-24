package com.knoldus.twitterproducer.impl

import com.knoldus.twitterproducer.api.TwitterProducerService
import com.knoldus.twitterproducer.impl.entities.TwitterEntity
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
  * Created by Knoldus on 16/2/17.
  */
class TwitterProducerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TwitterProducerApplication(context) with ConductRApplicationComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TwitterProducerApplication(context) with LagomDevModeComponents

  override def describeServices: Seq[Descriptor] = List(
    readDescriptor[TwitterProducerService]
  )
}


abstract class TwitterProducerComponents(context: LagomApplicationContext) extends LagomApplication(context)
  with CassandraPersistenceComponents with AhcWSComponents {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[TwitterProducerService].to(wire[TwitterProducerServiceImpl])
  )
  override lazy val jsonSerializerRegistry = TwitterSerializerRegistry

  persistentEntityRegistry.register(wire[TwitterEntity])
}

abstract class TwitterProducerApplication(context: LagomApplicationContext) extends TwitterProducerComponents(context)
  with LagomKafkaComponents
