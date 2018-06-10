package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.impl.{
  Player => PlayerImpl,
  Question => QuestionImpl
}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait Player {
  val name: String
  var points: Int
  var correctAnswers: List[Question]
  var wrongAnswers: List[Question]
  def toString: String
}

object Player {
  val baseName = "Player"
  def create(name: String): PlayerImpl = {
    PlayerImpl(name)
  }

  def create(name: String, points: Int, correctAnswers: List[String], wrongAnswers: List[String]): PlayerImpl = {
    val correct = correctAnswers.map(a => QuestionImpl(-1, a, 0, List.empty, -1, -1))
    val wrong = wrongAnswers.map(a => QuestionImpl(-1, a, 0, List.empty, -1, -1))
    PlayerImpl(name, points, correct, wrong)
  }

  implicit val playerWrites: Writes[Player] = new Writes[Player] {
    def writes(player: Player): JsObject = Json.obj(
      "name" -> player.name,
      "points" -> player.points,
      "correctAnswers" -> player.correctAnswers,
      "wrongAnswers" -> player.wrongAnswers
    )
  }

  implicit val playerReads: Reads[Player] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "points").read[Int] and
      (JsPath \ "correctAnswers").read[List[Question]] and
      (JsPath \ "wrongAnswers").read[List[Question]]
  )(PlayerImpl.apply _)
}
