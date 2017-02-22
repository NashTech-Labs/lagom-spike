package com.knoldus.producer.impl

import akka.Done
import akka.actor.{AbstractActor, ActorSystem, Props}
import com.knoldus.producer.api.{TwitterProducerService, TwitterSchedulerService}
import com.knoldus.producer.impl.util.TwitterUtil
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationDouble

/**
  * Created by harmeet on 16/2/17.
  */
class TwitterSchedulerServiceImpl(system: ActorSystem, twitterService: TwitterProducerService)
  extends TwitterSchedulerService {

  override def scheduler = ServiceCall { _ =>
    Future {
      val ref = system.actorOf(Props(classOf[TwitterActor], twitterService))
      system.scheduler.schedule(500 millis, 2 minutes, ref, Start)
      Done
    }
  }
}

class TwitterActor(twitterService: TwitterProducerService) extends AbstractActor {

  override def receive = {
    case Start => {
      val tweets = TwitterUtil.fetchTweets
      tweets.foreach(twitterService.addNewTweet.invoke(_))
    }
    case _ => println("unknown message")
  }
}

case object Start