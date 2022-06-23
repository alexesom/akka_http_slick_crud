package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.alpakka.slick.scaladsl.SlickSession
import routes.implicits.ActorsImplicits
import services.GroupService
import slick.jdbc.JdbcBackend.Database

import scala.language.postfixOps


trait GroupRoute extends GroupService with ActorsImplicits {

  def groupsRoute(implicit db: Database, session: SlickSession): Route =
    concat(
      getGroupsRoute,
      postGroupsRoute,
      putGroupsRoute,
      patchGroupsRoute,
      deleteGroupsRoute
    )

  private def getGroupsRoute(implicit db: Database, session: SlickSession): Route = {
    get {
      concat(
        parameters("page".as[Int], "pageSize".withDefault(100)) { (page, pageSize) =>
          getGroupPageRoute(page, pageSize)
        },
        path(LongNumber) { id =>
          getGroupByIdRoute(id)
        },
        pathEndOrSingleSlash {
          getGroupPageRoute(1, 100)
        }
      )
    }
  }

  private def postGroupsRoute(implicit db: Database, session: SlickSession): Route = {
    post {
      pathEndOrSingleSlash(getGroupInsertRoute)
    }
  }

  private def putGroupsRoute(implicit db: Database, session: SlickSession): Route = {
    put {
      path(LongNumber)(getGroupPutRoute)
    }
  }

  private def patchGroupsRoute(implicit db: Database, session: SlickSession): Route = {
    patch {
      path(LongNumber)(getGroupUpdateRoute)
    }
  }

  private def deleteGroupsRoute(implicit db: Database, session: SlickSession): Route = {
    delete {
      path(LongNumber)(getGroupDeleteRoute)
    }
  }
}