package de.htwg.se.learn_duel

import de.htwg.se.learn_duel.controller.Controller
import java.io.BufferedReader

import com.google.inject.Guice
import de.htwg.se.learn_duel.view.{GUI, TUI}

object LearnDuel {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new GuiceModule())
    val controller = injector.getInstance(classOf[Controller])

    val tui = TUI.create(controller)
    GUI.create(controller)

    controller.requestUpdate()
    tui.processInput(new BufferedReader(Console.in))
  }
}
