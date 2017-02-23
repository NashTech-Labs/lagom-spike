package com.knoldus.producer.impl

import akka.actor.{AbstractActor, ActorSystem, Props}
import akka.{Done, NotUsed}
import com.knoldus.producer.api.{TwitterProducerService, TwitterSchedulerService}
import com.knoldus.producer.impl.util.TwitterUtil
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationDouble

/**
  * Created by Knoldus on 16/2/17.
  */
class TwitterSchedulerServiceImpl(system: ActorSystem, twitterService: TwitterProducerService, twitterUtil: TwitterUtil)
  extends TwitterSchedulerService {

  val log = LoggerFactory.getLogger(classOf[TwitterSchedulerServiceImpl])

  override def scheduler: ServiceCall[NotUsed, Done] = ServiceCall { _ =>
    log.info("scheduler service call ... ")
    Future {
      val ref = system.actorOf(Props(classOf[TwitterActor], twitterService, twitterUtil))
      system.scheduler.schedule(500 millis, 2 minutes, ref, Start)
      Done
    }
  }
}

class TwitterActor(twitterService: TwitterProducerService, util: TwitterUtil) extends AbstractActor {

  val log = LoggerFactory.getLogger(classOf[TwitterActor])

  override def receive: PartialFunction[Any, Unit] = {
    case Start => {
      val tweets = util.fetchTweets
      tweets.foreach(sender() ! twitterService.addNewTweet.invoke(_))
    }
    case _ => log.info("unknown message")
  }
}

case object Start