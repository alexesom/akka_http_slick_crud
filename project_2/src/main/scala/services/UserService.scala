package services

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{as, complete, entity, onSuccess}
import akka.http.scaladsl.server.Route
import models.User
import models.jsonSupport.UserJsonProtocol
import repositories.UserRepository
import routes.implicits.RoutesImplicits


trait UserService extends UserRepository with UserJsonProtocol with RoutesImplicits {

  def getUserInsertRoute: Route = {
    entity(as[User]) { user =>
      onSuccess(insertUser(user)) { responseId =>
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Post request complete! User: $user, DB id: $responseId</h3>"
          )
        )
      }
    }
  }

  def getUserPutRoute(id: Long): Route = {
    entity(as[User]) { user =>
      if (user.id != id) {
        complete(StatusCode.int2StatusCode(400))
      } else {
        onSuccess(putUser(user, id)) { id =>
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Put request complete! User: $user, DB id: $id</h3>"
          )
          )
        }
      }
    }
  }

  def getUserUpdateRoute(id: Long): Route = {
    entity(as[User]) { user =>
      if (user.id != id) {
        complete(StatusCode.int2StatusCode(400))
      } else {
        onSuccess(updateUser(user, id)) { response =>
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Patch request complete! User: $user, DB id: ${response.map(_.id).headOption.getOrElse(None)}</h3>"
          )
          )
        }
      }
    }
  }

  def getUserDeleteRoute(id: Long): Route = {
    onSuccess(deleteUser(id)) { response =>
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h3>Delete request complete! User: $response</h3>"))
    }
  }

  def getUserByIdRoute(id: Long): Route = {
    onSuccess(getUserId(id)) { resultUser =>
      complete(
        HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          resultUser
            .headOption
            .map(user => s"<h3>$user</h3>")
            .mkString("")
        )
      )
    }
  }

  def getUserPageRoute(page: Int, pageSize: Int): Route = {
    onSuccess(getUserPage(page, pageSize)) { page =>
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
        page
          .map(user => s"<h3>$user</h3>")
          .mkString("")
      )
      )
    }
  }
}
