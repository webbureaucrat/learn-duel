package de.htwg.se.learn_duel

import de.htwg.se.learn_duel.controller.Controller
import de.htwg.se.learn_duel.model.impl.Game
import java.io.BufferedReader

import de.htwg.se.learn_duel.model.Question
import de.htwg.se.learn_duel.view.{GUI, TUI}
import play.api.libs.json.Json

import scala.io.Source

object LearnDuel {
    def main(args: Array[String]): Unit = {
        val jsonString = Source.fromResource("questions.json").getLines.mkString("\n")
        val json = Json.parse(jsonString)
        val questions = Json.fromJson[List[Question]](json).getOrElse(List())

        val gameState = Game(questions = questions)
        val controller = Controller.create(gameState)
        val tui = TUI.create(controller)
        GUI.create(controller)

        controller.requestUpdate
        tui.processInput(new BufferedReader(Console.in))
    }
}
