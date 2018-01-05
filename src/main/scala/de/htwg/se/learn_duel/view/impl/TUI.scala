package de.htwg.se.learn_duel.view.impl

import java.io.BufferedReader
import java.util.{Timer, TimerTask}

import com.typesafe.scalalogging.LazyLogging
import de.htwg.se.learn_duel.{Observer, UpdateAction, UpdateData}
import de.htwg.se.learn_duel.controller.{Controller, ControllerException}
import de.htwg.se.learn_duel.model.{Player, Question}
import de.htwg.se.learn_duel.view.UI

class TUI (controller: Controller) extends UI with Observer with LazyLogging {
    controller.addObserver(this)
    var stopProcessingInput = false
    var inMenu = true
    var inGame = false
    displayMenu

    def displayPlayers(): Unit = {
        logger.info("Current players: " + controller.getPlayerNames.mkString(", "))
    }

    override def displayMenu(): Unit = {
        logger.info("")
        logger.info("Welcome to Learn Duel")
        displayPlayers()
        logger.info("n => new game")
        logger.info("a [name] => add player")
        logger.info("r [name] => remove player")
        logger.info("h => show help")
        logger.info("q => exit")
        logger.info("")
    }

    override def displayGame(question: Question, multiplayer: Boolean): Unit = {
        logger.info(question.text)
        question.answers.zipWithIndex.foreach {case (ans, i) => {
            logger.info((i + 1) + "/" + (i + 5) + ": " + ans.text)
        }}
    }

    override def displayResult(players: List[Player]): Unit = {
        logger.info("")
        logger.info("RESULT:")
        players.foreach(p => {
            logger.info("Player '" + p.name + "':")
            logger.info("Points: " + p.points)
            logger.info("Correct answers:")
            p.correctAnswers.foreach(q => {
                logger.info("\t" + q.text)
            })

            logger.info("Wrong answers:")
            p.wrongAnswers.foreach(q => {
                logger.info("\t" + q.text)
                val correctAnswer = q.answers.find(a => a.id == q.correctAnswer).get
                logger.info("\tcorrect answer is: " + correctAnswer.text)
            })
        })

        val player = players.max[Player]{ case (p1: Player, p2: Player) => {
            p1.points.compareTo(p2.points)
        }}

        logger.info("")
        logger.info("'" + player.name + "' won the game!")
        logger.info("")
    }

    // scalastyle:off
    override def update(updateParam: UpdateData): Unit = {
        updateParam.getAction() match {
            case UpdateAction.CLOSE_APPLICATION => stopProcessingInput = true
            case UpdateAction.SHOW_HELP => {
                logger.info(updateParam.getState().helpText)
            }
            case UpdateAction.PLAYER_UPDATE => displayPlayers
            case UpdateAction.SHOW_GAME => {
                displayGamePretty(
                    updateParam.getState().currentQuestion.get,
                    updateParam.getState().players.length > 1,
                    updateParam.getState().currentQuestionTime.get
                )
                inMenu = false;
                inGame = true;
            }
            case UpdateAction.UPDATE_TIMER => {
                displayGamePretty(
                    updateParam.getState().currentQuestion.get,
                    updateParam.getState().players.length > 1,
                    updateParam.getState().currentQuestionTime.get
                )
            }
            case UpdateAction.SHOW_RESULT => {
                displayResult(updateParam.getState().players)
            }
            case _ =>
        }
    }

    def processInput(input: BufferedReader): Unit = {
        while (!stopProcessingInput) {
            if (input.ready()) {
                val line = input.readLine()
                if (inMenu){
                    processMenuInput(line)
                } else if (inGame) {
                    processGameInput(line)
                } else {
                    processResultInput(line)
                }
            } else {
                Thread.sleep(200) // don't waste cpu cycles if no input is given
            }
        }
    }
    protected def processMenuInput(line: String): Unit = {
        val playerPattern = """(?:a|r)(?:\s+(.*))?""".r
        try {
            line match {
                case "q" => controller.onClose
                case "n" => controller.onStartGame
                case playerPattern(name) if line.startsWith("a") => controller.onAddPlayer(Option(name))
                case playerPattern(name) if line.startsWith("r") => if (name != null) {
                    controller.onRemovePlayer(name)
                }
                case "h" => controller.onHelp
                case "u" => controller.onPlayerActionUndo
                case "U" => controller.onPlayerActionRedo
                case _ => {
                    logger.info("Unknown command")
                    displayMenu
                }
            }
        } catch {
            case e: ControllerException => logger.error(e.getMessage)
        }
    }

    protected def processGameInput(line: String): Unit = {
        line match {
            case "q" => controller.onClose
            case "1" => controller.onAnswerChosen(1)
            case "2" => controller.onAnswerChosen(2)
            case "3" => controller.onAnswerChosen(3)
            case "4" => controller.onAnswerChosen(4)
            case "6" => controller.onAnswerChosen(6)
            case "7" => controller.onAnswerChosen(7)
            case "8" => controller.onAnswerChosen(8)
            case "9" => controller.onAnswerChosen(9)
            case _ => logger.info("Unknown command")
        }
    }

    protected def processResultInput(line: String): Unit = {
        line match {
            case "q" => controller.onClose
            case _ => logger.info("Unknown command")
        }
    }
    // scalastyle:on

    protected def displayGamePretty(question: Question, multiplayer: Boolean, timeRemaining: Int): Unit = {
        logger.info("")
        displayGame(question, multiplayer)
        logger.info("Time remaining: " + timeRemaining + "s")
        logger.info("")
    }

}
