package de.htwg.se.learn_duel

import de.htwg.se.learn_duel.controller.impl.Controller
import de.htwg.se.learn_duel.model.impl.Game
import de.htwg.se.learn_duel.view.impl.{GUI, TUI}

object LearnDuel {
    def main(args: Array[String]): Unit = {
        val gameState = Game
        val controller = Controller.create(gameState)
        val tui = TUI.create(controller)
        val gui = GUI.create(controller)

        while(tui.processInputLine(scala.io.StdIn.readLine())) {}
    }
}
