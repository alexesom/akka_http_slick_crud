package routes.implicits

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import slick.jdbc.JdbcBackend.Database

trait RoutesImplicits {
  implicit val system = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext = system.executionContext

  val db = Database.forConfig("database")
}
