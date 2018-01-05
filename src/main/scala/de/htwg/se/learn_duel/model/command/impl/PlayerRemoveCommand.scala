package de.htwg.se.learn_duel.model.command.impl

import de.htwg.se.learn_duel.controller.Controller
import de.htwg.se.learn_duel.model.command.Command

case class PlayerRemoveCommand(
    name: String,
    remvoePlayer: Function[String, Unit],
    addPlayer: Function[Option[String], String]
) extends Command {
    override def execute(): Unit = {
        remvoePlayer(name)
    }

    override def undo(): Unit = {
        addPlayer(Some(name))
    }

    override def redo(): Unit = {
        execute()
    }
}
