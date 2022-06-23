import akka.http.scaladsl.Http
import akka.stream.alpakka.slick.javadsl.SlickSession
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import routes.MainRoute
import slick.jdbc.JdbcBackend.Database

import scala.io.StdIn
import scala.language.postfixOps


object Main extends MainRoute with StrictLogging {
  implicit val session = SlickSession.forConfig("database-slick")
  implicit val db = Database.forConfig("database")

  def main(args: Array[String]): Unit = {
    logger.debug("Starting loading config")
    val config = ConfigFactory.load()
    logger.debug("Config loaded successfully")

    logger.debug(s"Start parsing host and port variables from config")
    val host = config.getString("application.host")
    val port = config.getInt("application.port")
    logger.debug(s"Parsing host and port variables from config ended successfully")

    val server = Http().newServerAt(host, port).bind(routes)

    StdIn.readLine()
    server
      .flatMap(_.unbind())
      .onComplete(_ => {
        system.terminate()
        session.close()
      })
  }
}