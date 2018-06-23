package de.htwg.se.learn_duel.model

import akka.actor.Actor
import de.htwg.se.learn_duel.model.impl.{Game => GameImpl}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait Game extends Resettable {
  var helpText: List[String]
  var players: List[Player]
  var questions: List[Question]
  var currentQuestion: Option[Question]
  var currentQuestionTime: Option[Int]

  def addPlayer(player: Player): Unit
  def removePlayer(player: Player): Unit
  def playerCount(): Int

  def addQuestion(question: Question): Unit
  def removeQuestion(question: Question): Unit
  def questionCount(): Int
}

object Game {
  val maxPlayerCount = 2

  implicit val gameWrites: Writes[Game] = new Writes[Game] {
    def writes(game: Game): JsObject = Json.obj(
      "helpText" -> game.helpText,
      "players" -> game.players,
      "questions" -> game.questions,
      "currentQuestion" -> game.currentQuestion,
      "currentQuestionTime" -> game.currentQuestionTime
    )
  }

  implicit val questionReads: Reads[Game] = (
    (JsPath \ "helpText").read[List[String]] and
      (JsPath \ "players").read[List[Player]] and
      (JsPath \ "questions").read[List[Question]] and
      (JsPath \ "currentQuestion").readNullable[Question] and
      (JsPath \ "currentQuestionTime").readNullable[Int]
  )(GameImpl.apply _)
}
