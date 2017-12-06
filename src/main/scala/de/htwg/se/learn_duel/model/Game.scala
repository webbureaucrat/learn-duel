package de.htwg.se.learn_duel.model

trait Game {
    protected var _players: List[Player] = List()
    def players: List[Player] = _players

    def addPlayer(player: Player): Unit
    def removePlayer(player: Player): Unit
    def playerCount(): Int

    def getHelp(): String
}
