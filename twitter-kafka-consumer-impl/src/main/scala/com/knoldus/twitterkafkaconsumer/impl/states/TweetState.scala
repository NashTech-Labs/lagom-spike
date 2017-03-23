package com.knoldus.twitterkafkaconsumer.impl.states

import com.knoldus.twitterproducer.api.models.Tweet
import play.api.libs.json.Json

/**
  * Created by Knoldus on 19/2/17.
  */
case class TweetState(tweet: Option[Tweet], timeStamp: String)

object TweetState {
  implicit val formatter = Json.format[TweetState]
}
