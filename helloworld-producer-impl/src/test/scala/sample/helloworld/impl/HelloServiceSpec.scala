package sample.helloworld.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.helloworld.api.HelloService

/**
  * Created by deepti on 20/2/17.
  */
class HelloServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  "The Hello Service" should {

    "say hello " in ServiceTest.withServer(ServiceTest.defaultSetup){ ctx =>
      new HelloApplication(ctx) with LocalServiceLocator
    }  {  server =>
      val client = server.serviceClient.implement[HelloService]
      client.hello("Alice").invoke().map{ response =>
        response should === ("Hello Alice")
      }
    }
  }
}