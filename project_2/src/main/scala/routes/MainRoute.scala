package routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import routes.implicits.RoutesImplicits
import slick.jdbc.JdbcBackend.Database

trait MainRoute extends GroupRoute with UserRoute with UserGroupRoute with RoutesImplicits {

  override val db = Database.forConfig("database")

  lazy val routes = {
    concat(
      pathSingleSlash(getApiTraces),
      pathPrefix(("users" / "groups") | ("groups" / "users"))(userGroupRoute),
      pathPrefix("users")(usersRoute),
      pathPrefix("groups")(groupsRoute),
    )
  }

  private def getApiTraces: Route = {
    //TODO

    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>/</h1>"))
    }
  }
}
