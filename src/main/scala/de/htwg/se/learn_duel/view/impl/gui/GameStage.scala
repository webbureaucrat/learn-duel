package de.htwg.se.learn_duel.view.impl.gui

import java.util.{Timer, TimerTask}
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.KeyCode

import de.htwg.se.learn_duel.model.Question

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text

class GameStage(
    question: Question,
    allowMouseInput: Boolean,
    onInput: Function[Int, Unit]
) extends PrimaryStage {
    var timeRemaining: Int = question.time
    var timerText: SimpleStringProperty = new SimpleStringProperty {
        "Time remaining: " + timeRemaining + "s"
    }
    var timer: Option[Timer] = None

    title.value = "Learn Duel Game"
    width = 640
    height = allowMouseInput match {
        case true => 480
        case false => 520
    }

    scene = new Scene {
        fill = White
        stylesheets += "styles.css"

        root = new VBox {
            styleClass += "game"

            val questionProp: Text = new Text {
                text = question.text
                styleClass += "headline"
            }
            children += questionProp

            val answerBox: VBox = new VBox {
                styleClass += "answer-container"

                question.answers.zipWithIndex.foreach { case (ans, i) =>
                    val btn = new Button {
                        styleClass += "answer-button"

                        text = "Answer " + (i + 1) + ": " + ans.text
                        if (allowMouseInput) {
                            onAction = _ => onInput(ans.id)
                        }
                    }

                    children += btn
                }
            }
            children += answerBox

            val timer: Text = new Text {
                styleClass += "remaining-time"
            }
            timer.text.bind(timerText)
            children += timer

            if (!allowMouseInput) {
                val warning = new Text {
                    text = "Mouse input not allowed, use keyboard instead"
                    fill = Color.Red
                }

                children += warning
            }
        }

        onKeyReleased = { e => {
            e.getCode match {
                case KeyCode.DIGIT1 => onInput(1)
                case KeyCode.DIGIT2 => onInput(2)
                case KeyCode.DIGIT3 => onInput(3)
                case KeyCode.DIGIT4 => onInput(4)
                case KeyCode.DIGIT6 => onInput(6)
                case KeyCode.DIGIT7 => onInput(7)
                case KeyCode.DIGIT8 => onInput(8)
                case KeyCode.DIGIT9 => onInput(9)
                case _ =>
            }
        }}
    }

    setUpTimer()

    def updateTime(timeRemaining: Int): Unit = {
        if (timer.isDefined) {
            timer.get.cancel()
        }

        this.timeRemaining = timeRemaining
        setUpTimer()
    }

    protected def setUpTimer(): Unit = {
        timer = Some(new Timer(true))
        val localTimer = timer.get

        localTimer.scheduleAtFixedRate(new TimerTask{
            override def run(): Unit = {
                timerText.set("Time remaining: " + timeRemaining + "s")
                timeRemaining -= 1
                if (timeRemaining < 0) {
                    localTimer.cancel()
                }
            }
        }, 0, 1000)
    }
}
