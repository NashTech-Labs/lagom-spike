package com.knoldus.producer.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * Created by harmeet on 16/2/17.
  */
trait TwitterSchedulerService extends Service {

  def doWork: ServiceCall[NotUsed, Done]

  override def descriptor: Descriptor = {
    import Service._
    named("scheduler").withCalls(call(doWork))
  }
}
