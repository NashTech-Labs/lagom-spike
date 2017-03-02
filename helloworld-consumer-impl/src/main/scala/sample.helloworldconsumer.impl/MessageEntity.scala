package sample.helloworldconsumer.impl
import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by knoldus on 20/2/17.
  */
class MessageEntity extends PersistentEntity {

  override type Command = MessageCommand[_]
  override type Event = MessageEvent
  override type State = MessageState
  val logger: Logger = LoggerFactory.getLogger(this.getClass())

  override def initialState: MessageState = MessageState("", LocalDateTime.now().toString)

  override def behavior: Behavior = {
    case MessageState(msg, _) => Actions().onCommand[SaveNewMessage, Done]{
      case (SaveNewMessage(msg), ctx, state) =>
        logger.info(s"observe new message from kafka and save ${msg}")

        ctx.thenPersist( MessageSaved(msg)) { msgSaved: MessageSaved =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (MessageSaved(message), state) =>
        logger.info("Message Saved event fire ...")

        MessageState(message, LocalDateTime.now().toString)
    }
  }
}
