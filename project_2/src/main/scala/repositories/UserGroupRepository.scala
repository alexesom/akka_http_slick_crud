package repositories

import models.{UserGroup, UserGroupTable}
import routes.implicits.RoutesImplicits
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

trait UserGroupRepository extends RoutesImplicits {
  val userGroupQuery = TableQuery[UserGroupTable]
  val insertUserGroup = userGroupQuery returning userGroupQuery.map(_.id)

  protected def getUserGroupId(id: Long): Future[Seq[UserGroup]] = {
    db.run(userGroupQuery.filter(_.id === id).result)
  }

  protected def getUserGroupPage(page: Long, pageSize: Long): Future[Seq[UserGroup]] = {
    db.run(userGroupQuery.drop((page - 1) * pageSize).take(pageSize).result)
  }

  protected def insertUser(userGroup: UserGroup): Future[Any] = {
    val action = userGroupQuery
      .filter(ug => (ug.user_id === userGroup.user_id) && (ug.group_id === userGroup.group_id))
      .exists
      .result
      .flatMap { exists =>
        if (!exists) {
          insertUserGroup +=  userGroup
        } else {
          DBIO.successful(None)
        }
      }

    db.run(action)
  }

  protected def putUserGroup(userGroup: UserGroup, id: Long): Future[Long] = {
    db.run(
      userGroupQuery.
        filter(_.id === id)
        .delete

        andThen

        (insertUserGroup forceInsert userGroup)
    )
  }

  protected def updateUserGroup(userGroup: UserGroup, id: Long): Future[Seq[UserGroup]] = {
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

  protected def deleteUserGroup(id: Long): Future[Seq[UserGroup]] = {
    val query = userGroupQuery.filter(_.id === id)

    val action = for {
      results <- query.result
      _ <- query.delete
    } yield results


    db.run(action)
  }
}
