package com.knoldus.twitterproducer.impl.commands

import akka.Done
import com.knoldus.twitterproducer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.Json

/**
  * Created by Knoldus on 16/2/17.
  */
trait TweetCommand[T] extends ReplyType[T]

case class PutTweet(tweet: Tweet) extends TweetCommand[Done]

object PutTweet {
  implicit val formatter = Json.format[PutTweet]
}
