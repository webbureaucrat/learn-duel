package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.impl.{Answer => AnswerImpl}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait Answer {
    val id: Int
    val text: String
}

object Answer {
    implicit val answerWrites: Writes[Answer] = new Writes[Answer] {
        def writes(answer: Answer): JsObject = Json.obj(
            "id" -> answer.id,
            "text" -> answer.text,
        )
    }

    implicit val answerReads: Reads[Answer] = (
            (JsPath \ "id").read[Int] and
            (JsPath \ "text").read[String]
        )(AnswerImpl.apply _)
}
