package de.htwg.se.learn_duel.controller.impl

import java.util.{Timer, TimerTask}

import de.htwg.se.learn_duel.controller.impl.exceptions._
import de.htwg.se.learn_duel.controller.{Controller => ControllerTrait}
import de.htwg.se.learn_duel.model.{Game, Player, Question}
import de.htwg.se.learn_duel.{UpdateAction, UpdateData}

class Controller(gameState: Game) extends ControllerTrait {
    protected var questionIter: Iterator[Question] = Iterator.empty
    protected var timer: Option[Timer] = None

    override def getCurrentPlayers: List[String] = {
        gameState.players.map(p => p.name)
    }

    // FIXME undo / redo pattern for player adding and removing
    override def addPlayer(name: Option[String]): Unit = {
        var playerName = name match {
            case Some(name) => name
            case None => nextPlayerName.getOrElse("<unknown>") // will not be used if None
        }

        if (gameState.playerCount == Game.maxPlayerCount) {
            throw TooManyPlayersException("There are too many players to add another one")
        }
        else if (gameState.players.exists(p => p.name == playerName)) {
            throw PlayerExistsException(s"'$playerName' already exists")
        }

        try {
            gameState.addPlayer(Player.create(playerName))
        } catch {
            case e: Throwable => throw ControllerProcedureFailed("Adding player failed: " + e.getMessage)
        }
        notifyObservers(new UpdateData(UpdateAction.PLAYER_UPDATE, gameState))
    }

    override def nextPlayerName: Option[String] = {
        gameState.playerCount match {
            case c if c < maxPlayerCount => Some(Player.baseName + (gameState.playerCount + 1).toString)
            case _ => None
        }
    }

    override def maxPlayerCount: Int = Game.maxPlayerCount

    override def removePlayer(name: String): Unit = {
        if (gameState.playerCount == 1) {
            throw NotEnoughPlayersException("There are not enough players to remove one")
        }

        gameState.players.find(p => p.name == name) match {
            case Some(p) =>
                gameState.removePlayer(p)
                notifyObservers(new UpdateData(UpdateAction.PLAYER_UPDATE, gameState))
            case None => throw PlayerNotExistingException(s"Player '$name' does not exist")
        }
    }

    override def onHelp(): Unit = {
        if (gameState.helpText.isEmpty) {
            import scala.io.Source
            val helpText: Iterator[String] = Source.fromResource("help.txt").getLines
            gameState.helpText = helpText.mkString("\n")
        }

        notifyObservers(new UpdateData(UpdateAction.SHOW_HELP, gameState))
    }

    override def onStartGame(): Unit = {
        resetQuestionIterator()
        if (!questionIter.hasNext) {
            throw new IllegalStateException("Can't start game without questions");
        }

        showGame()
    }

    override def onClose(): Unit = {
        notifyObservers(new UpdateData(UpdateAction.CLOSE_APPLICATION, gameState))
    }

    override def answerChosen(input: Int): Unit = {
        val currentQuestion = gameState.currentQuestion.get
        val correctAnswer = currentQuestion.correctAnswer
        val player = input match {
            case x if 0 until 5 contains x => Some(gameState.players(0))
            case x if (6 until 10 contains x) && (gameState.players.length > 1 )=> Some(gameState.players(1))
            case _ => None
        }

        if (player.isDefined && !playerAnsweredQuestion(player.get, currentQuestion.id)) {
            if (input == correctAnswer) {
                player.get.points += currentQuestion.points
                player.get.correctAnswers = player.get.correctAnswers :+ currentQuestion
            } else {
                player.get.wrongAnswers = player.get.wrongAnswers :+ currentQuestion
            }
        }

        // check if question was answered by all players
        val allAnswered = gameState.players.forall(p => {
            (p.correctAnswers ::: p.wrongAnswers).exists(q => {
                q.id == currentQuestion.id
            })
        })

        if (allAnswered) {
            nextQuestion
        }
    }

    protected def resetQuestionIterator(): Unit = {
        questionIter = gameState.questions.iterator
    }

    protected def stopTimer(): Unit = {
        if (timer.isDefined) {
            timer.get.cancel()
        }
    }

    protected def setUpTimer(): Unit = {
        timer = Some(new Timer(true))
        val localTimer = timer.get
        localTimer.scheduleAtFixedRate(new TimerTask {
            override def run(): Unit = {
                gameState.currentQuestionTime = gameState.currentQuestionTime match {
                    case Some(time) => {
                        val newTime = time - 1
                        if (newTime == 0) {
                            nextQuestion
                            None
                        } else {
                            Some(newTime)
                        }
                    }
                    case None => None
                }

                gameState.currentQuestionTime match {
                    case Some(time) if time % 5 == 0 => {
                        notifyObservers(new UpdateData(UpdateAction.UPDATE_TIMER, gameState))
                    }
                    case None => stopTimer
                    case _ =>
                }
            }
        }, 1000, 1000)
    }

    protected def nextQuestion(): Unit = {
        // implicitely add not given answer as wrong answer
        val currentQuestion = gameState.currentQuestion.get
        val noAnswerPlayers: List[Player] = gameState.players.filter(p => {
            !playerAnsweredQuestion(p, currentQuestion.id)
        })

        noAnswerPlayers.foreach(p => p.wrongAnswers = p.wrongAnswers :+ currentQuestion)

        if (questionIter.hasNext) {
            showGame()
        } else {
            stopTimer()
            notifyObservers(new UpdateData(UpdateAction.SHOW_RESULT, gameState))
        }
    }

    protected def showGame(): Unit = {
        stopTimer()

        val nextQuestion = questionIter.next()
        gameState.currentQuestion = Some(nextQuestion)
        gameState.currentQuestionTime = Some(nextQuestion.time)
        setUpTimer()

        notifyObservers((new UpdateData(UpdateAction.SHOW_GAME, gameState)))
    }

    protected def playerAnsweredQuestion(p: Player, questionId: Int): Boolean = {
        (p.correctAnswers ::: p.wrongAnswers).exists(q => {
            q.id == questionId
        })
    }
}
