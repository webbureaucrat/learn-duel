package de.htwg.se.learn_duel.model.database

import slick.jdbc.H2Profile.api._

class QuestionDAO(tag: Tag) extends Table[(Int, String, Boolean, Int)](tag, "QUESTION") {
  def id = column[Int]("QUESTION_ID", O.PrimaryKey, O.AutoInc)
  def text = column[String]("NAME")
  def correctlyAnswered = column[Boolean]("CORRECTLY_ANSWERED")
  def playerId = column[Int]("PLAYER_ID")
  def * = (id, text, correctlyAnswered, playerId)

  def result = foreignKey("PLAYER_FK", playerId, TableQuery[PlayerDAO])(_.id)
}
