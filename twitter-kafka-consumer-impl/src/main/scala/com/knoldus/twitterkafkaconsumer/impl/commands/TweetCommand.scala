package com.knoldus.twitterkafkaconsumer.impl.commands

import akka.Done
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.Json

/**
  * Created by Knoldus on 19/2/17.
  */
sealed trait TweetCommand[T] extends ReplyType[T]

case class SaveNewTweet(tweet: Tweet) extends TweetCommand[Done]

object SaveNewTweet {
  implicit val formatter = Json.format[SaveNewTweet]
}
