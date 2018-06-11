package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.impl.{Result => ResultImpl}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait Result {
    var dummy: String // FIXME: the implicit resultReads is not working without this, find a solution
    var players: List[Player]
}

object Result {
    def create(players: List[Player]): ResultImpl = {
        ResultImpl("dummy", players)
    }

    implicit val resultWrites: Writes[Result] = (result: Result) => Json.obj(
        "dummy" -> result.dummy,
        "players" -> result.players
    )

    implicit val resultReads: Reads[Result] = (
      (JsPath \ "dummy").read[String] and
      (JsPath \ "players").read[List[Player]]
      )(ResultImpl.apply _)
}
