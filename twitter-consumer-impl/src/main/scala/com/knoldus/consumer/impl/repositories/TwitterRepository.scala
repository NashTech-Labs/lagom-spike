package com.knoldus.consumer.impl.repositories

import com.datastax.driver.core.Row
import com.knoldus.producer.api.models.Tweet
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Knoldus on 20/2/17.
  */
private[impl]class TwitterRepository(cassandraSession: CassandraSession)(implicit ec: ExecutionContext) {

  def fetchAllLatestTweets(limit: Int): Future[Seq[Tweet]] = {
    val query =
      """
        |SELECT * FROM tweets
        |LIMIT ?
      """.stripMargin
    cassandraSession.selectAll(query, int2Integer(limit)).map { rows =>
      rows.map(row => convertRowToTweets(row))
    }
  }

  private def convertRowToTweets(row: Row) = {
      Tweet(
        row.getLong("tweet_id"),
        row.getLong("created_at"),
        row.getLong("user_id"),
        row.getString("tweet_user_name"),
        row.getString("text"),
        row.getLong("friends_count")
      )
  }
}
