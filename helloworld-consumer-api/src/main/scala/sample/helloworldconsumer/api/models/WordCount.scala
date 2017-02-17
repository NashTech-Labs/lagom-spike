package sample.helloworldconsumer.api.models

import play.api.libs.json.{Format, Json}


case class WordCount(name: String, organization: Option[String])

object WordCount {

  /**
    * Format for the hello command.
    *
    * Persistent entities get sharded across the cluster. This means commands
    * may be sent over the network to the node where the entity lives if the
    * entity is not on the same node that the command was issued from. To do
    * that, a JSON format needs to be declared so the command can be serialized
    * and deserialized.
    */
  implicit val format: Format[WordCount] = Json.format
}
