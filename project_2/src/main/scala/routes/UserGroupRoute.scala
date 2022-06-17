package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.jsonSupport.UserGroupJsonProtocol
import routes.implicits.RoutesImplicits
import services.UserGroupService

import scala.language.postfixOps

trait UserGroupRoute extends UserGroupService with UserGroupJsonProtocol with RoutesImplicits {

  lazy val userGroupRoute: Route =
    concat(
      getUserGroupRoute,
      postUserGroupRoute,
      putUserGroupRoute,
      patchUserGroupRoute,
      deleteUserGroupRoute
    )

  private def getUserGroupRoute: Route = {
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

  private def postUserGroupRoute: Route = {
    post {
      pathEndOrSingleSlash(getUserGroupInsertRoute)
    }
  }

  private def putUserGroupRoute: Route = {
    put {
      path(LongNumber)(getUserGroupPutRoute)
    }
  }

  private def patchUserGroupRoute: Route = {
    patch {
      path(LongNumber)(getUserGroupUpdateRoute)
    }
  }

  private def deleteUserGroupRoute: Route = {
    delete {
      path(LongNumber)(getUserGroupDeleteRoute)
    }
  }
}
