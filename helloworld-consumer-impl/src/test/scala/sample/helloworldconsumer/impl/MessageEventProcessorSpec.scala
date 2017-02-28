
package sample.helloworldconsumer.impl

import java.sql.Timestamp
import java.util.concurrent.atomic.AtomicInteger

import akka.persistence.query.Sequence
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import sample.helloworldconsumer.impl.repositories.MessageRepository

import scala.concurrent.Future

/**
  * Created by knoldus on 24/2/17.
  */

case class Message(word: String, insertionTime: Timestamp)

class MessageEventProcessorSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with MockitoSugar {

  var producerStub: ProducerStub[GreetingMessage] = _

  private lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new HelloConsumerApplication(ctx) with LocalServiceLocator {

      val stubFactory = new ProducerStubFactory(actorSystem, materializer)
      producerStub = stubFactory.producer[GreetingMessage](HelloService.TOPIC_NAME)
      override lazy val helloService = new HelloServiceStub(producerStub)
      override lazy val readSide: ReadSideTestDriver = new ReadSideTestDriver
      override lazy val messageRepository = mock[MessageRepository]
      when(messageRepository.fetchAndCountWordsFromMessages(100))
        .thenReturn(Future.successful(Map("hi"->2)))

    }
  }

  private val testDriver = server.application.readSide
  private val repository = server.application.messageRepository
  private val offset = new AtomicInteger()

  override protected def beforeAll(): Unit = server

  override protected def afterAll(): Unit = server.stop()


  "Message Event Processor " should {

    "insert a word" in {

      val message = Message("hi", new Timestamp(2017, 12, 12, 12, 12, 12, 12))

      for {
        _ <- feed("123L", MessageSaved("hi"))
        item <- repository.fetchAndCountWordsFromMessages(100)
      } yield item.contains("hi") should ===(true)
    }
  }

  private def feed(messageId: String, event: MessageEvent) = {
    testDriver.feed(messageId, event, Sequence(offset.getAndIncrement()))
  }
}
