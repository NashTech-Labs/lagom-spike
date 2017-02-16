package sample.helloworld.impl

import play.api.libs.json.{Format, Json}

/**
  * Created by knoldus on 16/2/17.
  */
/**
  * The current state held by the persistent entity.
  */
case class HelloState(message: String, timestamp: String)

object HelloState {
  /**
    * Format for the hello state.
    *
    * Persisted entities get snapshotted every configured number of events. This
    * means the state gets stored to the database, so that when the entity gets
    * loaded, you don't need to replay all the events, just the ones since the
    * snapshot. Hence, a JSON format needs to be declared so that it can be
    * serialized and deserialized when storing to and from the database.
    */
  implicit val format: Format[HelloState] = Json.format
}
