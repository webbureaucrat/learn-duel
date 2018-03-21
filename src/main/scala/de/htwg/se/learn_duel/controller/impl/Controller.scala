package de.htwg.se.learn_duel.controller.impl

import java.util.{Timer, TimerTask}

import com.google.inject.Inject
import de.htwg.se.learn_duel.controller.impl.exceptions._
import de.htwg.se.learn_duel.controller.{Controller => ControllerTrait}
import de.htwg.se.learn_duel.model.command.CommandInvoker
import de.htwg.se.learn_duel.model.command.impl.{PlayerAddCommand, PlayerRemoveCommand}
import de.htwg.se.learn_duel.model.{Game, Player, Question}
import de.htwg.se.learn_duel.{UpdateAction, UpdateData}

class Controller @Inject() (gameState: Game) extends ControllerTrait {
    protected var questionIter: Iterator[Question] = Iterator.empty
    protected var timer: Option[Timer] = None
    protected var lastUpdate: UpdateData = new UpdateData(UpdateAction.BEGIN, gameState)
    protected val invoker: CommandInvoker = CommandInvoker.create()

    override def requestUpdate(): Unit = {
        notifyObserversAndSaveUpdate(lastUpdate)
    }

    override def reset(): Unit = {
        gameState.reset()
        notifyObserversAndSaveUpdate(new UpdateData(UpdateAction.BEGIN, gameState))
    }

    override def getPlayerNames: List[String] = {
        gameState.players.map(p => p.name)
    }

    override def onAddPlayer(name: Option[String]) : Unit = {
        invoker.execute(PlayerAddCommand(name, addPlayer, removePlayer))
    }

    override def onRemovePlayer(name: String): Unit = {
        invoker.execute(PlayerRemoveCommand(name, removePlayer, addPlayer))
    }

    override def onPlayerActionUndo(): Unit = {
        invoker.undo()
    }

    override def onPlayerActionRedo(): Unit = {
        invoker.redo()
    }

    override def nextPlayerName(): Option[String] = {
        gameState.playerCount() match {
            case c if c < maxPlayerCount => Some(Player.baseName + (gameState.playerCount + 1).toString)
            case _ => None
        }
    }

    override def maxPlayerCount(): Int = Game.maxPlayerCount

    override def onHelp(): Unit = {
        notifyObserversAndSaveUpdate(new UpdateData(UpdateAction.SHOW_HELP, gameState))
    }

    override def onStartGame(): Unit = {
        resetQuestionIterator()

        if (questionIter.isEmpty) {
            throw new IllegalStateException("Can't start game without questions")
        }

        nextQuestion()
    }

    override def onClose(): Unit = {
        notifyObserversAndSaveUpdate(new UpdateData(UpdateAction.CLOSE_APPLICATION, gameState))
    }

    // scalastyle:off
    override def onAnswerChosen(input: Int): Unit = {
        val currentQuestion = gameState.currentQuestion.get
        val correctAnswer = currentQuestion.correctAnswer
        var (player: Option[Player], userInput: Int) = input match {
            case x if 0 until 5 contains x => (Some(gameState.players.head), input)
            case x if (6 until 10 contains x) && gameState.players.lengthCompare(1) > 0=> (Some(gameState.players(1)), input-5) // FIXME magic number -> local mp will be removed anyway
            case _ => (None, input)
        }

        if (player.isDefined && !playerAnsweredQuestion(player.get, currentQuestion.id)) {
            if (userInput == correctAnswer) {
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
            nextQuestion()
        }
    }
    // scalastyle:on

    protected def addPlayer(name: Option[String]): String = {
        var playerName = name match {
            case Some(n) => n
            case None => nextPlayerName().getOrElse("<unknown>") // will not be used if None
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

        notifyObserversAndSaveUpdate(new UpdateData(UpdateAction.PLAYER_UPDATE, gameState))

        playerName
    }

    protected  def removePlayer(name: String): Unit = {
        if (gameState.playerCount == 1) {
            throw NotEnoughPlayersException("There are not enough players to remove one")
        }

        gameState.players.find(p => p.name == name) match {
            case Some(p) =>
                gameState.removePlayer(p)
                notifyObserversAndSaveUpdate(new UpdateData(UpdateAction.PLAYER_UPDATE, gameState))
            case None => throw PlayerNotExistingException(s"Player '$name' does not exist")
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
        val localTimer = new Timer(true)
        timer = Some(localTimer)

        localTimer.scheduleAtFixedRate(new TimerTask {
            override def run(): Unit = {
                val newTime = gameState.currentQuestionTime match {
                    case Some(time) =>
                      val newTime = time - 1
                      if (newTime == 0) {
                          nextQuestion()
                          None
                      } else {
                          Some(newTime)
                      }
                    case None => None
                }

                newTime match {
                    case Some(time) =>
                      gameState.currentQuestionTime = newTime
                      notifyObserversAndSaveUpdate(new UpdateData(UpdateAction.TIMER_UPDATE, gameState))
                    case _ =>
                }
            }
        }, 1000, 1000)
    }

    protected def nextQuestion(): Unit = {
        stopTimer()

        // implicitely add not given answer as wrong answer
        if (gameState.currentQuestion.isDefined) {
            val currentQuestion = gameState.currentQuestion.get
            val noAnswerPlayers: List[Player] = gameState.players.filter(p => {
                !playerAnsweredQuestion(p, currentQuestion.id)
            })

            noAnswerPlayers.foreach(p => p.wrongAnswers = p.wrongAnswers :+ currentQuestion)
        }

        if (questionIter.hasNext) {
            showGame()
        } else {
            notifyObserversAndSaveUpdate(new UpdateData(UpdateAction.SHOW_RESULT, gameState))
        }
    }

    protected def showGame(): Unit = {
        val nextQuestion = questionIter.next()
        gameState.currentQuestion = Some(nextQuestion)
        gameState.currentQuestionTime = Some(nextQuestion.time)
        setUpTimer()

        notifyObserversAndSaveUpdate(new UpdateData(UpdateAction.SHOW_GAME, gameState))
    }

    protected def playerAnsweredQuestion(p: Player, questionId: Int): Boolean = {
        (p.correctAnswers ::: p.wrongAnswers).exists(q => {
            q.id == questionId
        })
    }

    protected def notifyObserversAndSaveUpdate(data: UpdateData): Unit = {
        lastUpdate = data
        notifyObservers(data)
    }
}
