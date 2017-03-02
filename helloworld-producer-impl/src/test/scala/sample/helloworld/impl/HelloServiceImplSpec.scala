package sample.helloworld.impl

import akka.stream.testkit.scaladsl.TestSink
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ServiceTest, TestTopicComponents}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage

/**
  * Created by knoldus on 23/2/17.
  */
class HelloServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll{



  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)){
    ctx =>
      new HelloComponents(ctx) with LocalServiceLocator
        with TestTopicComponents

  }
  lazy val client = server.serviceClient.implement[HelloService]

  "The Hello Service" should{

    "say hello" in {
      client.hello("Alice").invoke().map{ response =>
        response should === ("Hello, Alice!")
      }
    }


    "publish to kafka" in {

      implicit val system = server.actorSystem
      implicit val mat = server.materializer
      val source = client.greetingsTopic().subscribe.atMostOnceSource
      val greetingMessage = GreetingMessage("msg 1")
      client.useGreeting("123").invoke(greetingMessage)


      source.runWith(TestSink.probe[GreetingMessage])
        .request(1)
        .expectNext should === (GreetingMessage("msg 1"))
    }
  }

  override protected def beforeAll() = server

  override protected def afterAll() = server.stop()
}
