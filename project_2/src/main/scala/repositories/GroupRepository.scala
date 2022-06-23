package repositories

import akka.NotUsed
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Sink, Source}
import models.{Group, GroupTable}
import routes.implicits.ActorsImplicits
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.{ResultSetConcurrency, ResultSetType}
import slick.lifted.TableQuery

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait GroupRepository extends ActorsImplicits {

  val groupsQuery = TableQuery[GroupTable]
  val insertGroup = groupsQuery returning groupsQuery.map(_.id)

  protected def insertGroup(group: Group)(implicit db: Database, session: SlickSession): Future[Long] = {
    Try { insertGroup += group } match {
      case Success(value) => db.run(value)
      case Failure(exception) => Future.failed(exception)
    }
  }

  protected def putGroup(group: Group, id: Long)(implicit db: Database, session: SlickSession): Future[Long] = {
    db.run(
      groupsQuery.
        filter(_.id === id)
        .delete

        andThen

        (insertGroup forceInsert group)
    )
  }

  protected def updateGroup(group: Group, id: Long)
                           (implicit db: Database, session: SlickSession): Future[Int] = {
    val query = groupsQuery
      .filter(_.id === id)
      .insertOrUpdate(group)

    db.run(query)
  }

  protected def deleteGroup(id: Long)(implicit db: Database, session: SlickSession): Future[Seq[Group]] = {
    val query = groupsQuery.filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query.delete
    } yield results

    db.run(action)
  }

  protected def getGroupId(id: Long)(implicit db: Database, session: SlickSession): Future[Seq[Group]] = {
    db.run(groupsQuery.filter(_.id === id).result)
  }

  protected def getGroupPage(page: Long, pageSize: Long)(implicit db: Database, session: SlickSession): Source[Group, NotUsed] = {
    val action = groupsQuery
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
