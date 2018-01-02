package de.htwg.se.learn_duel.controller

import de.htwg.se.learn_duel.Observable

trait Controller extends Observable {
    def nextPlayerName: String
    def getCurrentPlayers: List[String]
    def addPlayer(name: Option[String]): Unit
    def removePlayer(name: String): Unit
    def getMaxPlayerCount: Int
    def onHelp: Unit
    def onStartGame: Unit
    def onClose: Unit
}
