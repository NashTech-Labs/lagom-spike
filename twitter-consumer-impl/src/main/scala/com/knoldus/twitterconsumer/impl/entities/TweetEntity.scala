package com.knoldus.twitterconsumer.impl.entities

import java.time.LocalDateTime

import akka.Done
import com.knoldus.twitterconsumer.impl.commands.{SaveNewTweet, TweetCommand}
import com.knoldus.twitterconsumer.impl.events.{TweetEvent, TweetSaved}
import com.knoldus.twitterconsumer.impl.states.TweetState
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import org.slf4j.LoggerFactory

/**
  * Created by Knoldus on 19/2/17.
  */
class TweetEntity extends PersistentEntity {

  val logger = LoggerFactory.getLogger(classOf[TweetEntity])

  override type Command = TweetCommand[_]
  override type Event = TweetEvent
  override type State = TweetState

  override def initialState: TweetState = TweetState(Option.empty, LocalDateTime.now().toString)

  override def behavior: Behavior = {
    case TweetState(tweet, _) => Actions().onCommand[SaveNewTweet, Done]{
      case (SaveNewTweet(tweet), ctx, state) =>
        logger.info(s"observe new tweet from kafka and save ${tweet}")

        ctx.thenPersist( TweetSaved(tweet)) { tweetSaved: TweetSaved =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (TweetSaved(tweet), state) =>
        TweetState(Option(tweet), LocalDateTime.now().toString)
    }
  }
}
