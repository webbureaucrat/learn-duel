package de.htwg.se.learn_duel.controller

import de.htwg.se.learn_duel.controller.impl.exceptions._
import de.htwg.se.learn_duel.{Observer, UpdateAction, UpdateData}
import de.htwg.se.learn_duel.model.{Game, Question}
import de.htwg.se.learn_duel.model.impl.{Game => GameImpl}
import de.htwg.se.learn_duel.controller.impl.{Controller => ControllerImpl}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.{JsValue, Json}

import scala.io.Source

class DummyObserver2 extends Observer {
  var updateData: Option[UpdateData] = None
  override def update(updateData: UpdateData): Unit = {
    this.updateData = Some(updateData)
  }
}

@RunWith(classOf[JUnitRunner])
class DatabaseTest extends WordSpec with Matchers {
  "A Controller1" when {
    val jsonString: String =
      Source.fromResource("test_questions.json").getLines.mkString("\n")
    val json: JsValue = Json.parse(jsonString)
    val questionList: List[Question] =
      Json.fromJson[List[Question]](json).getOrElse(List())
    val gameState = GameImpl(questions = questionList)

    val dummyObserver = new DummyObserver()

    "constructed" should {
      val controller = new ControllerImpl(gameState)
      controller.onAddPlayer(Some("Hinz"))
      controller.onStartGame()
      controller.onAnswerChosen(2)
      controller.onAnswerChosen(7)
      controller.onAnswerChosen(5)
      controller.onAnswerChosen(9)
      Thread.sleep(4000)
      "insert save-games in database" in {
        for( a <- 1 to 1000) {
          controller.onSaveResult()
        }
      }

    }
  }
}
