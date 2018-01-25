package de.htwg.se.learn_duel.controller

import com.google.inject.Guice
import de.htwg.se.learn_duel.GuiceModule
import de.htwg.se.learn_duel.model.Game
import de.htwg.se.learn_duel.controller.impl.{Controller => ControllerImpl}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
    "A Controller" when {
        val injector = Guice.createInjector(new GuiceModule())
        val gameState = injector.getInstance(classOf[Game])

        "constructed" should {
            val controller = new ControllerImpl(gameState)

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
        }

        "constructed with factory method" should {
            val manualController = new ControllerImpl(gameState)
            "be valid" in {
                val controller = Controller.create(gameState)
            }
        }
    }
}
