package de.htwg.se.learn_duel.model.impl

import java.security.InvalidParameterException

import de.htwg.se.learn_duel.model.{Game => GameTrait, Player => PlayerTrait, Question => QuestionTrait}

// FIXME find better way for resetting or do not let all props be specified in constructor (currently needed for JSON reader)
case class Game (
       var helpText: List[String] = List(),
       var players: List[PlayerTrait] = List(),
       var questions: List[QuestionTrait] = List(),
       var currentQuestion: Option[QuestionTrait] = None,
       var currentQuestionTime: Option[Int] = None
) extends GameTrait {
    protected val initialQuestions: List[QuestionTrait] = questions
    reset()

    override def addPlayer(player: PlayerTrait): Unit = players = players :+ player

    override def removePlayer(player: PlayerTrait): Unit = players = players.filter(_ != player)

    override def playerCount(): Int = players.size

    override def addQuestion(question: QuestionTrait): Unit = questions = questions :+ question

    override def removeQuestion(question: QuestionTrait): Unit = questions = questions.filter(_ != question)

    override def questionCount(): Int = questions.size

    override def reset(): Unit = {
        questions = initialQuestions
        currentQuestion = None
        currentQuestionTime = None
        players = List()
        addPlayer(Player("Player1"))
        helpText = List(
            "Learn Duel is based on QuizDuel and works in a similar fashion, but with a new twist:\nYou play with questions based on your school or study assignments.",
            "For now, there is only local play, but online features will be added later.\nIf you are playing alone, the answers can be selected with the mouse or the keys 1-4.\nIn local multiplayer mode player 1 can specify his answer with the keys 1-4 and\nplayer 2 with the keys 6-9.",
            "Future features:",
            "* define your own questions",
            "* play with up to 3 friends online and compete against each other"
        )
    }
}


