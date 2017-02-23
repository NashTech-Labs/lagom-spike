package com.knoldus.twitterproducer.impl.util

import com.knoldus.twitterproducer.api.models.Tweet
import com.typesafe.config.ConfigFactory
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

import scala.collection.JavaConverters._

/**
  * Created by Knoldus on 16/2/17.
  */

class TwitterUtil {

  val config = ConfigFactory.load();

  def fetchTweets: List[Tweet] = {
    val cb = new ConfigurationBuilder()
      .setOAuthConsumerKey(config.getString("twitter.consumerKey"))
      .setOAuthConsumerSecret(config.getString("twitter.consumerSecret"))
      .setOAuthAccessToken(config.getString("twitter.accessToken"))
      .setOAuthAccessTokenSecret(config.getString("twitter.accessTokenSecret"))

    val tf = new TwitterFactory(cb.build())
    val twitter = tf.getInstance()
    twitter.getHomeTimeline().iterator().asScala
      .map(status => Tweet(status.getId, status.getCreatedAt.getTime, status.getUser.getId, status.getUser.getName,
        status.getText, status.getUser.getFriendsCount)).toList
  }
}