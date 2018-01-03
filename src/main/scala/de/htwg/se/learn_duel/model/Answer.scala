package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.impl.{Answer => AnswerImpl}
import play.api.libs.json.{JsPath, Json, Reads, Writes}
import play.api.libs.functional.syntax._

trait Answer {
    val id: Int
    val text: String
}

object Answer {
    implicit val answerWrites = new Writes[Answer] {
        def writes(answer: Answer) = Json.obj(
            "id" -> answer.id,
            "text" -> answer.text,
        )
    }

    implicit val answerReads: Reads[Answer] = (
            (JsPath \ "id").read[Int] and
            (JsPath \ "text").read[String]
        )(AnswerImpl.apply _)
}
