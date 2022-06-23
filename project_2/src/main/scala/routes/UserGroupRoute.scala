package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.alpakka.slick.scaladsl.SlickSession
import models.jsonSupport.UserGroupJsonProtocol
import routes.implicits.ActorsImplicits
import services.UserGroupService
import slick.jdbc.JdbcBackend.Database

import scala.language.postfixOps

trait UserGroupRoute extends UserGroupService with UserGroupJsonProtocol with ActorsImplicits {

  def userGroupRoute(implicit db: Database, session: SlickSession): Route =
    concat(
      getUserGroupRoute,
      postUserGroupRoute,
      putUserGroupRoute,
      patchUserGroupRoute,
      deleteUserGroupRoute
    )

  private def getUserGroupRoute(implicit db: Database, session: SlickSession): Route = {
    get {
      concat(
        parameters("page".as[Int], "pageSize".withDefault(100)) { (page, pageSize) =>
          getUserGroupPageRoute(page, pageSize)
        },
        path(LongNumber) { id =>
          getUserGroupByIdRoute(id)
        },
        pathEndOrSingleSlash {
          getUserGroupPageRoute(1, 100)
        }
      )
    }
  }

  private def postUserGroupRoute(implicit db: Database, session: SlickSession): Route = {
    post {
      pathEndOrSingleSlash(getUserGroupInsertRoute)
    }
  }

  private def putUserGroupRoute(implicit db: Database, session: SlickSession): Route = {
    put {
      path(LongNumber)(getUserGroupPutRoute)
    }
  }

  private def patchUserGroupRoute(implicit db: Database, session: SlickSession): Route = {
    patch {
      path(LongNumber)(getUserGroupUpdateRoute)
    }
  }

  private def deleteUserGroupRoute(implicit db: Database, session: SlickSession): Route = {
    delete {
      path(LongNumber)(getUserGroupDeleteRoute)
    }
  }
}
