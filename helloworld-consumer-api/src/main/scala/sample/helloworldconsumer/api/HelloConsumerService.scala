package sample.helloworldconsumer.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import com.lightbend.lagom.scaladsl.api.transport.Method
import play.api.libs.json.{Format, Json}

trait HelloConsumerService extends Service {

  override def descriptor = {
    import Service._

    named("wordCounts").withCalls(
      restCall(Method.GET, "/api/wordcount", findTopHundredWordCounts _),
      restCall(Method.GET, "/api/foo", foo)
    ).withAutoAcl(true)
  }

  def findTopHundredWordCounts(): ServiceCall[NotUsed, Map[String, Int]]

  def foo(): ServiceCall[NotUsed, String]

  case class WordDetails(word: String, count: String)

  object WordDetails {
    implicit val format: Format[WordDetails] = Json.format
  }
}


