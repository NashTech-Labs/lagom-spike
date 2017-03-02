package com.knoldus.twitterproducer.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * Created by Knoldus on 16/2/17.
  */
trait TwitterSchedulerService extends Service {

  def scheduler: ServiceCall[NotUsed, Done]

  override def descriptor: Descriptor = {
    import Service._
    named("scheduler").
      withCalls(call(scheduler _))
      .withAutoAcl(true)
  }
}
