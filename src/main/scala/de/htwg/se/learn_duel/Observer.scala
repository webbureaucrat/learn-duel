package de.htwg.se.learn_duel

object UpdateAction extends Enumeration {
  type UpdateAction = Value
  val BEGIN, CLOSE_APPLICATION, PLAYER_UPDATE, SHOW_HELP, SHOW_GAME,
  SHOW_PREVIOUS_RESULTS, SHOW_RESULT, TIMER_UPDATE = Value
}
import UpdateAction._
import de.htwg.se.learn_duel.model.{Game, Result}

class UpdateData(
  updateAction: UpdateAction,
  gameState: Game,
  results: Option[List[Result]] = None
) {
  def getAction: UpdateAction = updateAction
  def getState: Game = gameState
  def getResults: Option[List[Result]] = results
}

trait Observer {
  def update(updateData: UpdateData): Unit
}
