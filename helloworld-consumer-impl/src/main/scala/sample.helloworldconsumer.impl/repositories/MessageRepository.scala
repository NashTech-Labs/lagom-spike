package sample.helloworldconsumer.impl.repositories

import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by knoldus on 21/2/17.
  */
private[impl] class MessageRepository(cassandraSession: CassandraSession)(implicit ec: ExecutionContext) {

  /**
    * Function to fetch top 100 messages and perform word count
    *
    * @param limit
    * @return
    */
  def fetchAndCountWordsFromMessages(limit: Int): Future[Map[String, Int]] = {
    val query =
      """
        |SELECT * FROM wordcounttest LIMIT 100
      """.stripMargin
    cassandraSession.selectAll(query).map { rows =>
      rows.map(row => row.getString("words")).groupBy(value => value).mapValues(_.size)
    }
  }


}
