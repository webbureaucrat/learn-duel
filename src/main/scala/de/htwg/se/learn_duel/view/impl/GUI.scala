package de.htwg.se.learn_duel.view.impl

import de.htwg.se.learn_duel.controller.Controller
import de.htwg.se.learn_duel.view.UI

class GUI private (controller: Controller) extends UI {
    override def displayMenu(): Unit = {}

    override def update: Unit = {}
}

object GUI {
    def create(controller: Controller): GUI = {
        new GUI(controller)
    }
}
