package sample.helloworldconsumer.impl
import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  * Created by knoldus on 20/2/17.
  */
class MessageEntity extends PersistentEntity {

  override type Command = MessageCommand[_]
  override type Event = MessageEvent
  override type State = MessageState

  override def initialState = MessageState("", LocalDateTime.now().toString)

  override def behavior: Behavior = {
    case MessageState(msg, _) => Actions().onCommand[SaveNewMessage, Done]{
      case (SaveNewMessage(msg), ctx, state) =>
        println(s"observe new message from kafka and save ${msg}")

        ctx.thenPersist( MessageSaved(msg)) { msgSaved: MessageSaved =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (MessageSaved(message), state) =>
        println(s"MessgaeSaved event fire ...")

        MessageState(message, LocalDateTime.now().toString)
    }
  }
}
