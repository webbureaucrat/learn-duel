package de.htwg.se.learn_duel.model.impl

import java.security.InvalidParameterException

import de.htwg.se.learn_duel.model.{Player => PlayerTrait}

case class Player(name: String) extends PlayerTrait {
    if  (name.isEmpty) {
        throw new InvalidParameterException("Player name cannot be empty")
    } else if (!name.matches("\\S+")) {
        throw new InvalidParameterException("Player name may not contain whitespaces")
    }

    override def toString: String = name
}

object Player {
    val baseName = "Player"
}
