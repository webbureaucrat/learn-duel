package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.micro_service_impl.{PlayerHttpServer, Player => PlayerImpl}
import play.api.libs.functional.syntax._
import play.api.libs.json._

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
    print("Running")

    val player = PlayerImpl(name)
    val playerHttpServer: PlayerHttpServer = new PlayerHttpServer(player)
    player
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
    ) (PlayerImpl.apply _)
}
