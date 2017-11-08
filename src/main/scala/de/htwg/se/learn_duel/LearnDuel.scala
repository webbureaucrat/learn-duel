package de.htwg.se.learn_duel

import de.htwg.se.learn_duel.ui.impl.TUI

object LearnDuel {
    def main(args: Array[String]): Unit = {
        println(TUI.createTUI.dummyText)
    }
}
