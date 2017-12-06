package de.htwg.se.learn_duel.controller.impl

import de.htwg.se.learn_duel.controller.{Controller => ControllerTrait}
import de.htwg.se.learn_duel.model.impl.Player
import de.htwg.se.learn_duel.model.impl.Game

class Controller private (gameState: Game) extends ControllerTrait {
    override def nextPlayerName(): String = List(Player.baseName, gameState.playerCount + 1).mkString(" ")

    override def getCurrentPlayers(): List[String] = {
        gameState.players.map(p => p.name)
    }

    override def addPlayer(name: Option[String]): Unit = {
        var playerName = name match {
            case Some(name) => name
            case None => nextPlayerName()
        }

        if (gameState.playerCount == Game.maxPlayerCount) {
            // FIXME error: max player count reached
            return
        }
        else if (gameState.players.exists(p => p.name == name)) {
            // FIXME error: player already exists
            return
        }

        gameState.addPlayer(Player(playerName))
    }

    override def removePlayer(name: String): Unit = {
        if (gameState.playerCount == 1) {
            // FIXME error: can't remove last player
            return
        }

        gameState.players.find(p => p.name == name) match {
            case Some(p) => gameState.removePlayer(p)
            case None =>
        }
    }

    override def getMaxPlayerCount(): Int = Game.maxPlayerCount
}

object Controller {
    def create(gameState: Game): Controller = {
        new Controller(gameState)
    }
}
