package com.knoldus.producer.api.models

import play.api.libs.json.Json

/**
  * Created by harmeet on 16/2/17.
  */
case class Tweet(
                  tweetId: Long,
                  createdAt: Long,
                  userId: Long,
                  tweetUserName: String,
                  countryName: String,
                  friendsCount: Long
                )

object Tweet {
  implicit val tweetFormatter = Json.format[Tweet]
}