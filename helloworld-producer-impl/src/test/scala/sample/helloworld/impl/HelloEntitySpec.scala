package sample.helloworld.impl

import akka.Done
import akka.actor.ActorSystem
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalactic.ConversionCheckedTripleEquals
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._


class HelloEntitySpec extends WordSpecLike with Matchers with BeforeAndAfterAll
  with ConversionCheckedTripleEquals {
  val system = ActorSystem("HelloEntitySpec", JsonSerializerRegistry.actorSystemSetupFor(HelloSerializerRegistry))

  override def afterAll(): Unit = {
    Await.ready(system.terminate, 10 seconds)
  }

  "Hello Entity " must {

    "handle UseGreeting Message and fire an event" in {
      val driver = new PersistentEntityTestDriver(system, new HelloEntity, "Hello Entity-1")
      val newMessage = "Welcome Back!!"
      val outcome = driver.run(UseGreetingMessage(newMessage))
      assert(outcome.events.toList === List(GreetingMessageChanged(newMessage)))
      assert(outcome.replies.toList === List(Done))
      assert(outcome.issues === Nil)
    }

    "handle hello Message and return a reply" in {
      val driver = new PersistentEntityTestDriver(system, new HelloEntity, "Hello Entity-2")
      val id = "Alice"
      val outcome = driver.run(Hello(id,None))


      assert(outcome.events.toList === List())
      assert(outcome.replies.toList === List("Hello, Alice!"))
      assert(outcome.issues === Nil)
    }
  }
}
