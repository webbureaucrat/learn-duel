package de.htwg.se.learn_duel.view.impl.gui

import javafx.event.{ActionEvent, EventHandler}

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Text, TextAlignment}

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
    height = 540

    scene = new Scene {
        fill = White
        stylesheets += "styles.css"

        root = new VBox {
            styleClass += "menu"

            val headLine = new Text {
                text = "Learn Duel"
                styleClass += "headline"
            }
            children += headLine

            val newGameButton = new Button {
                text = "New Game"
                onAction = newGameAction
                styleClass += "play-button"
            }
            children += newGameButton

            val playerContainer = new VBox {
                styleClass += "player-rows-container"

                for (playerName <- playerInfo._1) {
                    val hBox = new HBox {
                        styleClass += "player-container"

                        val txtFld = new TextField {
                            text = playerName
                            editable = false
                            styleClass += "player-textfield"
                            styleClass += "player-textfield-non-editable"
                        }
                        children += txtFld

                        val removeBtn = new Button {
                            text = "Remove"
                            onAction = _ => playerRemoveAction(playerName)
                            styleClass += "add-remove-buttons"
                        }
                        children += removeBtn
                    }
                    children += hBox
                }

                playerInfo._2 match {
                    case Some(nextPlayername) => {
                        val hBox = new HBox {
                            styleClass += "player-container"

                            val txtField = new TextField {
                                promptText = nextPlayername
                                styleClass += "player-textfield"
                            }
                            children += txtField

                            val addBtn = new Button {
                                text = "Add"
                                onAction = _ => playerAddAction(
                                    if (txtField.getText.isEmpty) {
                                        txtField.getPromptText
                                    } else {
                                        txtField.getText
                                    }
                                )
                                styleClass += "add-remove-buttons"
                            }
                            children += addBtn
                        }
                        children += hBox
                    }
                    case _ =>
                }
            }
            children += playerContainer

            val helpButton = new Button {
                text = "Help"
                onAction = helpAction
                styleClass += "help-button"
            }

            children += helpButton
        }
    }
}
