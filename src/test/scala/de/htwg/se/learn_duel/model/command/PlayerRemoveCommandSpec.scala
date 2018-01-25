package de.htwg.se.learn_duel.model

import java.security.InvalidParameterException

import de.htwg.se.learn_duel.model.command.impl.PlayerRemoveCommand
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlayerRemoveCommandSpec extends WordSpec with Matchers {
    "A PlayerRemoveCommand" when {
        "constructed" should {
            var addCalled = false
            var removeCalled = false
            val playerRemoveCommand = PlayerRemoveCommand(
                "player",
                _ => { removeCalled = true; },
                _ => { addCalled = true; "player" }
            )

            "call the remove function on execute" in {
                playerRemoveCommand.execute()
                removeCalled should be(true)
            }

            "call the add function on undo" in {
                playerRemoveCommand.undo()
                addCalled should be(true)
            }

            "call the remove function on redo" in {
                removeCalled = false
                playerRemoveCommand.redo()
                removeCalled should be(true)
            }
        }
    }
}

