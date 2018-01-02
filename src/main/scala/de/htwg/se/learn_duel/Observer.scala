package de.htwg.se.learn_duel

object UpdateAction extends Enumeration {
    type UpdateAction = Value
    val CLOSE_APPLICATION, START_GAME, SHOW_HELP = Value
}
import UpdateAction._
import de.htwg.se.learn_duel.model.Game

class UpdateData(updateAction: UpdateAction, gameState: Option[Game]) {
    def getAction(): UpdateAction = updateAction
    def getState(): Option[Game] = gameState
}

trait Observer {
    def update(updateData: UpdateData): Unit
}
