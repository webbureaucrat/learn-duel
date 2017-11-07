package de.htwg.se.learn_duel.ui.impl

import de.htwg.se.learn_duel.controller.Controller
import de.htwg.se.learn_duel.ui.UI

class TUI(controller: Controller) extends UI {
    def dummyText: String = "Just some text."
}

object TUI {
    def createTUI: TUI = {
        val controller = new Controller {}
        new TUI(controller)
    }
}