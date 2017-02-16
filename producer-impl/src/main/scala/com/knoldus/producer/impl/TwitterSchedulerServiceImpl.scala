package com.knoldus.producer.impl

import akka.{Done, NotUsed}
import akka.actor.{AbstractActor, ActorSystem, Props}
import com.knoldus.producer.api.{TwitterSchedulerService, TwitterService}
import com.knoldus.producer.impl.util.TwitterUtil
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.duration.DurationDouble
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by harmeet on 16/2/17.
  */
class TwitterSchedulerServiceImpl(system: ActorSystem, twitterService: TwitterService) extends TwitterSchedulerService{

  override def doWork = ServiceCall { some =>
    Future {
      println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ")
      val ref = system.actorOf(Props(classOf[TwitterActor], twitterService))
      system.scheduler.schedule(500 millis, 1000 millis, ref, Start)
      Done
    }
  }
}

class TwitterActor(twitterService: TwitterService) extends AbstractActor {

  override def receive = {
    case Start => {
      val tweet = TwitterUtil.fetchTweets
      twitterService.fetchTweetFromTwitter.invoke(tweet)
    }
  }
}

case object Start