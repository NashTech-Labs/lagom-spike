package com.knoldus.producer.impl.states

import com.knoldus.producer.api.models.Tweet
import play.api.libs.json.Json

/**
  * Created by harmeet on 16/2/17.
  */
case class TweetState (tweet: Tweet, timestamp: String)

object TweetState {
  implicit val format = Json.format[TweetState]
}
