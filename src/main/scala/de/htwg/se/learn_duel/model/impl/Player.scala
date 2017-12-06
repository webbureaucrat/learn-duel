package de.htwg.se.learn_duel.model.impl

import de.htwg.se.learn_duel.model.{ Player => PlayerTrait }

case class Player(name: String) extends PlayerTrait {
    if  (name.isEmpty) {
        // FIXME error: name can't be empty
    }

    override def toString:String = name
}

object Player {
    val baseName = "Player"
}
