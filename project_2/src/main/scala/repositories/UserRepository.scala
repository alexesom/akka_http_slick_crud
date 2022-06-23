package repositories

import akka.NotUsed
import akka.stream.alpakka.slick.scaladsl.{Slick, SlickSession}
import akka.stream.scaladsl.Source
import models.{User, UserTable}
import routes.implicits.ActorsImplicits
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.{ResultSetConcurrency, ResultSetType}
import slick.lifted.TableQuery

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait UserRepository extends ActorsImplicits {

  val userQuery = TableQuery[UserTable]
  val insertUser = userQuery returning userQuery.map(_.id)

  protected def insertUser(user: User)
                          (implicit db: Database, session: SlickSession): Future[Long] = {
    Try {
      insertUser += user
    } match {
      case Success(value) => db.run(value)
      case Failure(exception) => Future.failed(exception)
    }
  }

  protected def putUser(user: User, id: Long)
                       (implicit db: Database, session: SlickSession): Future[Long] = {
    db.run(
      userQuery.
        filter(_.id === id)
        .delete

        andThen

        (insertUser forceInsert user)
    )
  }

  protected def updateUser(user: User, id: Long)
                          (implicit db: Database, session: SlickSession): Future[Int] = {
    val query = userQuery
      .filter(_.id === id)
      .insertOrUpdate(user)

    db.run(query)
  }

  protected def deleteUser(id: Long)
                          (implicit db: Database, session: SlickSession): Future[Seq[User]] = {
    val query = userQuery.filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query.delete
    } yield results

    db.run(action)
  }

  protected def getUserId(id: Long)
                         (implicit db: Database, session: SlickSession): Future[Seq[User]] = {
    db.run(userQuery.filter(_.id === id).result)
  }

  protected def getUserPage(page: Long, pageSize: Long)
                           (implicit db: Database, session: SlickSession): Source[User, NotUsed] = {
    val action = userQuery
      .drop((page - 1) * pageSize)
      .take(pageSize)
      .result
      .transactionally
      .withStatementParameters(
        rsType = ResultSetType.ForwardOnly,
        rsConcurrency = ResultSetConcurrency.ReadOnly,
        fetchSize = 1000
      )

    Slick
      .source(action)
  }
}
