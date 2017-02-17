package sample.helloworldconsumer.impl

import akka.stream.scaladsl.Flow
import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage
import sample.helloworldconsumer.api.HelloConsumerService


class HelloConsumerServiceImpl (helloService: HelloService) extends HelloConsumerService {


  helloService.greetingsTopic
    .subscribe
    .atLeastOnce(
      Flow[GreetingMessage].map{ msg =>
        putGreetingMessage(msg)
        Done
      }
    )


  var lastObservedMessage: String = _
  private def putGreetingMessage(greetingMessage: GreetingMessage) = {
    //TODO write function to insert data in cassandra and do word count on it
    lastObservedMessage = greetingMessage.message
  }

  override def wordCount: ServiceCall[NotUsed, String] = ServiceCall {
        //TODO write function to get data from cassandra
    req => scala.concurrent.Future.successful(lastObservedMessage)
  }
}
