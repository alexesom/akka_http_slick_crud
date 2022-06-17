package models

import slick.jdbc.PostgresProfile.api._

case class UserGroup(id: Long = 0L, user_id: Long, group_id: Long)

class UserGroupTable(tag: Tag) extends Table[UserGroup](tag, "user_group") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def user_id = column[Long]("user_id")

  def group_id = column[Long]("group_id")

  def user_id_fk = foreignKey(
    "user_id_fk",
    column[Long]("user_id"),
    TableQuery[GroupTable]
  )(_.id)

  def group_id_fk = foreignKey(
    "group_id_fk",
    column[Long]("group_id"),
    TableQuery[UserTable]
  )(_.id)

  def * = (id, user_id, group_id) <> (UserGroup.tupled, UserGroup.unapply)
}