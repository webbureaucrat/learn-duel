package de.htwg.se.learn_duel.view.impl

import de.htwg.se.learn_duel.controller.Controller
import de.htwg.se.learn_duel.view.UI

class TUI private (controller: Controller) extends UI {
    controller.addObserver(this)
    displayMenu()

    override def update: Unit = {

    }

    override def displayMenu(): Unit = {
        println("")
        println("Welcome to Learn Duel")
        println("Current players: " + controller.getCurrentPlayers().mkString(", "))
        println("n => new game")
        println("a [name] => add player")
        println("r [name] => remove player")
        println("q => exit")
        println("")
    }

    def processInputLine(input: String): Boolean = {
        var continue = true
        val playerPattern = """(?:a|r)(?:\s+(.*))?""".r
        input match {
            case "q" => continue = false
            case "n" =>
            case playerPattern(name) if input.startsWith("a") => controller.addPlayer(Option(name))
            case playerPattern(name) if input.startsWith("r") => if (name != null) { controller.removePlayer(name) }
            case _ => println("Unknown command")
        }

        displayMenu()
        continue
    }
}

object TUI {
    def create(controller: Controller): TUI = {
        new TUI(controller)
    }
}
