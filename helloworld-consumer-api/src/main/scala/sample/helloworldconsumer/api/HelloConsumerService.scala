package sample.helloworldconsumer.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait HelloConsumerService extends Service {


  def foo: ServiceCall[NotUsed, String]

  override def descriptor = {
    import Service._

    named("another-service").withCalls(
      namedCall("/api/foo", foo)
    ).withAutoAcl(true)

  }

}



