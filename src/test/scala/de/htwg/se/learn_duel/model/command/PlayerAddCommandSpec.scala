package de.htwg.se.learn_duel.model

import java.security.InvalidParameterException

import de.htwg.se.learn_duel.model.command.impl.PlayerAddCommand
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlayerAddCommandSpec extends WordSpec with Matchers {
    "A PlayerAddCommand" when {
        "constructed" should {
            var addCalled = false
            var removeCalled = false
            val nameAfterAdd = "playerAdd"
            val playerAddCommand = PlayerAddCommand(
                Some("player"),
                _ => { addCalled = true; nameAfterAdd },
                _ => { removeCalled = true; }
            )

            "call the add function on execute" in {
                playerAddCommand.execute()
                addCalled should be(true)
                playerAddCommand.actualName should be(nameAfterAdd)
            }

            "call the remove function on undo" in {
                playerAddCommand.undo()
                removeCalled should be(true)
            }

            "call the add function on redo" in {
                addCalled = false
                playerAddCommand.redo()
                addCalled should be(true)
            }
        }
    }
}

