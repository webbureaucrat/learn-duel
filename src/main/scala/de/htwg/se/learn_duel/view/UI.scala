package de.htwg.se.learn_duel.view

import de.htwg.se.learn_duel.Observer

trait UI extends Observer {
    def displayMenu(): Unit
}
