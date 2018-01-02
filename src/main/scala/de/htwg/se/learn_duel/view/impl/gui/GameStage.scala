package de.htwg.se.learn_duel.view.impl.gui

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle

class GameStage extends PrimaryStage {
    title.value = "Game"
    width = 480
    height = 640
    scene = new Scene {
        fill = LightGreen
        content = new Rectangle {
            x = 25
            y = 40
            width = 100
            height = 100
            fill <== when(hover) choose Yellow otherwise Blue
        }
    }
}
