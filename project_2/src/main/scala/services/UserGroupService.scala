package services

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{as, complete, entity, onSuccess}
import akka.http.scaladsl.server.Route
import akka.stream.alpakka.slick.scaladsl.SlickSession
import models.UserGroup
import models.jsonSupport.{UserGroupJsonProtocol, UserJsonProtocol}
import repositories.UserGroupRepository
import routes.implicits.ActorsImplicits
import slick.jdbc.JdbcBackend.Database

trait UserGroupService extends UserGroupRepository with UserGroupJsonProtocol with ActorsImplicits {
  def getUserGroupByIdRoute(id: Long)
                           (implicit db: Database, session: SlickSession): Route = {
    onSuccess(getUserGroupId(id)) { resultUserGroup =>
      complete(
        HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          resultUserGroup
            .headOption
            .map(userGroup => s"<h3>$userGroup</h3>")
            .mkString("")
        )
      )
    }
  }

  protected def getUserGroupPageRoute(page: Int, pageSize: Int)
                                     (implicit db: Database, session: SlickSession): Route = {
    complete(
      getUserGroupPage(page, pageSize)
    )
  }

  protected def getUserGroupInsertRoute(implicit db: Database, session: SlickSession): Route = {
    entity(as[UserGroup]) { userGroup =>
      onSuccess(insertUser(userGroup)) { responseId =>
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Post request complete! User group: $userGroup, DB id: $responseId</h3>"
          )
        )
      }
    }
  }

  protected def getUserGroupPutRoute(id: Long)(implicit db: Database, session: SlickSession): Route = {
    entity(as[UserGroup]) { userGroup =>
      if (userGroup.id != id) {
        complete(StatusCode.int2StatusCode(400))
      } else {
        onSuccess(putUserGroup(userGroup, id)) { id =>
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Put request complete! User group: $userGroup, DB id: $id</h3>"
          )
          )
        }
      }
    }
  }

  protected def getUserGroupUpdateRoute(id: Long)
                                       (implicit db: Database, session: SlickSession): Route = {
    entity(as[UserGroup]) { userGroup =>
      if (userGroup.id != id) {
        complete(StatusCode.int2StatusCode(400))
      } else {
        onSuccess(updateUserGroup(userGroup, id)) { response =>
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Patch request complete! User group: $userGroup, DB id: ${response.map(_.id).headOption.getOrElse(None)}</h3>"
          )
          )
        }
      }
    }
  }

  protected def getUserGroupDeleteRoute(id: Long)
                                       (implicit db: Database, session: SlickSession): Route = {
    onSuccess(deleteUserGroup(id)) { response =>
      complete(
        HttpEntity(ContentTypes.`text/html(UTF-8)`,
          s"<h3>Delete request complete! User group: ${response.headOption.getOrElse(None)}</h3>"
        )
      )
    }
  }
}
