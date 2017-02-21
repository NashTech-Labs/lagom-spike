package sample.helloworldconsumer.impl

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

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
        """        CREATE TABLE IF NOT EXISTS wordcounts (
                      words text,
                      count_value counter,
                      PRIMARY KEY (words)
                    )
        """)
    } yield Done
  }

  private def prepareStatements(): Future[Done] = {
    for {
      insert <- cassandraSession.prepare(
        """UPDATE wordcounts set count_value = count_value +1 where words = ? """)
    } yield {
      insertWordStmt = insert
      Done
    }
  }

  private def insertWord(started: EventStreamElement[MessageSaved]) = {
    Future.successful {
      val words = started.event.msg.replaceAll("[^\\p{L}\\p{Nd}]+", " ").split(" ").toList
      println("get the messgea and all words "+words)
      val bindings =  words.map{ word=> insertWordStmt.bind(words) }
      println("values are ===============================> "+bindings)
      bindings
    }
  }

}
