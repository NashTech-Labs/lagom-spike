package sample.helloworldconsumer.impl

import akka.Done
import com.datastax.driver.core.{BoundStatement, PreparedStatement}
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by knoldus on 20/2/17.
  */
class MessageEventProcessor(cassandraReadSide: CassandraReadSide, cassandraSession: CassandraSession)
                           (implicit ec: ExecutionContext) extends ReadSideProcessor[MessageEvent] {

  private var insertWordStmt: PreparedStatement = _

  override def buildHandler(): ReadSideHandler[MessageEvent] = {
    cassandraReadSide.builder[MessageEvent]("message_offset")
      .setGlobalPrepare(createTable)
      .setPrepare { tags =>
        prepareStatements
      }
      .setEventHandler[MessageSaved](insertWord)
      .build()
  }

  override def aggregateTags: Set[AggregateEventTag[MessageEvent]] = Set(MessageEventTag.INSTANCE)

  private def createTable(): Future[Done] = {
    for {
      _ <- cassandraSession.executeCreateTable(
        """        CREATE TABLE IF NOT EXISTS wordcounttest (
                      words text,
                      insertion_time timestamp,
                      PRIMARY KEY (words,insertion_time)
                    )WITH CLUSTERING ORDER BY (insertion_time DESC)
        """)
    } yield Done
  }

  private def prepareStatements(): Future[Done] = {
    for {
      insert <- cassandraSession.prepare(
        """insert into wordcounttest(words ,insertion_time) values(? ,toTimestamp(now())) """)
    } yield {
      insertWordStmt = insert
      Done
    }
  }

  private def insertWord(started: EventStreamElement[MessageSaved]): Future[List[BoundStatement]] = {
    Future.successful {
     val words = started.event.msg.replaceAll("[^\\p{L}\\p{Nd}]+", " ").split(" ").toList
     words.map{ word=> insertWordStmt.bind(word) }
    }
  }

}
