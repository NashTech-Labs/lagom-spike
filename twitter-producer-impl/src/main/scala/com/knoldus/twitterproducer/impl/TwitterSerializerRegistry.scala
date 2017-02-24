package com.knoldus.twitterproducer.impl

import com.knoldus.twitterproducer.api.models.Tweet
import com.knoldus.twitterproducer.impl.commands.PutTweet
import com.knoldus.twitterproducer.impl.events.TweetSaved
import com.knoldus.twitterproducer.impl.states.TweetState
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}


/**
  * Created by Knoldus on 16/2/17.
  */
object TwitterSerializerRegistry extends JsonSerializerRegistry {

  override val serializers = Vector(
    JsonSerializer[Tweet],
    JsonSerializer[TweetSaved],
    JsonSerializer[PutTweet],
    JsonSerializer[TweetState]
  )
}
