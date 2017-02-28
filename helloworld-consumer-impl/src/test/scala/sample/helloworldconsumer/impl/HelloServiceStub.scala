package sample.helloworldconsumer.impl

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.testkit.ProducerStub
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage

/**
  * Created by knoldus on 27/2/17.
  */
class HelloServiceStub(stub: ProducerStub[GreetingMessage])
  extends HelloService {

  override def greetingsTopic(): Topic[GreetingMessage] = stub.topic

  override def hello(id: String): ServiceCall[NotUsed, String] = ???

  override def useGreeting(id: String): ServiceCall[GreetingMessage, Done] = ???
}