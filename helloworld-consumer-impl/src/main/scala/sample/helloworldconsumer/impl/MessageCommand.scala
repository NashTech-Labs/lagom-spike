package sample.helloworldconsumer.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.Json
import akka.Done

/**
  * Created by knoldus on 20/2/17.
  */
sealed trait MessageCommand [T] extends ReplyType[T]

case class SaveNewMessage(message: String) extends MessageCommand[Done]

object SaveNewMessage {
  implicit val formatter = Json.format[SaveNewMessage]
}