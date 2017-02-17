package sample.helloworldconsumer.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait HelloConsumerService extends Service {


  def wordCount: ServiceCall[NotUsed, String]

  override def descriptor = {
    import Service._

    named("another-service").withCalls(
      namedCall("/api/wordCount", wordCount)
    ).withAutoAcl(true)

  }

}



