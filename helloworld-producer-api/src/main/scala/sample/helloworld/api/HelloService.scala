package sample.helloworld.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import sample.helloworld.api.model.GreetingMessage

/**
  * Created by knoldus on 16/2/17.
  */

/**
  * The Hello service trait.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the HelloService.
  */

trait HelloService extends Service{

  /**
    * Example: curl http://localhost:9000/api/hello/Alice
    */
  def hello(id: String): ServiceCall[NotUsed, String]

  /**
    * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
    * "Hi"}' http://localhost:9000/api/hello/Alice
    */
  def useGreeting(id: String): ServiceCall[GreetingMessage, Done]

  override final def descriptor: Descriptor = {
    import Service._
    // @formatter:off
    named("hello").withCalls(
      pathCall("/api/hello/:id", hello _),
      pathCall("/api/hello/:id", useGreeting _)
    ).withTopics(
          topic(HelloService.TOPIC_NAME, greetingsTopic)
     ).withAutoAcl(true)
    // @formatter:on
  }

  // The topic handle
  def greetingsTopic(): Topic[GreetingMessage]
}

object HelloService  {
  val TOPIC_NAME = "wordCount"
}

