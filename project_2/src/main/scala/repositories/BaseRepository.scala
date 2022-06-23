package repositories

import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import models.{BaseTable, Group, GroupTable, UserTable}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.{ResultSetConcurrency, ResultSetType}
import slick.lifted.{AbstractTable, TableQuery}

import scala.concurrent.Future
import akka.stream.alpakka.slick.javadsl.SlickSession
import akka.stream.alpakka.slick.scaladsl.Slick
import akka.stream.scaladsl.{Flow, Source}
import akka.stream.scaladsl.JavaFlowSupport.Source
import routes.implicits.ActorsImplicits
import slick.basic.DatabasePublisher
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.{ResultSetConcurrency, ResultSetType}
import slick.lifted.TableQuery

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


//TODO
/*
trait BaseRepository[A <: BaseTable, E <: Table[A]] extends ActorsImplicits {
  val query = TableQuery[E]

  protected def insert(element: A)(implicit db: Database): Future[Long] = {
    Try {
      query += element
    } match {
      case Success(value) => db.run(value).map(_.toLong)
      case Failure(exception) => Future.failed(exception)
    }
  }

  protected def replace(element: A, id: Long)(implicit db: Database): Future[Long] = {
    db.run(
      query
        .filter(_.id === id)
        .delete

        andThen

        (insertGroup forceInsert group)
    )
  }

  protected def updateGroup(group: Group, id: Long)(implicit db: Database): Future[Seq[Group]] = {
    val query = groupsQuery
      .filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query
        .map(_.displayName)
        .update(group.displayName)
    } yield results


    val databasePublisher = db.stream(
      action
        .asInstanceOf[DBIOAction[Group, Streaming[Group], Effect.All]]
        .transactionally
        .withStatementParameters(
          rsType = ResultSetType.ForwardOnly,
          rsConcurrency = ResultSetConcurrency.ReadOnly,
          fetchSize = 1000
        )
    )


    implicit val session = SlickSession.forConfig("database")

    Slick.source(query.result)(session)
  }

  protected def deleteGroup(id: Long)(implicit db: Database): Future[Seq[Group]] = {
    val query = groupsQuery.filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query.delete
    } yield results

    db.run(action)
  }

  protected def getGroupId(id: Long)(implicit db: Database): Future[Seq[Group]] = {
    db.run(groupsQuery.filter(_.id === id).result)
  }

  protected def getGroupPage(page: Long, pageSize: Long)(implicit db: Database): Future[Seq[Group]] = {
    db.run(groupsQuery.drop((page - 1) * pageSize).take(pageSize).result)
  }
}
*/
