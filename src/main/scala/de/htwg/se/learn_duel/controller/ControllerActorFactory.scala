package de.htwg.se.learn_duel.controller

import akka.actor.{ActorRef, ActorSystem, Props}
import com.google.inject.{Inject, Injector}

case class ControllerActorFactory @Inject()(injector: Injector, system: ActorSystem) {
  var controllers: Map[String, ActorRef] = Map.empty

  def getInstance(ip: String): ActorRef = {
    controllers.get(ip) match {
      case Some(controller) => controller
      case None => {
        val newController = system.actorOf(Props(injector.getInstance(classOf[Controller])))
        controllers = controllers + (ip -> newController)
        newController
      }
    }
  }
}
