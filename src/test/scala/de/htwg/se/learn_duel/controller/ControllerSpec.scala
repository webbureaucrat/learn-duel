package de.htwg.se.learn_duel.controller

import de.htwg.se.learn_duel.{Observer, UpdateAction, UpdateData}
import de.htwg.se.learn_duel.model.{Game, Question}
import de.htwg.se.learn_duel.model.impl.{Game => GameImpl}
import de.htwg.se.learn_duel.controller.impl.{Controller => ControllerImpl}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.{JsValue, Json}

import scala.io.Source

class DummyObserver extends Observer {
    var updateData: Option[UpdateData] = None
    override def update(updateData: UpdateData): Unit = {
        this.updateData = Some(updateData)
    }
}

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
    "A Controller" when {
        val jsonString: String = Source.fromResource("test_questions.json").getLines.mkString("\n")
        val json: JsValue = Json.parse(jsonString)
        val questionList: List[Question] = Json.fromJson[List[Question]](json).getOrElse(List())
        val gameState = GameImpl(questions = questionList)
        val dummyObserver = new DummyObserver()

        "constructed" should {
            val controller = new ControllerImpl(gameState)

            "update observers with the last update on request to do so" in {
                controller.addObserver(dummyObserver)
                dummyObserver.updateData should be(None)
                controller.requestUpdate()
                dummyObserver.updateData.isDefined should be(true)
                dummyObserver.updateData.get.getAction() should be(UpdateAction.BEGIN)
                dummyObserver.updateData.get.getState() should be(gameState)
            }
            "be able to generate a list of player names" in {
                controller.getPlayerNames should be(gameState.players.map(p => p.name))
            }
            "have a max player count" in {
                controller.maxPlayerCount() should be (Game.maxPlayerCount)
            }
            "be able to add a player" in {
                gameState.playerCount() should be(1)
                controller.onAddPlayer(Some("player"))
                gameState.playerCount() should be(2)
            }
            "be able to remove a player" in {
                gameState.playerCount() should be(2)
                controller.onRemovePlayer("player")
                gameState.playerCount() should be(1)
            }
            "be able to add a player with a default name" in {
                gameState.playerCount() should be(1)
                controller.onAddPlayer(None)
                gameState.playerCount() should be(2)
            }
            "be able do undo and redo a player action" in {
                gameState.playerCount() should be(2)
                controller.onPlayerActionUndo()
                gameState.playerCount() should be(1)
                controller.onPlayerActionRedo()
                gameState.playerCount() should be(2)
            }
            "not supply a next player name if max player count is reached" in {
                gameState.playerCount() should be(2)
                val nextName = controller.nextPlayerName()
                nextName should be(None)
            }
            "supply a next player name if max player count was not reached" in {
                controller.onPlayerActionUndo()
                gameState.playerCount() should be(1)
                val nextName = controller.nextPlayerName()
                nextName should be(Some("Player2"))
            }
            "indicate to observers that they should show the help" in {
                controller.onHelp()
                dummyObserver.updateData.get.getAction() should be(UpdateAction.SHOW_HELP)
            }
            "indicate to observers that they should start the game" in {
                controller.onStartGame()
                dummyObserver.updateData.get.getAction() should be(UpdateAction.SHOW_GAME)
                gameState.currentQuestion should not be(None)
                gameState.currentQuestionTime should not be(None)
            }
            "indicate to observers that they should close" in {
                controller.onClose()
                dummyObserver.updateData.get.getAction() should be(UpdateAction.CLOSE_APPLICATION)
            }
            "be able to reset game state" in {
                controller.onPlayerActionRedo()
                gameState.playerCount() should be(2)
                gameState.currentQuestion should not be(None)
                gameState.currentQuestionTime should not be(None)
                dummyObserver.updateData.get.getAction() should not be(UpdateAction.BEGIN)

                controller.reset()
                gameState.playerCount() should be(1)
                gameState.currentQuestion should be(None)
                gameState.currentQuestionTime should be(None)
                dummyObserver.updateData.get.getAction() should be(UpdateAction.BEGIN)
            }
            "add correctly and wrongly answered questions to player and move on to next question" in {
                gameState.players.head.correctAnswers.length should be(0)
                gameState.players.head.wrongAnswers.length should be(0)
                controller.onStartGame()

                val question1 = gameState.currentQuestion

                controller.onAnswerChosen(2)

                gameState.players.head.correctAnswers.length should be(1)

                controller.onAnswerChosen(1)

                gameState.players.head.wrongAnswers.length should be(1)
                dummyObserver.updateData.get.getAction() should be(UpdateAction.SHOW_RESULT)
            }
            "should update question timer" in {
                controller.reset()
                controller.onStartGame()
                val time = gameState.currentQuestionTime
                Thread.sleep(2000)
                gameState.currentQuestionTime should not be(time)
            }
            "remove observers correctly" in {
                controller.onHelp()
                val currentAction = dummyObserver.updateData.get.getAction()
                currentAction should not be(UpdateAction.SHOW_GAME)
                controller.removeObserver(dummyObserver)
                controller.onStartGame()
                dummyObserver.updateData.get.getAction() should not be(UpdateAction.SHOW_GAME)
                dummyObserver.updateData.get.getAction() should be(currentAction)
            }
        }
        "constructed with no questions" should {
            val newState = GameImpl(questions = questionList)
            newState.questions = List()

            val controller = new ControllerImpl(newState)

            "throw on start game" in {
                assertThrows[IllegalStateException] {
                    controller.onStartGame()
                }
            }
        }
        "constructed with factory method" should {
            val manualController = new ControllerImpl(gameState)
            "be valid" in {
                val controller = Controller.create(gameState)
            }
        }
    }
}
