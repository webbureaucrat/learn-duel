package de.htwg.se.learn_duel.model.impl

import de.htwg.se.learn_duel.model.{Answer => AnswerTrait, Question => QuestionTrait}

// needed for json (de)serialization
case class Question(
   id: Int,
   text: String,
   points: Int,
   answers: List[AnswerTrait],
   correctAnswer: Int,
   time: Int
) extends QuestionTrait {}
