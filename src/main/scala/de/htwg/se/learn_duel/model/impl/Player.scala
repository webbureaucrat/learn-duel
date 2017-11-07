package de.htwg.se.learn_duel.model.impl

import de.htwg.se.learn_duel.model.{ Player => PlayerTrait }

case class Player(name: String) extends PlayerTrait {
   override def toString:String = name
}

