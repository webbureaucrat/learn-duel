package de.htwg.se.learn_duel.view.impl.gui

import java.util.concurrent.CountDownLatch
import javafx.application.Platform

import de.htwg.se.learn_duel.{Observer, UpdateAction, UpdateData}
import de.htwg.se.learn_duel.controller.Controller
import de.htwg.se.learn_duel.view.UI

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.control.{Button, ButtonType, Dialog}
import scalafx.stage.Modality

class GUI private (controller: Controller, latch: CountDownLatch) extends JFXApp with UI with Observer {
    controller.addObserver(this)
    displayMenu()
    this.stage.onCloseRequest = {(_) =>
        controller.onClose
    }

    // signal initialization down
    latch.countDown()

    override def displayMenu(): Unit = {

        this.stage = new MenuStage(
            - => {
                controller.onStartGame
            },
            _ => {
                controller.onHelp
        })
    }

    override def displayGame(): Unit = {
        this.stage = new GameStage
    }

    override def update(updateParam: UpdateData): Unit = {
        // every update needs to be run on the JavaFX Application thread
        Platform.runLater {() =>
            updateParam.getAction() match {
                case UpdateAction.CLOSE_APPLICATION => this.stage.close()
                case UpdateAction.SHOW_HELP => {
                    val helpText = updateParam.getState() match {
                        case Some(gameState) => gameState.helpText
                        case None => "No help available."
                    }
                    val dlg = new InfoPopup("Learn Duel Help", helpText)
                    dlg.show
                }
                case _ =>
            }
        }
    }
}

object GUI {
    def create(controller: Controller): Unit = {
        val latch = new CountDownLatch(1)
        val gui = new GUI(controller, latch)

        // run GUI on its own thread
        Future {
            gui.main(Array())
        }

        // wait for initialization of JFXApp to be done
        latch.await()
    }
}
