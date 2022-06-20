package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import routes.implicits.RoutesImplicits
import services.GroupService

import scala.language.postfixOps


trait GroupRoute extends GroupService with RoutesImplicits {

  lazy val groupsRoute: Route =
    concat(
      getGroupsRoute,
      postGroupsRoute,
      putGroupsRoute,
      patchGroupsRoute,
      deleteGroupsRoute
    )

  private def getGroupsRoute: Route = {
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

  private def postGroupsRoute: Route = {
    post {
      pathEndOrSingleSlash(getGroupInsertRoute)
    }
  }

  private def putGroupsRoute: Route = {
    put {
      path(LongNumber)(getGroupPutRoute)
    }
  }

  private def patchGroupsRoute: Route = {
    patch {
      path(LongNumber)(getGroupUpdateRoute)
    }
  }

  private def deleteGroupsRoute: Route = {
    delete {
      path(LongNumber)(getGroupDeleteRoute)
    }
  }
}