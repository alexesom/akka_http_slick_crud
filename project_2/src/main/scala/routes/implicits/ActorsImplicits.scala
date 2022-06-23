package routes.implicits

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

trait ActorsImplicits {
  implicit val system = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext = system.executionContext
}
