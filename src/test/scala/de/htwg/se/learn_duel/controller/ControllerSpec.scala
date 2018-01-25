package de.htwg.se.learn_duel.controller

import com.google.inject.Guice
import de.htwg.se.learn_duel.{GuiceModule, Observer, UpdateAction, UpdateData}
import de.htwg.se.learn_duel.model.Game
import de.htwg.se.learn_duel.controller.impl.{Controller => ControllerImpl}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

class DummyObserver extends Observer {
    var updateData: Option[UpdateData] = None
    override def update(updateData: UpdateData): Unit = {
        this.updateData = Some(updateData)
    }
}

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
    "A Controller" when {
        val injector = Guice.createInjector(new GuiceModule())
        val gameState = injector.getInstance(classOf[Game])
        val dummyObserver = new DummyObserver()

        "constructed" should {
            val controller = new ControllerImpl(gameState)

            "update observers with the last update on request to do so" in {
                controller.addObserver(dummyObserver)
                dummyObserver.updateData should be(None)
                controller.requestUpdate()
                dummyObserver.updateData.isDefined should be(true)
                dummyObserver.updateData.get.getAction() should be(UpdateAction.BEGIN)
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
        }

        "constructed with factory method" should {
            val manualController = new ControllerImpl(gameState)
            "be valid" in {
                val controller = Controller.create(gameState)
            }
        }
    }
}
