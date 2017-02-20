package com.knoldus.consumer.impl.events

import akka.Done
import akka.persistence.cassandra.session.scaladsl.CassandraSession
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraReadSide

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by harmeet on 19/2/17.
  */
class TweetEventProcessor(cassandraReadSide: CassandraReadSide, cassandraSession: CassandraSession)
                         (implicit ec: ExecutionContext) extends ReadSideProcessor[TweetEvent] {

  private var insertTweetStatement: PreparedStatement = _

  override def buildHandler(): ReadSideHandler[TweetEvent] = {
    cassandraReadSide.builder[TweetEvent]("tweet_offset")
      .setGlobalPrepare(createTable)
      .setPrepare { tags =>
        prepareStatements
      }
      .setEventHandler[TweetSaved](insertTweet)
      .build()
  }

  override def aggregateTags: Set[AggregateEventTag[TweetEvent]] = Set(TweetEventTag.INSTANCE)

  private def createTable(): Future[Done] = {
    for {
      _ <- cassandraSession.executeCreateTable(
        """
          CREATE TABLE IF NOT EXISTS tweets (
            tweet_id long,
            created_at long,
            user_id long,
            tweet_user_name string,
            text text,
            friends_count long
            PRIMARY KEY (tweet_id)
          )
      """)
    } yield Done
  }

  private def prepareStatements: Future[Done] = {
    for {
      insert <- cassandraSession.prepare(
        """INSERT INTO
          |tweets(tweetId, createdAt, userId, tweetUserName, text, friendsCount)
          |VALUES(?, ?, ?, ?, ?, ?)
          |""".stripMargin)
    } yield {
      insertTweetStatement = insert
      Done
    }
  }

  private def insertTweet(started: EventStreamElement[TweetSaved]) = {
    Future.successful {
      val tweet = started.event.tweet
      val bindings = insertTweetStatement.bind(long2Long(tweet.tweetId), long2Long(tweet.createdAt),
        long2Long(tweet.userId), tweet.tweetUserName, tweet.text, long2Long(tweet.friendsCount))
      List(bindings)
    }
  }
}
