package com.knoldus.consumer.impl.events

import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.Json

/**
  * Created by harmeet on 19/2/17.
  */

object TweetEventTag {
  val INSTANCE = AggregateEventTag[TweetEvent]
}

sealed trait TweetEvent extends AggregateEvent[TweetEvent] {
  override def aggregateTag = TweetEventTag.INSTANCE
}

case class TweetSaved(tweet: Tweet) extends TweetEvent

object TweetSaved {
  implicit val formatter = Json.format[TweetSaved]
}