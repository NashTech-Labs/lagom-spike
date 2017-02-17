package com.knoldus.producer.impl

import com.knoldus.producer.api.models.Tweet
import com.knoldus.producer.impl.commands.PutTweet
import com.knoldus.producer.impl.events.TweetSaved
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}


/**
  * Created by harmeet on 16/2/17.
  */
object TwitterSerializerRegistry extends JsonSerializerRegistry {

  override val serializers = Vector(
    JsonSerializer[Tweet],
    JsonSerializer[TweetSaved],
    JsonSerializer[PutTweet]
  )
}
