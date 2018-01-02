package de.htwg.se.learn_duel.view.impl

import java.io.{BufferedReader, StringReader}

import de.htwg.se.learn_duel.{Observer, UpdateAction, UpdateData}
import de.htwg.se.learn_duel.controller.{Controller, ControllerException}
import de.htwg.se.learn_duel.view.UI

import scalafx.collections.ObservableBuffer.Update

class TUI private (controller: Controller) extends UI with Observer {
    controller.addObserver(this)
    var stopProcessingInput = false
    displayMenu

    override def displayMenu(): Unit = {

        println("")
        println("Welcome to Learn Duel")
        println("Current players: " + controller.getCurrentPlayers.mkString(", "))
        println("n => new game")
        println("a [name] => add player")
        println("r [name] => remove player")
        println("h => show help")
        println("q => exit")
        println("")
    }

    override def displayGame(): Unit = {

    }

    override def update(updateParam: UpdateData): Unit = {
        updateParam.getAction() match {
            case UpdateAction.CLOSE_APPLICATION => stopProcessingInput = true
            case UpdateAction.SHOW_HELP => {
                val helpText = updateParam.getState() match {
                    case Some(gameState) => gameState.helpText
                    case None => "No help available."
                }
                println(helpText)
            }
        }
    }

    // scalastyle:off
    def processInput(input: BufferedReader): Unit = {
        val playerPattern = """(?:a|r)(?:\s+(.*))?""".r

        while (!stopProcessingInput) {
            if (input.ready()) {
                val line = input.readLine()

                // don't match input if we close the application anyway
                try {
                    line match {
                        case "q" => {
                            controller.onClose
                        }
                        case "n" =>
                        case playerPattern(name) if line.startsWith("a") => controller.addPlayer(Option(name))
                        case playerPattern(name) if line.startsWith("r") => if (name != null) {
                            controller.removePlayer(name)
                        }
                        case "h" => controller.onHelp
                        case _ => println("Unknown command")
                    }
                } catch {
                    case e: ControllerException => println(e.getMessage)
                }

                // check if the application should be closed after parsing input
                // if yes, don't repeat the menu
                if (!stopProcessingInput) {
                    displayMenu
                }
            }
        }
    }
    // scalastyle:on
}

object TUI {
    def create(controller: Controller): TUI = {
        new TUI(controller)
    }
}
