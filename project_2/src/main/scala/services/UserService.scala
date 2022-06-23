package services

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, onSuccess}
import akka.http.scaladsl.server.Route
import akka.stream.alpakka.slick.scaladsl.SlickSession
import models.User
import models.jsonSupport.UserJsonProtocol
import repositories.UserRepository
import routes.implicits.ActorsImplicits
import slick.jdbc.JdbcBackend.Database

import scala.util.{Failure, Success}


trait UserService extends UserRepository with UserJsonProtocol with ActorsImplicits {

  def getUserInsertRoute(implicit db: Database, session: SlickSession): Route = {
    entity(as[User]) { user =>
      onComplete(insertUser(user)) {
        case Success(responseId) =>
          complete(
            HttpEntity(
              ContentTypes.`text/html(UTF-8)`,
              s"<h3>Post request complete! User: $user, DB id: $responseId</h3>"
            )
          )

        case Failure(exception) =>
          complete(
            HttpEntity(
              ContentTypes.`text/html(UTF-8)`,
              s"<h3>Cannot insert user because already exists</h3>"
            )
          )
      }
    }
  }

  def getUserPutRoute(id: Long)
                     (implicit db: Database, session: SlickSession): Route = {
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

  def getUserUpdateRoute(id: Long)
                        (implicit db: Database, session: SlickSession): Route = {
    entity(as[User]) { user =>
      if (user.id != id) {
        complete(StatusCode.int2StatusCode(400))
      } else {
        onSuccess(updateUser(user, id)) { response =>
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Patch request complete! User: $user, DB id: $response</h3>"
          )
          )
        }
      }
    }
  }

  def getUserDeleteRoute(id: Long)
                        (implicit db: Database, session: SlickSession): Route = {
    onSuccess(deleteUser(id)) { response =>
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h3>Delete request complete! User: $response</h3>"))
    }
  }

  def getUserByIdRoute(id: Long)
                      (implicit db: Database, session: SlickSession): Route = {
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

  def getUserPageRoute(page: Int, pageSize: Int)
                      (implicit db: Database, session: SlickSession): Route = {
    complete(
      getUserPage(page, pageSize)
    )

  }
}
