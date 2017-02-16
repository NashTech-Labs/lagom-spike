package sample.helloworld.impl

import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  * Created by knoldus on 16/2/17.
  */
class HelloEntity extends PersistentEntity {

  override type Command = HelloCommand[_]
  override type Event = HelloEvent
  override type State = HelloState

  /**
    * The initial state. This is used if there is no snapshotted state to be found.
    */
  override def initialState: HelloState = HelloState("Hello", LocalDateTime.now.toString)

  /**
    * An entity can define different behaviours for different states, so the behaviour
    * is a function of the current state to a set of actions.
    */
  override def behavior: Behavior = {
    case HelloState(message, _) => Actions().onCommand[UseGreetingMessage, Done] {

      // Command handler for the UseGreetingMessage command
      case (UseGreetingMessage(newMessage), ctx, state) =>
        // In response to this command, we want to first persist it as a
        // GreetingMessageChanged event
        ctx.thenPersist(
          GreetingMessageChanged(newMessage)
        ) { _ =>
          // Then once the event is successfully persisted, we respond with done.
          ctx.reply(Done)
        }

    }.onReadOnlyCommand[Hello, String] {

      // Command handler for the Hello command
      case (Hello(name, organization), ctx, state) =>
        // Reply with a message built from the current message, and the name of
        // the person we're meant to say hello to.
        ctx.reply(s"$message, $name!")

    }.onEvent {

      // Event handler for the GreetingMessageChanged event
      case (GreetingMessageChanged(newMessage), state) =>
        // We simply update the current state to use the greeting message from
        // the event.
        HelloState(newMessage, LocalDateTime.now().toString)

    }
  }
}
