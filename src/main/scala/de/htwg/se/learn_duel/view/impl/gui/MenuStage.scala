package de.htwg.se.learn_duel.view.impl.gui

import javafx.event.{ActionEvent, EventHandler}

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text

class MenuStage(
   newGameAction: EventHandler[ActionEvent],
   helpAction: EventHandler[ActionEvent]
) extends PrimaryStage {
    title.value = "Learn Duel Menu"
    resizable = false
    width = 480
    height = 640

    scene = new Scene {
        fill = White
        root = new VBox {
            children += new Text("Learn Duel")

            val newGameButton = new Button {
                text = "New Game"
                onAction = newGameAction
            }
            children += newGameButton


            val helpButton = new Button {
                text = "?"
                onAction = helpAction
            }

            children += helpButton
        }

    }
}
