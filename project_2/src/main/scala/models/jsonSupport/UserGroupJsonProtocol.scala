package models.jsonSupport

import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.UserGroup
import spray.json.DefaultJsonProtocol.{LongJsonFormat, jsonFormat3}


trait UserGroupJsonProtocol extends SprayJsonSupport with BaseJsonStreamingProtocol{
  implicit val userGroupFormat = jsonFormat3(UserGroup)
}