package sample.helloworldconsumer.impl

import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire._
import com.typesafe.conductr.bundlelib.lagom.scaladsl.ConductRApplicationComponents
import play.api.libs.ws.ahc.AhcWSComponents
import sample.helloworld.api.HelloService
import sample.helloworldconsumer.api.HelloConsumerService
import sample.helloworldconsumer.impl.repositories.MessageRepository
import scala.concurrent.duration._



class HelloConsumerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new HelloConsumerApplication(context) with ConductRApplicationComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new HelloConsumerApplication(context) with LagomDevModeComponents

  override def describeServices: List[Descriptor] = List(
       readDescriptor[HelloConsumerService]
  )
}

abstract class HelloConsumerApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {
  applicationLifecycle.addStopHook { () =>
    persistentEntityRegistry.gracefulShutdown(15.seconds)
  }

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[HelloConsumerService].to(wire[HelloConsumerServiceImpl])
  )

  //Bind the HelloService client
  lazy val helloService = serviceClient.implement[HelloService]

  lazy val messageRepository = wire[MessageRepository]
  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = HelloConsumerSerializerRegistry

  // Register the Message persistent entity
  //persistentEntityRegistry.register(wire[HelloEntity])

  persistentEntityRegistry.register(wire[MessageEntity])
  //wire[HelloConsumerServiceImpl]
  readSide.register(wire[MessageEventProcessor])
}

