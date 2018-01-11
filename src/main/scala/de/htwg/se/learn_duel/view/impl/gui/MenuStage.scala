package de.htwg.se.learn_duel.view.impl.gui

import javafx.event.{ActionEvent, EventHandler}

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text

class MenuStage(
   newGameAction: EventHandler[ActionEvent],
   helpAction: EventHandler[ActionEvent],
   playerInfo: (List[String], Option[String]),
   playerAddAction: Function[String, Unit],
   playerRemoveAction: Function[String, Unit]
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

            for (playerName <- playerInfo._1) {
                val hBox = new HBox {
                    val txtFld = new TextField {
                        text = playerName
                        editable = false
                    }
                    children += txtFld

                    val removeBtn = new Button {
                        text = "-"
                        onAction = _ => playerRemoveAction(playerName)
                    }
                    children += removeBtn
                }
                children += hBox
            }

            playerInfo._2 match {
                case Some(nextPlayername) => {
                    val hBox = new HBox {
                        val txtField = new TextField {
                            promptText = nextPlayername
                        }
                        children += txtField

                        val addBtn = new Button {
                            text = "+"
                            onAction = _ => playerAddAction(
                                if (txtField.getText.isEmpty) {
                                    txtField.getPromptText
                                } else {
                                    txtField.getText
                                }
                            )
                        }
                        children += addBtn
                    }
                    children += hBox
                }
                case _ =>
            }

            val helpButton = new Button {
                text = "?"
                onAction = helpAction
            }

            children += helpButton
        }

    }
}
