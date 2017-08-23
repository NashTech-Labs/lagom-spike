package sample.helloworld.impl

/**
  * Created by knoldus on 16/2/17.
  */
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import com.typesafe.conductr.bundlelib.lagom.scaladsl.ConductRApplicationComponents
import play.api.libs.ws.ahc.AhcWSComponents
import sample.helloworld.api.HelloService
import scala.concurrent.duration._

class HelloLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new HelloApplication(context) with ConductRApplicationComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new HelloApplication(context) with LagomDevModeComponents

  override def describeServices: List[Descriptor] = List(
    readDescriptor[HelloService]
  )
}

abstract class HelloApplication(context: LagomApplicationContext) extends HelloComponents(context)
    with LagomKafkaComponents
    {

}

abstract class HelloComponents(context: LagomApplicationContext) extends LagomApplication(context)
  with CassandraPersistenceComponents
with AhcWSComponents{
  applicationLifecycle.addStopHook { () =>
    persistentEntityRegistry.gracefulShutdown(15.seconds)
  }


  // Bind the services that this server provides
  override lazy val lagomServer = LagomServer.forServices(
    bindService[HelloService].to(wire[HelloServiceImpl])
  )

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = HelloSerializerRegistry

  // Register the Hello persistent entity
  persistentEntityRegistry.register(wire[HelloEntity])
}
