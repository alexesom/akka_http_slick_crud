package models

import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate

case class User(id: Long = 0L, firstName: String, lastName: String, dob: LocalDate, email: String, password: String)

class UserTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def firstName = column[String]("firstname")

  def lastName = column[String]("lastname")

  def dob = column[LocalDate]("dob")

  def email = column[String]("email")

  def password = column[String]("password")

  def * = (id, firstName, lastName, dob, email, password) <> (User.tupled, User.unapply)

  override def toString(): String = s"id: $id, firstName: $firstName, lastName: $lastName, dob: $dob, email: $email, password: $password"
}