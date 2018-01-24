package de.htwg.se.learn_duel.view.impl.gui

import de.htwg.se.learn_duel.model.Player

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.control.Button
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text

class ResultStage(
    players: List[Player],
    backAction: Function0[Unit]
) extends PrimaryStage {
    title.value = "Learn Duel Result"
    width = 480
    height = players.length match {
        case 1 => 560
        case _ => 720
    }

    scene = new Scene {
        fill = White
        stylesheets += "styles.css"

        root = new VBox {
            styleClass += "result"

            val headline = new Text {
                text = "Result"
                styleClass += "headline"
            }
            children += headline

            val playerContainer = new VBox {
                styleClass += "player-results"

                players.foreach { p => {
                    val singlePlayerContainer = new VBox {
                        styleClass += "player-result"

                        val player = new Text {
                            text = p.name
                            styleClass += "player-name"
                        }
                        children += player

                        val points = new Text {
                            text = "Points: " + p.points
                            styleClass += "player-points"
                        }
                        children += points

                        val correctContainer = new VBox {
                            styleClass += "correct-container"

                            if (p.correctAnswers.length > 0) {
                                val correctText = new Text {
                                    text = "Correct answers"
                                    styleClass += "correct-text"
                                }
                                children += correctText

                                p.correctAnswers.foreach(q => {
                                    val correctQuestion = new Text {
                                        text = q.text
                                        styleClass += "correct-answer"
                                    }
                                    children += correctQuestion
                                })
                            }
                        }
                        children += correctContainer

                        val wrongContainer = new VBox {
                            styleClass += "wrong-container"

                            if (p.wrongAnswers.length > 0) {
                                val wrongText = new Text {
                                    text = "Wrong answers"
                                    styleClass += "wrong-text"
                                }
                                children += wrongText

                                p.wrongAnswers.foreach(q => {
                                    val correctAnswer = q.answers.find(a => a.id == q.correctAnswer).get
                                    val answerText = new Text {
                                        text = q.text + " (correct answer: " + correctAnswer.text + ")"
                                        styleClass += "wrong-answer"
                                    }
                                    children += answerText
                                })
                            }
                        }
                        children += wrongContainer
                    }
                    children += singlePlayerContainer
                }}
            }
            children += playerContainer

            val player = players.max[Player]{ case (p1: Player, p2: Player) => {
                p1.points.compareTo(p2.points)
            }}

            val winner = new Text {
                text = "'" + player.name + "' won the game!"
                styleClass += "winner"
            }
            children += winner

            val backButton = new Button {
                text = "Back"
                onAction = backAction
                styleClass += "back-button"
            }
            children += backButton
        }
    }
}
