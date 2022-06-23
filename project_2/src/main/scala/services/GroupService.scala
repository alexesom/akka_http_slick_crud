package services

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{as, complete, entity, onSuccess}
import akka.http.scaladsl.server.Route
import akka.stream.alpakka.slick.scaladsl.SlickSession
import models.Group
import models.jsonSupport.GroupJsonProtocol
import repositories.GroupRepository
import routes.implicits.ActorsImplicits
import slick.jdbc.JdbcBackend.Database


trait GroupService extends GroupRepository with GroupJsonProtocol with ActorsImplicits {
  def getGroupInsertRoute(implicit db: Database, session: SlickSession): Route = {
    entity(as[Group]) { group =>
      onSuccess(insertGroup(group)) { responseId =>
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Post request complete! Group: $group, DB id: $responseId</h3>"
          )
        )
      }
    }
  }

  def getGroupPutRoute(id: Long)(implicit db: Database, session: SlickSession): Route = {
    entity(as[Group]) { group =>
      if (group.id != id) {
        complete(StatusCode.int2StatusCode(400))
      } else {

        onSuccess(putGroup(group, id)) { id =>
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Put request complete! Group: $group, DB id: $id</h3>"
          )
          )
        }
      }
    }
  }

  def getGroupUpdateRoute(id: Long)(implicit db: Database, session: SlickSession): Route = {
    entity(as[Group]) { group =>
      if (group.id != id) {
        complete(StatusCode.int2StatusCode(400))
      } else {
        onSuccess(updateGroup(group, id)) { response =>
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"<h3>Patch request complete! Group: $group, DB id: ${response}</h3>"
          )
          )
        }
      }
    }
  }

  def getGroupDeleteRoute(id: Long)(implicit db: Database, session: SlickSession): Route = {

    onSuccess(deleteGroup(id)) { response =>
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h3>Delete request complete! Group: $response</h3>"))
    }
  }

  def getGroupByIdRoute(id: Long)(implicit db: Database, session: SlickSession): Route = {
    onSuccess(getGroupId(id)) { resultGroup =>
      complete(
        HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          resultGroup
            .headOption
            .map(group => s"<h3>$group</h3>")
            .mkString("")
        )
      )
    }
  }

  def getGroupPageRoute(page: Int, pageSize: Int)(implicit db: Database, session: SlickSession): Route = {
    complete(
      getGroupPage(page, pageSize)
    )
  }
}
