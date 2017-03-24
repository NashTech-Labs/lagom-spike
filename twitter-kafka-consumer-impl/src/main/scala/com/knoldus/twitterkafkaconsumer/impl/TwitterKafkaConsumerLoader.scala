package com.knoldus.twitterkafkaconsumer.impl

import com.knoldus.twitterkafkaconsumer.api.TwitterConsumerService
import com.knoldus.twitterkafkaconsumer.impl.entities.TweetEntity
import com.knoldus.twitterkafkaconsumer.impl.events.TweetEventProcessor
import com.knoldus.twitterkafkaconsumer.impl.repositories.TwitterRepository
import com.knoldus.twitterproducer.api.TwitterProducerService
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import com.typesafe.conductr.bundlelib.lagom.scaladsl.ConductRApplicationComponents
import play.api.libs.ws.ahc.AhcWSComponents

import scala.collection.immutable.Seq

/**
  * Created by Knoldus on 19/2/17.
  */

class TwitterKafkaConsumerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new TwitterKafkaConsumerApplication(context) with ConductRApplicationComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new TwitterKafkaConsumerApplication(context) with LagomDevModeComponents

  override def describeServices: Seq[Descriptor] = List(
    readDescriptor[TwitterConsumerService]
  )
}

abstract class TwitterKafkaConsumerComponents(context: LagomApplicationContext) extends LagomApplication(context)
  with CassandraPersistenceComponents with AhcWSComponents {

  lazy val twitterService = serviceClient.implement[TwitterProducerService]
  lazy val twitterRepository = wire[TwitterRepository]

  override lazy val lagomServer = LagomServer.forServices(
    bindService[TwitterConsumerService].to(wire[TwitterConsumerServiceImpl])
  )

  override lazy val jsonSerializerRegistry = TwitterSerializerRegistry

  persistentEntityRegistry.register(wire[TweetEntity])

  readSide.register(wire[TweetEventProcessor])

  wire[KafkaTweetTopicListener]
}

abstract class TwitterKafkaConsumerApplication(context: LagomApplicationContext) extends TwitterKafkaConsumerComponents(context)
  with LagomKafkaComponents
