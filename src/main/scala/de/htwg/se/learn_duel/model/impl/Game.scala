package de.htwg.se.learn_duel.model.impl

import de.htwg.se.learn_duel.model.{Player => PlayerTrait, Game => GameTrait}

case class Game() extends GameTrait {
    addPlayer(new Player("Player 1"))

    override def addPlayer(player: PlayerTrait): Unit = _players = player :: _players

    override def removePlayer(player: PlayerTrait): Unit = _players = _players.filter(_ != player)

    override def playerCount(): Int = _players.size

    override def getHelp(): String = {
        import scala.io.Source
        val readmeText: Iterator[String] = Source.fromResource("readme.txt").getLines
        readmeText.mkString("\n")
    }
}

object Game {
    val maxPlayerCount = 4
}
