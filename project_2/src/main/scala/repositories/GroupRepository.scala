package repositories

import models.{Group, GroupTable}
import routes.implicits.RoutesImplicits
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

trait GroupRepository extends RoutesImplicits {
  val groupsQuery = TableQuery[GroupTable]
  val insertGroup = groupsQuery returning groupsQuery.map(_.id)

  protected def insertGroup(group: Group): Future[Any] = {
    val action = groupsQuery
      .filter(g => g.displayName === group.displayName)
      .exists
      .result
      .flatMap { exists =>
        if (!exists) {
          insertGroup += group
        } else {
          DBIO.successful(None)
        }
      }

    db.run(action)
  }

  protected def putGroup(group: Group, id: Long): Future[Long] = {
    db.run(
      groupsQuery.
        filter(_.id === id)
        .delete

        andThen

        (insertGroup forceInsert group)
    )
  }

  protected def updateGroup(group: Group, id: Long): Future[Seq[Group]] = {
    val query = groupsQuery
      .filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query
        .map(_.displayName)
        .update(group.displayName)
    } yield results

    db.run(action)
  }

  protected def deleteGroup(id: Long): Future[Seq[Group]] = {
    val query = groupsQuery.filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query.delete
    } yield results

    db.run(action)
  }

  protected def getGroupId(id: Long): Future[Seq[Group]] = {
    db.run(groupsQuery.filter(_.id === id).result)
  }

  protected def getGroupPage(page: Long, pageSize: Long): Future[Seq[Group]] = {
    db.run(groupsQuery.drop((page - 1) * pageSize).take(pageSize).result)
  }
}
