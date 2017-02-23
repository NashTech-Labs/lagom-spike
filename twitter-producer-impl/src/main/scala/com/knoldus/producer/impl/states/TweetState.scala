package com.knoldus.producer.impl.states

import com.knoldus.producer.api.models.Tweet
import play.api.libs.json.Json

/**
  * Created by Knoldus on 16/2/17.
  */
case class TweetState (tweet: Option[Tweet], timestamp: String)

object TweetState {
  implicit val format = Json.format[TweetState]
}
