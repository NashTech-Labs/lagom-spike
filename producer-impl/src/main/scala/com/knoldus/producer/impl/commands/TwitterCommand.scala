package com.knoldus.producer.impl.commands

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType

/**
  * Created by harmeet on 16/2/17.
  */
trait TwitterCommand[T] extends ReplyType[T]