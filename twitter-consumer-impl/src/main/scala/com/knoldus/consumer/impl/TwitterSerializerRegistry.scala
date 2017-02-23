package com.knoldus.consumer.impl

import com.knoldus.consumer.impl.commands.SaveNewTweet
import com.knoldus.consumer.impl.events.TweetSaved
import com.knoldus.consumer.impl.states.TweetState
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}


/**
  * Created by Knoldus on 16/2/17.
  */
object TwitterSerializerRegistry extends JsonSerializerRegistry {

  override val serializers = Vector(
    JsonSerializer[Tweet],
    JsonSerializer[TweetSaved],
    JsonSerializer[SaveNewTweet],
    JsonSerializer[TweetState]
  )
}
