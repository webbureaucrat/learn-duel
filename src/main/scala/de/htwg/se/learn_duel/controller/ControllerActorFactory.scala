package de.htwg.se.learn_duel.controller

import akka.actor.{ActorRef, ActorSystem, Props}
import com.google.inject.{Inject, Injector}

case class ControllerActorFactory @Inject()(injector: Injector, system: ActorSystem) {
  var controllers: Map[String, ActorRef] = Map.empty

  def createInstance(id: String): Unit = {
    val newController = system.actorOf(Props(injector.getInstance(classOf[Controller])))
    controllers = controllers + (id -> newController)
  }

  def getInstance(id: String): Option[ActorRef] = {
    controllers.get(id)
  }
}
