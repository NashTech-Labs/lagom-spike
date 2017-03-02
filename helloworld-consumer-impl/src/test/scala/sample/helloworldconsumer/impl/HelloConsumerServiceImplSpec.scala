package sample.helloworldconsumer.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage
import sample.helloworldconsumer.api.HelloConsumerService
import sample.helloworldconsumer.impl.repositories.MessageRepository

import scala.concurrent.Future

/**
  * Created by knoldus on 21/2/17.
  */

class HelloConsumerServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with MockitoSugar {


  var producerStub: ProducerStub[GreetingMessage] = _


  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new HelloConsumerApplication(ctx) with LocalServiceLocator {

      val stubFactory = new ProducerStubFactory(actorSystem, materializer)
      producerStub = stubFactory.producer[GreetingMessage](HelloService.TOPIC_NAME)
      override lazy val helloService = new HelloServiceStub(producerStub)
      override lazy val messageRepository = mock[MessageRepository]
      when(messageRepository.fetchAndCountWordsFromMessages(100))
        .thenReturn(Future.successful(Map("hi" -> 2)))
    }
  }

  lazy val client = server.serviceClient.implement[HelloConsumerService]

  "The Hello consumer Service" should {
    "publish updates on greetings message" in {

      producerStub.send(GreetingMessage("added to kafka!"))
      client.foo.invoke().map { resp =>
        resp should ===("added to kafka!")
      }
    }

    "find top hundred word counts" in {

      client.findTopHundredWordCounts.invoke().map { response =>
        response should ===(Map("hi" -> 2))

      }
    }
  }

  override protected def beforeAll() = server

  override protected def afterAll() = server.stop()
}