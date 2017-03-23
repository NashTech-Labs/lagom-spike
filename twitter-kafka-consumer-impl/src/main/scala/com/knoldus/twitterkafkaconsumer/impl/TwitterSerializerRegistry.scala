package com.knoldus.twitterkafkaconsumer.impl

import com.knoldus.twitterkafkaconsumer.impl.commands.SaveNewTweet
import com.knoldus.twitterkafkaconsumer.impl.events.TweetSaved
import com.knoldus.twitterkafkaconsumer.impl.states.TweetState
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
