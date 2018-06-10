package de.htwg.se.learn_duel.model.database

import slick.jdbc.H2Profile.api._

class PlayerDAO(tag: Tag) extends Table[(Int, String, Int, Int)](tag, "PLAYER") {
  def id = column[Int]("PLAYER_ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def points = column[Int]("POINTS")
  def resultId = column[Int]("RESULT_ID")
  def * = (id, name, points, resultId)

  def result = foreignKey("RESULT_FK", resultId, TableQuery[ResultDAO])(_.id)
}
