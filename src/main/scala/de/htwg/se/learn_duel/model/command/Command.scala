package de.htwg.se.learn_duel.model.command

trait Command {
    def execute(): Unit
    def undo(): Unit
    def redo(): Unit
}
