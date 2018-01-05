package de.htwg.se.learn_duel.model.command

import de.htwg.se.learn_duel.model.command.impl.{ CommandInvoker => CommandInvokerImpl }

trait CommandInvoker {
    var undoCommands: List[Command] = List()
    var redoCommands: List[Command] = List()
    def execute(cmd: Command): Unit
    def undo(): Unit
    def redo(): Unit
}

object CommandInvoker {
    def create(): CommandInvokerImpl = {
        CommandInvokerImpl()
    }
}
