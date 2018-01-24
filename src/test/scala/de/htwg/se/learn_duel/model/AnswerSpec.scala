package de.htwg.se.learn_duel.model

import de.htwg.se.learn_duel.model.impl.{Answer => AnswerImpl}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.Json

@RunWith(classOf[JUnitRunner])
class AnswerSpec extends WordSpec with Matchers {
    "A Answer" when {
        "new" should {
            val answer = AnswerImpl(0, "text")
            "a ID" in {
                answer.id should be(0)
            }
            "have a text" in {
                answer.text should be("text")
            }
        }
    }

    "A Answer" should {
        "be serializable to JSON" in {
            val answer = AnswerImpl(0, "text")

            val jsonValue = Json.parse("{\"id\": 0, \"text\": \"text\"}")

            Json.toJson(answer) should be(jsonValue)
        }
    }
}

