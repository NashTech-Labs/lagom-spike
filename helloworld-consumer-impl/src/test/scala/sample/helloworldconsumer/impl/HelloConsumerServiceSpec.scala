package sample.helloworldconsumer.impl

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.scalatest.{AsyncWordSpec, Matchers}
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage
import sample.helloworld.impl.{Hello, HelloEntity}
import sample.helloworldconsumer.api.HelloConsumerService

/**
  * Created by deepti on 21/2/17.
  */

class HelloConsumerServiceSpec extends AsyncWordSpec with Matchers {
  var producerStub: ProducerStub[GreetingMessage] = _

  "The Hello Consumer Service" should {
    "publish updates on greetings message" in
      ServiceTest.withServer(ServiceTest.defaultSetup) { ctx =>
        new HelloConsumerApplication(ctx) with LocalServiceLocator {


          val stubFactory = new ProducerStubFactory(actorSystem, materializer)
          producerStub = stubFactory.producer[GreetingMessage](HelloService.TOPIC_NAME)


          override lazy val helloService = new HelloServiceStub(producerStub)
        }
      } { server =>

        producerStub.send(GreetingMessage("added to kafka!!"))

        server.serviceClient.implement[HelloConsumerService].wordCount.invoke().map { resp =>
          resp should ===("added to kafka!!")
        }

      }
  }
}

class HelloServiceStub(stub: ProducerStub[GreetingMessage])
  extends HelloService {

  override def greetingsTopic(): Topic[GreetingMessage] = stub.topic

  override def hello(id: String): ServiceCall[NotUsed, String] = ???

  override def useGreeting(id: String): ServiceCall[GreetingMessage, Done] = ???
}



