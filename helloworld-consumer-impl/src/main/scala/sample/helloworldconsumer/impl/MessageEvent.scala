package sample.helloworldconsumer.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.Json

/**
  * Created by knoldus on 20/2/17.
  */

object MessageEventTag {
  val INSTANCE = AggregateEventTag[MessageEvent]
}

sealed trait MessageEvent extends AggregateEvent[MessageEvent] {
override def aggregateTag = MessageEventTag.INSTANCE
}

case class MessageSaved(msg: String) extends MessageEvent

object MessageSaved {
  implicit val formatter = Json.format[MessageSaved]
}
