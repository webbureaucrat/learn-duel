package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.impl.{Result => ResultImpl}
import play.api.libs.json._

trait Result {
    var players: List[Player]
}

object Result {
    def create(players: List[Player]): ResultImpl = {
        ResultImpl(players)
    }

    implicit val resultWrites: Writes[Result] = (result: Result) => Json.obj(
        "players" -> result.players
    )

    implicit val resultReads: Reads[List[Player] => ResultImpl] = (JsPath \ "players").read(ResultImpl.apply _)
}
