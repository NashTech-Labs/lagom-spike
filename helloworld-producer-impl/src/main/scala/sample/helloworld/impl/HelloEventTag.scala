package sample.helloworld.impl

import com.lightbend.lagom.scaladsl.persistence.AggregateEventTag

/**
  * Created by knoldus on 16/2/17.
  */
object HelloEventTag {
  val instance: AggregateEventTag[HelloEvent] = AggregateEventTag[HelloEvent]()

}
