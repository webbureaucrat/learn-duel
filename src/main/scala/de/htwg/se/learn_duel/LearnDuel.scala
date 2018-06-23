package de.htwg.se.learn_duel

import java.io.BufferedReader

import com.google.inject.Guice
import de.htwg.se.learn_duel.view.impl.RestUi

import scala.io.StdIn

object LearnDuel {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new GuiceModule())
    val restUi = injector.getInstance(classOf[RestUi])

    StdIn.readLine()
    println("exiting...")
    restUi.unbind()
    println("done")
  }
}
