package de.htwg.se.learn_duel.controller

import de.htwg.se.learn_duel.Observable
import de.htwg.se.learn_duel.controller.impl.{Controller => ControllerImpl}
import de.htwg.se.learn_duel.model.{Game, Player, Question}

trait Controller extends Observable {
    def nextPlayerName: Option[String]
    def getPlayerNames: List[String]
    def maxPlayerCount: Int
    def requestUpdate: Unit
    def onAddPlayer(name: Option[String]): Unit
    def onRemovePlayer(name: String): Unit
    def onPlayerActionUndo: Unit
    def onPlayerActionRedo: Unit
    def onHelp: Unit
    def onStartGame: Unit
    def onClose: Unit
    def onAnswerChosen(input: Int)
}

object Controller {
    def create(gameState: Game): ControllerImpl = {
        new ControllerImpl(gameState)
    }
}
