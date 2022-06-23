package models.jsonSupport

import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.Group
import spray.json.DefaultJsonProtocol.{LongJsonFormat, StringJsonFormat, jsonFormat2}

trait GroupJsonProtocol extends SprayJsonSupport with BaseJsonStreamingProtocol{
  implicit val groupFormat = jsonFormat2(Group)
}
