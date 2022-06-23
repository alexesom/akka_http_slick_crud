package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.alpakka.slick.scaladsl.SlickSession
import models.jsonSupport.UserJsonProtocol
import routes.implicits.ActorsImplicits
import services.UserService
import slick.jdbc.JdbcBackend.Database

import scala.language.postfixOps


trait UserRoute extends UserService with UserJsonProtocol with ActorsImplicits {

  def usersRoute(implicit db: Database, session: SlickSession): Route =
    concat(
      getUsersRoute,
      postUsersRoute,
      putUsersRoute,
      patchUsersRoute,
      deleteUsersRoute
    )

  private def getUsersRoute(implicit db: Database, session: SlickSession): Route = {
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

  private def postUsersRoute(implicit db: Database, session: SlickSession): Route = {
    post {
      pathEndOrSingleSlash(getUserInsertRoute)
    }
  }

  private def putUsersRoute(implicit db: Database, session: SlickSession): Route = {
    put {
      path(LongNumber)(getUserPutRoute)
    }
  }

  private def patchUsersRoute(implicit db: Database, session: SlickSession): Route = {
    patch {
      path(LongNumber)(getUserUpdateRoute)
    }
  }

  private def deleteUsersRoute(implicit db: Database, session: SlickSession): Route = {
    delete {
      path(LongNumber)(getUserDeleteRoute)
    }
  }
}