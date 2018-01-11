package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.impl.{Question => QuestionImpl}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait Question {
    val id: Int
    val text: String
    val points: Int
    val answers: List[Answer]
    val correctAnswer: Int
    val time: Int
}

object Question {
    implicit val questionWrites: Writes[Question] = new Writes[Question] {
        def writes(question: Question): JsObject = Json.obj(
            "id" -> question.id,
            "text" -> question.text,
            "points" -> question.points,
            "answers" -> question.answers,
            "correctAnswer" -> question.correctAnswer,
            "time" -> question.time
        )
    }

    implicit val questionReads: Reads[Question] = (
            (JsPath \ "id").read[Int] and
            (JsPath \ "text").read[String] and
            (JsPath \ "points").read[Int] and
            (JsPath \ "answers").read[List[Answer]] and
            (JsPath \ "correctAnswer").read[Int] and
            (JsPath \ "time").read[Int]
        )(QuestionImpl.apply _)
}
