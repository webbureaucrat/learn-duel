package de.htwg.se.learn_duel.controller

trait ControllerException {
    self: Throwable =>
    val message: String
}
