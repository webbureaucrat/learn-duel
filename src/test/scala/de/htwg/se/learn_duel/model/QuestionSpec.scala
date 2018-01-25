package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.impl.{Answer => AnswerImpl, Question => QuestionImpl}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.Json

@RunWith(classOf[JUnitRunner])
class QuestionSpec extends WordSpec with Matchers {
    "A Question" when {
        "new" should {
            val answers = List(AnswerImpl(0, "text"), AnswerImpl(1, "text2"))
            val question = QuestionImpl(0, "text", 20, answers, 0, 20)
            "have a ID" in {
                question.id should be(0)
            }
            "have a text" in {
                question.text should be("text")
            }
            "have a point value" in {
                question.points should be(20)
            }
            "have a list of answers" in {
                question.answers should be (answers)
            }
            "have a correct answer" in {
                question.correctAnswer should be(0)
            }
            "have a time" in {
                question.time should be(20)
            }
        }
        "serialized to JSON" should {
            "be correct" in {
                val answers = List(AnswerImpl(0, "text"))
                val question = QuestionImpl(0, "text", 20, answers, 0, 20)

                val jsonValue = Json.parse("{\"id\": 0, \"text\": \"text\", \"points\": 20, \"answers\": [{ \"id\": 0, \"text\": \"text\" }],\"correctAnswer\": 0, \"time\": 20}")

                Json.toJson(question) should be(jsonValue)
            }
        }
    }
}

