package models.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.User
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat, RootJsonFormat}

import java.time.LocalDate

trait BaseUserJsonProtocol extends DefaultJsonProtocol {
  implicit val localDateFormat: JsonFormat[LocalDate] = new JsonFormat[LocalDate] {
    override def write(obj: LocalDate): JsValue = JsString(obj.toString)

    override def read(json: JsValue): LocalDate = json match {
      case date: JsString => LocalDate.parse(date.value)
      case _ =>
        throw new IllegalArgumentException(
          s"Can not parse json value [$json] to a local date object")
    }
  }
}

trait UserJsonProtocol extends SprayJsonSupport with BaseUserJsonProtocol {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat6(User)
}