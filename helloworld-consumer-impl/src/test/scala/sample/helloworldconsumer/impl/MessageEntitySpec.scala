package sample.helloworldconsumer.impl

import akka.actor.ActorSystem
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by knoldus on 28/2/17.
  */
class MessageEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll with TypeCheckedTripleEquals {

  val system = ActorSystem("MessageEntitySpec", JsonSerializerRegistry.actorSystemSetupFor(HelloConsumerSerializerRegistry))

  override protected def afterAll(): Unit = {
    Await.ready(system.terminate(), 10 seconds)
  }

  "message entity " must {
    "handle SaveNewMessage command" in {
      val driver = new PersistentEntityTestDriver(system, new MessageEntity, "message-1")
      val outcome = driver.run(SaveNewMessage("Welcome Back !! :)"))
      assert(outcome.events === (List(MessageSaved("Welcome Back !! :)"))))
    }

  }

}
