package com.knoldus.producer.impl.entities

import java.time.LocalDateTime

import akka.Done
import com.knoldus.producer.impl.commands.{PutTweet, TweetCommand}
import com.knoldus.producer.impl.events.{TweetEvent, TweetSaved}
import com.knoldus.producer.impl.states.TweetState
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  * Created by harmeet on 16/2/17.
  */
class TwitterEntity extends PersistentEntity {

  override type Command = TweetCommand[_]
  override type Event = TweetEvent
  override type State = TweetState

  override def initialState: TweetState = TweetState(Option.empty, LocalDateTime.now().toString)

  override def behavior: Behavior = {
    case TweetState(tweet, _) => Actions().
      onCommand[PutTweet, Done] {

      case (PutTweet(tweet), ctx, state) =>
        //println(s"New Tweet ${tweet}")

        ctx.thenPersist( TweetSaved(tweet) ) { _ =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (TweetSaved(tweet), state) =>
        println(s" TweetSaved Event Fire .... ")
        TweetState(Option(tweet), LocalDateTime.now().toString)
    }
  }
}
