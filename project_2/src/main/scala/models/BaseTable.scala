package models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

//TODO
/*
class BaseTable[T <: BaseModel](tag: Tag)(tableTag: Tag, tableName: String) extends Table[T](tableTag, tableName){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  override def * = (id)
}
*/
