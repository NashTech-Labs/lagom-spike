package com.knoldus.consumer.impl.entities

import java.time.LocalDateTime

import akka.Done
import com.knoldus.consumer.impl.commands.{SaveNewTweet, TweetCommand}
import com.knoldus.consumer.impl.events.{TweetEvent, TweetSaved}
import com.knoldus.consumer.impl.states.TweetState
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  * Created by harmeet on 19/2/17.
  */
class TweetEntity extends PersistentEntity {

  override type Command = TweetCommand[_]
  override type Event = TweetEvent
  override type State = TweetState

  override def initialState = TweetState(Option.empty, LocalDateTime.now().toString)

  override def behavior: Behavior = {
    case TweetState(tweet, _) => Actions().onCommand[SaveNewTweet, Done]{
      case (SaveNewTweet(tweet), ctx, state) =>
        println(s"observe new tweet from kafka and save ${tweet}")

        ctx.thenPersist( TweetSaved(tweet)) { tweetSaved: TweetSaved =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (TweetSaved(tweet), state) =>
        println(s"TweetSaved event fire ...")

        TweetState(Option(tweet), LocalDateTime.now().toString)
    }
  }
}
