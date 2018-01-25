package de.htwg.se.learn_duel.model.command

import de.htwg.se.learn_duel.model.command.impl.{ CommandInvoker => CommandInvokerImpl, PlayerAddCommand }
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommandInvokerSpec extends WordSpec with Matchers {
    "A CommandInvoker" when {
        "constructed" should {
            var addCalled = false
            var removeCalled = false
            val nameAfterAdd = "playerAdd"
            val playerAddCommand = PlayerAddCommand(
                Some("player"),
                _ => { addCalled = true; nameAfterAdd },
                _ => { removeCalled = true; }
            )
            val commandInvoker = CommandInvokerImpl()

            "have no saved commands" in {
                commandInvoker.undoCommands.length should be(0)
                commandInvoker.redoCommands.length should be(0)
            }
            "execute given commands and save them" in {
                addCalled should be(false)

                commandInvoker.execute(playerAddCommand)

                addCalled should be(true)
                commandInvoker.redoCommands.length should be(0)
                commandInvoker.undoCommands.length should be(1)
            }
            "be able to undo command" in {
                removeCalled should be(false)
                commandInvoker.redoCommands.length should be(0)
                commandInvoker.undoCommands.length should be(1)

                commandInvoker.undo()

                removeCalled should be(true)
                commandInvoker.redoCommands.length should be(1)
                commandInvoker.undoCommands.length should be(0)
            }
            "not do anything on undo when there are no commands to undo" in {
                commandInvoker.undoCommands.length should be(0)

                removeCalled = false
                commandInvoker.undo()

                removeCalled should be(false)
            }
            "be able to redo command" in {
                addCalled = false
                commandInvoker.redoCommands.length should be(1)
                commandInvoker.undoCommands.length should be(0)

                commandInvoker.redo()

                addCalled should be(true)
                commandInvoker.redoCommands.length should be(0)
                commandInvoker.undoCommands.length should be(1)
            }
            "not do anything on redo when there are no commands to redo" in {
                commandInvoker.redoCommands.length should be(0)

                addCalled = false
                commandInvoker.redo()

                addCalled should be(false)
            }
        }

        "constructed with factory method" should {
            val commandInvoker = CommandInvoker.create()
            "have no saved commands" in {
                commandInvoker.undoCommands.length should be(0)
                commandInvoker.redoCommands.length should be(0)
            }
        }
    }
}

