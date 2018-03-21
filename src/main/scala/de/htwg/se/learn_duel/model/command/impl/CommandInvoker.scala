package de.htwg.se.learn_duel.model.command.impl

import de.htwg.se.learn_duel.model.command.{Command, CommandInvoker => CommandInvokerTrait}

case class CommandInvoker() extends CommandInvokerTrait {
    override def execute(cmd: Command): Unit = {
        cmd.execute()
        undoCommands = undoCommands :+ cmd
        redoCommands = List()
    }

    override def undo(): Unit = {
        if (undoCommands.nonEmpty) {
            val lastCommand = undoCommands.last
            lastCommand.undo()
            redoCommands = redoCommands :+ lastCommand
            undoCommands = undoCommands diff List(lastCommand)
        }
    }

    override def redo(): Unit = {
        if (redoCommands.nonEmpty) {
            redoCommands.head.redo()
            undoCommands = undoCommands :+ redoCommands.head
            redoCommands = redoCommands diff List(redoCommands.head)
        }
    }
}
