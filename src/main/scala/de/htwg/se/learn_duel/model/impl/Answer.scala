package de.htwg.se.learn_duel.model.impl

import de.htwg.se.learn_duel.model.{Answer => AnswerTrait}

// needed for json (de)serialization
case class Answer(id: Int, text: String) extends AnswerTrait {}
