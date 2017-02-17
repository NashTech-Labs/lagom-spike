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
        doSomethingWithTheMessage(msg)
        Done
      }
    )


  var lastObservedMessage: String = _
  private def doSomethingWithTheMessage(greetingMessage: GreetingMessage) = {
    lastObservedMessage = greetingMessage.message
  }

  override def foo: ServiceCall[NotUsed, String] = ServiceCall {
    req => scala.concurrent.Future.successful(lastObservedMessage)
  }
}
