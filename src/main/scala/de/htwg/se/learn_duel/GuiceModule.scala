package de.htwg.se.learn_duel

import akka.actor.ActorSystem
import com.google.inject.AbstractModule
import de.htwg.se.learn_duel.controller.{Controller, ControllerActorFactory}
import de.htwg.se.learn_duel.controller.impl.{Controller => ControllerImpl}
import de.htwg.se.learn_duel.model.{Game, Question}
import de.htwg.se.learn_duel.model.impl.{Game => GameImpl}
import de.htwg.se.learn_duel.view.impl.RestUi
import play.api.libs.json.{JsValue, Json}

import scala.io.Source

class GuiceModule extends AbstractModule {
  implicit val actorSystem = ActorSystem("actorSystem")

  val jsonString: String =
    Source.fromResource("questions.json").getLines.mkString("\n")
  val json: JsValue = Json.parse(jsonString)
  val questions: List[Question] =
    Json.fromJson[List[Question]](json).getOrElse(List())

  override def configure(): Unit = {
    bind(classOf[Game]).toInstance(GameImpl(questions = questions))
    bind(classOf[Controller]).to(classOf[ControllerImpl])
    bind(classOf[ActorSystem]).toInstance(actorSystem)
  }
}
