package de.htwg.se.learn_duel.model.impl

import java.security.InvalidParameterException

import de.htwg.se.learn_duel.model.{Player => PlayerTrait, Question => QuestionTrait}

case class Player(
    name: String,
    var points: Int = 0,
    var correctAnswers: List[QuestionTrait] = List(),
    var wrongAnswers: List[QuestionTrait] = List()
) extends PlayerTrait {
    if  (name.isEmpty) {
        throw new InvalidParameterException("Player name cannot be empty")
    } else if (!name.matches("\\S+")) {
        throw new InvalidParameterException("Player name may not contain whitespaces")
    }

    override def toString: String = name
}
