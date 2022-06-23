package repositories

import akka.NotUsed
import akka.stream.alpakka.slick.scaladsl.{Slick, SlickSession}
import akka.stream.scaladsl.Source
import models.{UserGroup, UserGroupTable}
import routes.implicits.ActorsImplicits
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.{ResultSetConcurrency, ResultSetType}
import slick.lifted.TableQuery

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait UserGroupRepository extends ActorsImplicits {

  val userGroupQuery = TableQuery[UserGroupTable]
  val insertUserGroup = userGroupQuery returning userGroupQuery.map(_.id)

  protected def getUserGroupId(id: Long)
                              (implicit db: Database, session: SlickSession): Future[Seq[UserGroup]] = {
    db.run(userGroupQuery.filter(_.id === id).result)
  }

  protected def getUserGroupPage(page: Long, pageSize: Long)
                                (implicit db: Database, session: SlickSession): Source[UserGroup, NotUsed] = {
    val action = userGroupQuery
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

  protected def insertUser(userGroup: UserGroup)
                          (implicit db: Database, session: SlickSession): Future[Any] = {
    Try {
      insertUserGroup += userGroup
    } match {
      case Success(value) => db.run(value)
      case Failure(exception) => Future.failed(exception)
    }
  }

  protected def putUserGroup(userGroup: UserGroup, id: Long)
                            (implicit db: Database, session: SlickSession): Future[Long] = {
    db.run(
      userGroupQuery
        .filter(_.id === id)
        .delete

        andThen

        (insertUserGroup forceInsert userGroup)
    )
  }

  protected def updateUserGroup(userGroup: UserGroup, id: Long)
                               (implicit db: Database, session: SlickSession): Future[Seq[UserGroup]] = {
    val query = userGroupQuery
      .filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query
        .map(filteredUserGroup => (filteredUserGroup.user_id, filteredUserGroup.group_id))
        .update((userGroup.user_id, userGroup.group_id))
    } yield results

    db.run(action)
  }

  protected def deleteUserGroup(id: Long)
                               (implicit db: Database, session: SlickSession): Future[Seq[UserGroup]] = {
    val query = userGroupQuery.filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query.delete
    } yield results


    db.run(action)
  }
}
