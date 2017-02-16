package com.knoldus.producer.impl.events

import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.Json

/**
  * Created by harmeet on 16/2/17.
  */

object TwitterEventTag {
  val INSTANCE = AggregateEventTag[TwitterEvent]
}

sealed trait TwitterEvent extends AggregateEvent[TwitterEvent] {
  override def aggregateTag = TwitterEventTag.INSTANCE
}

case class TweetFetched(tweet: Tweet) extends TwitterEvent
object TweetFetched {
  implicit val fomatter = Json.format[TweetFetched]
}