package com.knoldus.twitterconsumer.impl

import com.knoldus.twitterconsumer.impl.commands.SaveNewTweet
import com.knoldus.twitterconsumer.impl.events.TweetSaved
import com.knoldus.twitterconsumer.impl.states.TweetState
import com.knoldus.twitterproducer.api.models.Tweet
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
