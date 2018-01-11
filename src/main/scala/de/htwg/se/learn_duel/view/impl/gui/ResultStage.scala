package de.htwg.se.learn_duel.view.impl.gui

import de.htwg.se.learn_duel.model.Player

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.control.Button
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text

class ResultStage(
    players: List[Player],
    backAction: Function0[Unit]
) extends PrimaryStage {
    title.value = "Learn Duel Result"
    width = 480
    height = 640

    scene = new Scene {
        fill = White
        root = new VBox {
            players.foreach { p => {
                children.addAll(
                    new Text("Player '" + p.name + "': "),
                    new Text("Points: " + p.points),
                    new Text("Correct answers:")
                )

                p.correctAnswers.foreach(q => {
                    children.add(new Text("\t" + q.text))
                })

                children.add(new Text("Wrong answers:"))

                p.wrongAnswers.foreach(q => {
                    val correctAnswer = q.answers.find(a => a.id == q.correctAnswer).get
                    children.addAll(
                        new Text("\t" + q.text),
                        new Text("\tcorrect answer is: " + correctAnswer.text)
                    )
                })
            }}

            val player = players.max[Player]{ case (p1: Player, p2: Player) => {
                p1.points.compareTo(p2.points)
            }}

            children.add(new Text("'" + player.name + "' won the game!"))

            val backButton = new Button {
                text = "Back"
                onAction = backAction
            }
            children += backButton
        }
    }
}
