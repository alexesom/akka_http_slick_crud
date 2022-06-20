package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.jsonSupport.UserJsonProtocol
import routes.implicits.RoutesImplicits
import services.UserService

import scala.language.postfixOps


trait UserRoute extends UserService with UserJsonProtocol with RoutesImplicits {

  lazy val usersRoute: Route =
    concat(
      getUsersRoute,
      postUsersRoute,
      putUsersRoute,
      patchUsersRoute,
      deleteUsersRoute
    )

  private def getUsersRoute: Route = {
    get {
      concat(
        parameters("page".as[Int], "pageSize".withDefault(100)) { (page, pageSize) =>
          getUserPageRoute(page, pageSize)
        },
        path(LongNumber) { id =>
          getUserByIdRoute(id)
        },
        pathEndOrSingleSlash {
          getUserPageRoute(1, 100)
        }
      )
    }
  }

  private def postUsersRoute: Route = {
    post {
      pathEndOrSingleSlash(getUserInsertRoute)
    }
  }

  private def putUsersRoute: Route = {
    put {
      path(LongNumber)(getUserPutRoute)
    }
  }

  private def patchUsersRoute: Route = {
    patch {
      path(LongNumber)(getUserUpdateRoute)
    }
  }

  private def deleteUsersRoute: Route = {
    delete {
      path(LongNumber)(getUserDeleteRoute)
    }
  }
}