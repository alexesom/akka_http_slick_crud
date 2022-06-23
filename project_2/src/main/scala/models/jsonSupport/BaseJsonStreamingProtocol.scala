package models.jsonSupport

import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}

trait BaseJsonStreamingProtocol {
  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()
}
