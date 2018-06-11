package de.htwg.se.learn_duel.model.impl

import de.htwg.se.learn_duel.model.{
    Player => PlayerTrait,
    Result => ResultTrait
}

case class Result(var dummy: String, var players: List[PlayerTrait]) extends ResultTrait {
}
