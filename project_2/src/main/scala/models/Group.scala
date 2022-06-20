package models

import slick.jdbc.PostgresProfile.api._

case class Group(id: Long = 0L, displayName: String)

class GroupTable(tag: Tag) extends Table[Group](tag, "groups") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def displayName = column[String]("displayname")

  def * = (id, displayName) <> (Group.tupled, Group.unapply)
}