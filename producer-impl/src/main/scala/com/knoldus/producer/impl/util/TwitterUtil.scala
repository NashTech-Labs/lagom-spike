package com.knoldus.producer.impl.util

import com.knoldus.producer.api.models.Tweet
import com.typesafe.config.ConfigFactory
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

import scala.collection.JavaConverters._

/**
  * Created by harmeet on 16/2/17.
  */
object TwitterUtil {

  val config = ConfigFactory.load();

  def fetchTweets = {
    val cb = new ConfigurationBuilder()
      .setOAuthConsumerKey(config.getString("twitter.consumerKey"))
      .setOAuthConsumerSecret(config.getString("twitter.consumerSecret"))
      .setOAuthAccessToken(config.getString("twitter.accessToken"))
      .setOAuthAccessTokenSecret(config.getString("twitter.accessTokenSecret"))

    val tf = new TwitterFactory(cb.build())
    val twitter = tf.getInstance()
    val status = twitter.getHomeTimeline().get(0)

    Tweet(status.getId, status.getCreatedAt.getTime, status.getUser.getId, status.getUser.getName,
      status.getPlace.getCountry, status.getUser.getFriendsCount)
  }
}
