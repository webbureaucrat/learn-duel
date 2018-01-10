package de.htwg.se.learn_duel.model.command.impl

import de.htwg.se.learn_duel.model.command.Command

case class PlayerAddCommand(
    name: Option[String],
    addPlayer: Function[Option[String], String],
    removePlayer: Function[String, Unit]
) extends Command {
    var actualName: String = ""

    override def execute(): Unit = {
        actualName = addPlayer(name)
    }

    override def undo(): Unit = {
        removePlayer(actualName)
    }

    override def redo(): Unit = {
        execute()
    }
}
