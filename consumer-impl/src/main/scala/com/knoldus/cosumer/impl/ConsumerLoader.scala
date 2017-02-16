package com.knoldus.cosumer.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import play.api.libs.ws.ahc.AhcWSComponents

/**
  * Created by harmeet on 16/2/17.
  */
class ConsumerLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new ConsumerApplication(context) {
      override def serviceLocator = NoServiceLocator
    }
}

abstract class ConsumerApplication(context: LagomApplicationContext) extends LagomApplication(context)
with CassandraPersistenceComponents with AhcWSComponents {

  override def lagomServer = ???

  override def jsonSerializerRegistry = ???
}