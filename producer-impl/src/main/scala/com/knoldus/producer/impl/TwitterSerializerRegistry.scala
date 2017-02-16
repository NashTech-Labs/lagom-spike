package com.knoldus.producer.impl

import com.knoldus.producer.api.models.Tweet
import com.knoldus.producer.impl.events.TweetFetched
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

import scala.collection.immutable.Seq


/**
  * Created by harmeet on 16/2/17.
  */
object TwitterSerializerRegistry extends JsonSerializerRegistry {

  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[Tweet],
    JsonSerializer[TweetFetched]
  )
}
