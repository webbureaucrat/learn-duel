package de.htwg.se.learn_duel.model.micro_service_impl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContextExecutor, Future}

class PlayerHttpServer(player: Player) {
  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route =
    get {
      pathPrefix("rest" / "v1" / "model" / "player") {
        path("string") {
          complete(StatusCodes.OK -> player.name)
        }
      }
    }

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(route, "localhost", 8081)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}

