package sample.helloworldconsumer.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents
import sample.helloworld.api.HelloService
import sample.helloworld.impl.HelloSerializerRegistry
import sample.helloworldconsumer.api.HelloConsumerService
import sample.helloworldconsumer.api.models.WordCount



class HelloConsumerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new HelloConsumerApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new HelloConsumerApplication(context) with LagomDevModeComponents
}

abstract class HelloConsumerApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[HelloConsumerService].to(wire[HelloConsumerServiceImpl])
  )

  //Bind the HelloService client
  lazy val helloService = serviceClient.implement[HelloService]

 /* object HelloConsumerSerializerRegistry extends JsonSerializerRegistry {
    override def serializers: Seq[JsonSerializer[_]] = Seq(
      JsonSerializer[WordCount]
    )
  }*/

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = HelloSerializerRegistry

  // Register the Hello persistent entity
  //persistentEntityRegistry.register(wire[HelloEntity])
}

