package de.htwg.se.learn_duel.view.impl

import javafx.embed.swing.JFXPanel

import de.htwg.se.learn_duel.Observer
import de.htwg.se.learn_duel.controller.Controller
import de.htwg.se.learn_duel.view.UI

import scalafx.application.JFXApp

class GUI private (controller: Controller) extends JFXApp with UI with Observer {
    controller.addObserver(this)
    displayMenu

    override def displayMenu(): Unit = {
        this.stage = new MenuScreen
    }

    override def displayGame(): Unit = {
        //this.stage = new GameScreen
    }

    override def update: Unit = {}
}

object GUI {
    def create(controller: Controller): GUI = {
        val gui = new GUI(controller)
        gui.main(Array())
        gui
    }
}
