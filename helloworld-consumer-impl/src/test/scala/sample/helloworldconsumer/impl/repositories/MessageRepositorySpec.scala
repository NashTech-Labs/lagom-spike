
package sample.helloworldconsumer.impl.repositories

import java.sql.Timestamp

import com.lightbend.lagom.scaladsl.persistence.ReadSide
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage
import sample.helloworldconsumer.impl.{HelloConsumerApplication, HelloServiceStub, ReadSideTestDriver}

import scala.concurrent.Await
import scala.concurrent.duration._


class MessageRepositorySpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  case class Message(word: String, insertionTime: Timestamp)

  var producerStub: ProducerStub[GreetingMessage] = _

  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new HelloConsumerApplication(ctx) with LocalServiceLocator {

      val stubFactory = new ProducerStubFactory(actorSystem, materializer)

      producerStub = stubFactory.producer[GreetingMessage](HelloService.TOPIC_NAME)

      override lazy val helloService = new HelloServiceStub(producerStub)

       override lazy val readSide: ReadSide = new ReadSideTestDriver
    }
  }
  val cassandraSession = server.application.cassandraSession

  override protected def beforeAll(): Unit = {

    val f = cassandraSession.executeCreateTable(
      """
        |CREATE TABLE IF NOT EXISTS wordcounttest (
        |words text,
        |insertion_time timestamp,
        |PRIMARY KEY (words,insertion_time)
        |)WITH CLUSTERING ORDER BY (insertion_time DESC)
      """.stripMargin)

    Await.result(f, 10 seconds)
  }


  "message" should {
    "fetch and count words from messages" in {


      val f = cassandraSession.prepare(
        """INSERT into wordcounttest(words ,insertion_time)
          |values(? ,toTimestamp(now()))
          | """.stripMargin)
        .map(statement => {
          statement.bind("hi")

        })

      for {
        statement <- f
        _ <- cassandraSession.executeWrite(statement)
      } yield {
        val message = Message("hi", new Timestamp(2017, 12, 12, 12, 12, 12, 12))

        val fResult = server.application.messageRepository.fetchAndCountWordsFromMessages(1)
        val result = Await.result(fResult, 10 seconds)
        result.contains("hi") should ===(true)
      }
    }
  }
}



