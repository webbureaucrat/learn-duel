package de.htwg.se.learn_duel.controller.impl

import de.htwg.se.learn_duel.{UpdateAction, UpdateData}
import de.htwg.se.learn_duel.controller.impl.exceptions._
import de.htwg.se.learn_duel.controller.{Controller => ControllerTrait}
import de.htwg.se.learn_duel.model.impl.Player
import de.htwg.se.learn_duel.model.impl.Game

class Controller private (gameState: Game) extends ControllerTrait {
    override def nextPlayerName: String = Player.baseName + (gameState.playerCount + 1).toString

    override def getCurrentPlayers: List[String] = {
        gameState.players.map(p => p.name)
    }

    override def addPlayer(name: Option[String]): Unit = {
        var playerName = name match {
            case Some(name) => name
            case None => nextPlayerName
        }

        if (gameState.playerCount == Game.maxPlayerCount) {
            throw TooManyPlayersException("There are too many players to add another one")
        }
        else if (gameState.players.exists(p => p.name == playerName)) {
            throw PlayerExistsException(s"'$playerName' already exists")
        }

        try {
            gameState.addPlayer(Player(playerName))
        } catch {
            case e: Throwable => throw ControllerProcedureFailed("Adding player failed: " + e.getMessage)
        }
        notifyObservers(new UpdateData(UpdateAction.CLOSE_APPLICATION, Option(null)))
    }

    override def removePlayer(name: String): Unit = {
        if (gameState.playerCount == 1) {
            throw NotEnoughPlayersException("There are not enough players to remove one")
        }

        gameState.players.find(p => p.name == name) match {
            case Some(p) =>
                gameState.removePlayer(p)
                notifyObservers(new UpdateData(UpdateAction.CLOSE_APPLICATION, Option(null)))
            case None => throw PlayerNotExistingException(s"Player '$name' does not exist")
        }
    }

    override def getMaxPlayerCount: Int = Game.maxPlayerCount

    override def onHelp(): Unit = {
        if (gameState.helpText.isEmpty) {
            import scala.io.Source
            val helpText: Iterator[String] = Source.fromResource("help.txt").getLines
            gameState.helpText = helpText.mkString("\n")
        }

        notifyObservers(new UpdateData(UpdateAction.SHOW_HELP, Option(gameState)))
    }

    override def onStartGame(): Unit = {

    }

    override def onClose(): Unit = {
        notifyObservers(new UpdateData(UpdateAction.CLOSE_APPLICATION, Option(null)))
    }
}

object Controller {
    // FIXME Builder Pattern
    def create(gameState: Game): Controller = {
        new Controller(gameState)
    }
}
