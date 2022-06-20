package models.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.UserGroup
import spray.json.DefaultJsonProtocol.{jsonFormat3, LongJsonFormat}


trait UserGroupJsonProtocol extends SprayJsonSupport {
  implicit val userGroupFormat = jsonFormat3(UserGroup)
}