package de.htwg.se.learn_duel.model.database

import slick.jdbc.H2Profile.api._

class ResultDAO(tag: Tag) extends Table[(Int)](tag, "RESULT") {
    def id = column[Int]("RESULT_ID", O.PrimaryKey, O.AutoInc)
    def * = (id)
}
