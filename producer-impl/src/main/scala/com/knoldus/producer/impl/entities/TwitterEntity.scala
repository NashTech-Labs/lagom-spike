package com.knoldus.producer.impl.entities

import java.time.LocalDateTime

import com.knoldus.producer.api.models.Tweet
import com.knoldus.producer.impl.commands.TwitterCommand
import com.knoldus.producer.impl.events.TwitterEvent
import com.knoldus.producer.impl.states.TweetState
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  * Created by harmeet on 16/2/17.
  */
class TwitterEntity extends PersistentEntity {

  override type Command = TwitterCommand[_]
  override type Event = TwitterEvent
  override type State = TweetState

  override def initialState: TweetState = TweetState(Tweet(0, 0, 0, "" ,"", 0), LocalDateTime.now().toString)

  override def behavior: Behavior = ???
}
