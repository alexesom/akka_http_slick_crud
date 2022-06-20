package repositories

import models.{User, UserTable}
import routes.implicits.RoutesImplicits
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

trait UserRepository extends RoutesImplicits {
  val userQuery = TableQuery[UserTable]
  val insertUser = userQuery returning userQuery.map(_.id)

  protected def insertUser(user: User): Future[Any] = {
    val action = userQuery
      .filter(u => u.email === user.email)
      .exists
      .result
      .flatMap { exists =>
        if (!exists) {
          insertUser += user
        } else {
          DBIO.successful(None)
        }
      }

    db.run(action)
  }

  protected def putUser(user: User, id: Long): Future[Long] = {
    db.run(
      userQuery.
        filter(_.id === id)
        .delete

        andThen

        (insertUser forceInsert user)
    )
  }

  protected def updateUser(user: User, id: Long): Future[Seq[User]] = {
    val query = userQuery
      .filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query
        .map(user => (user.firstName, user.lastName, user.dob, user.email, user.password))
        .update((user.firstName, user.lastName, user.dob, user.email, user.password))
    } yield results

    db.run(action)
  }

  protected def deleteUser(id: Long): Future[Seq[User]] = {
    val query = userQuery.filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query.delete
    } yield results

    db.run(action)
  }

  protected def getUserId(id: Long): Future[Seq[User]] = {
    db.run(userQuery.filter(_.id === id).result)
  }

  protected def getUserPage(page:Long, pageSize: Long): Future[Seq[User]] = {
    db.run(userQuery.drop((page - 1) * pageSize).take(pageSize).result)
  }
}
