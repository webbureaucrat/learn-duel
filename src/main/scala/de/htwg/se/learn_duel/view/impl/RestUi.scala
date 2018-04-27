package de.htwg.se.learn_duel.view.impl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.htwg.se.learn_duel.controller.Controller

import scala.concurrent.{ExecutionContextExecutor, Future}

class RestUi(controller: Controller) {

  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route =
    get {
      pathSingleSlash {
        // FIXME #10 redirect to game
        complete(
          HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Learn Duel</h1>"))
      }
      pathPrefix("rest" / "v1") {
        pathEnd {
          // FIXME #10 give an overview over all API endpoint (json)
          /**
            * GET         /                                           Controller.game
            * GET         /rest/v1/help                               Controller.onHelp
            * GET         /rest/v1/game                               Controller.onStartGame
            * GET         /rest/v1/cleanState                         Controller.onReset
            * PUT         /rest/v1/player/name                        Controller.onAddPlayer(name)
            * DELETE      /rest/v1/player/name                        Controller.onRemovePlayer(name)
            * POST        /rest/v1/answer/answer                      Controller.onAnswerChosen(answer:Int)
            * GET         /rest/v1/maxPlayerCount                     Controller.getMaxPlayerCount
            */

          complete("")
        } ~
          path("help") {
            controller.onHelp()
            complete("")
          } ~
          path("game") {
            controller.onStartGame()
            complete("")
          } ~
          path("cleanState") {
            controller.reset()
            complete("")
          } ~
          path("maxPlayerCount") {
            complete(controller.maxPlayerCount.toString)
          }
      }
    } ~
      post {
        pathPrefix("rest" / "v1") {
          path("answer" / IntNumber) { id => {
            controller.onAnswerChosen(id)
            complete("")
          }
          }
        }
      } ~
      put {
        pathPrefix("rest" / "v1") {
          path("player") {
            controller.onAddPlayer(None)
            complete("")
          } ~
            path("player" / Segment) { name => {
              controller.onAddPlayer(Some(name))
              complete("")
            }
            }
        }
      } ~
      delete {
        pathPrefix("rest" / "v1") {
          path("player" / Segment) { name => {
            controller.onRemovePlayer(name)
            complete("")
          }
          }
        }
      }

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(route, "localhost", 8080)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
