package de.htwg.se.learn_duel

import de.htwg.se.learn_duel.controller.Controller
import de.htwg.se.learn_duel.model.impl.Game
import java.io.BufferedReader

import com.google.inject.Guice
import de.htwg.se.learn_duel.model.Question
import de.htwg.se.learn_duel.view.{GUI, TUI}
import play.api.libs.json.Json

import scala.io.Source

object LearnDuel {
    def main(args: Array[String]): Unit = {
        val injector = Guice.createInjector(new GuiceModule())
        val controller = injector.getInstance(classOf[Controller])

        val tui = TUI.create(controller)
        GUI.create(controller)

        controller.requestUpdate
        tui.processInput(new BufferedReader(Console.in))
    }
}
