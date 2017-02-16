package sample.helloworld.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import play.api.libs.json.{Format, Json}

/**
  * Created by knoldus on 16/2/17.
  */
/**
  * This interface defines all the events that the HelloEntity supports.
  */
sealed trait HelloEvent extends AggregateEvent[HelloEvent]
{
  override def aggregateTag: AggregateEventTagger[HelloEvent] = {
     HelloEventTag.instance
  }
}



/**
  * An event that represents a change in greeting message.
  */
case class GreetingMessageChanged(message: String) extends HelloEvent

object GreetingMessageChanged {

  /**
    * Format for the greeting message changed event.
    *
    * Events get stored and loaded from the database, hence a JSON format
    * needs to be declared so that they can be serialized and deserialized.
    */
  implicit val format: Format[GreetingMessageChanged] = Json.format
}
