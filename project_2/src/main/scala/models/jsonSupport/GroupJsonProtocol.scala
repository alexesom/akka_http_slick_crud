package models.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.Group
import spray.json.DefaultJsonProtocol.{LongJsonFormat, StringJsonFormat, jsonFormat2}

trait GroupJsonProtocol extends SprayJsonSupport {
  implicit val groupFormat = jsonFormat2(Group)
}
