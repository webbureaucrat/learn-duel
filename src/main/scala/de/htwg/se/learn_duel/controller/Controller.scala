package de.htwg.se.learn_duel.controller

import de.htwg.se.learn_duel.Observable
import de.htwg.se.learn_duel.controller.impl.{Controller => ControllerImpl}
import de.htwg.se.learn_duel.model.{Game, Player, Question}

trait Controller extends Observable {
    def nextPlayerName: Option[String]
    def getCurrentPlayers: List[String]
    def addPlayer(name: Option[String]): Unit
    def removePlayer(name: String): Unit
    def maxPlayerCount: Int
    def onHelp: Unit
    def onStartGame: Unit
    def onClose: Unit
    def answerChosen(input: Int)
}

object Controller {
    def create(gameState: Game): ControllerImpl = {
        new ControllerImpl(gameState)
    }
}
