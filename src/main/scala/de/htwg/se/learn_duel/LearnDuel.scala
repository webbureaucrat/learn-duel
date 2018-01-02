package de.htwg.se.learn_duel

import java.io.BufferedReader
import javafx.application.Platform

import de.htwg.se.learn_duel.controller.impl.Controller
import de.htwg.se.learn_duel.model.impl.Game
import de.htwg.se.learn_duel.view.impl.TUI
import de.htwg.se.learn_duel.view.impl.gui.GUI

object LearnDuel {
    def main(args: Array[String]): Unit = {
        val gameState = Game()
        val controller = Controller.create(gameState)
        val tui = TUI.create(controller)
        val gui = GUI.create(controller)

        tui.processInput(new BufferedReader(Console.in))
    }
}
