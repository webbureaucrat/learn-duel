package de.htwg.se.learn_duel

object UpdateAction extends Enumeration {
    type UpdateAction = Value
    val CLOSE_APPLICATION, PLAYER_UPDATE, SHOW_HELP,
        SHOW_GAME, SHOW_RESULT, UPDATE_TIMER = Value
}
import UpdateAction._
import de.htwg.se.learn_duel.model.Game

class UpdateData(updateAction: UpdateAction, gameState: Game) {
    def getAction(): UpdateAction = updateAction
    def getState(): Game = gameState
}

trait Observer {
    protected def update(updateData: UpdateData): Unit
}
