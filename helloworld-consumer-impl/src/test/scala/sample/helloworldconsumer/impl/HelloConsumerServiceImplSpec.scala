package sample.helloworldconsumer.impl

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, Matchers}
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage
import sample.helloworldconsumer.api.HelloConsumerService
import sample.helloworldconsumer.impl.repositories.MessageRepository

import scala.concurrent.Future

/**
  * Created by deepti on 21/2/17.
  */

class HelloConsumerServiceImplSpec extends AsyncWordSpec with Matchers with MockitoSugar{
  var producerStub: ProducerStub[GreetingMessage] = _
  val mockedMessageRepository = mock[MessageRepository]

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
        when(mockedMessageRepository.fetchAndCountWordsFromMessages(2)).thenReturn(Future(Map("hi"->1)))
        server.serviceClient.implement[HelloConsumerService].findTopHundredWordCounts().invoke().map { resp =>
          resp should ===(Map("hi"->1))
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



