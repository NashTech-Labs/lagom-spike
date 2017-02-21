package sample.helloworldconsumer.impl

import play.api.libs.json.Json

/**
  * Created by knoldus on 20/2/17.
  */
case class MessageState(msg:String , timeStamp: String)

object MessageState {
  implicit val formatter = Json.format[MessageState]
}
