package de.htwg.se.learn_duel.view.impl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import de.htwg.se.learn_duel.controller.Controller

import scala.concurrent.{ExecutionContextExecutor, Future}

class RestUi(controller: Controller) {

  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route: Route =
    get {
      pathPrefix("rest") {
        pathEnd {
          usage()
        } ~
          pathSingleSlash {
            usage()
          } ~
          path("v1" / "help") {
            controller.onHelp()
            complete(StatusCodes.OK)
          } ~
          path("v1" / "game") {
            controller.onStartGame()
            complete(StatusCodes.OK)
          } ~
          path("v1" / "cleanState") {
            controller.reset()
            complete(StatusCodes.OK)
          } ~
          path("v1" / "maxPlayerCount") {
            complete(controller.maxPlayerCount.toString)
          }
      }
    } ~
      post {
        pathPrefix("rest") {
          path("v1" / "answer" / IntNumber) { id => {
            controller.onAnswerChosen(id)
            complete(StatusCodes.OK)
          }
          }
        }
      } ~
      put {
        pathPrefix("rest") {
          path("v1" / "player") {
            controller.onAddPlayer(None)
            complete(StatusCodes.OK)
          } ~
            path("v1" / "player" / Segment) { name => {
              controller.onAddPlayer(Some(name))
              complete(StatusCodes.OK)
            }
            }
        }
      } ~
      delete {
        pathPrefix("rest") {
          path("v1" / "player" / Segment) { name => {
            controller.onRemovePlayer(name)
            complete(StatusCodes.OK)
          }
          }
        }
      }

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(route, "localhost", 8080)

  def usage(): StandardRoute = {
    lazy val message =
      """Learn Duel rest API
      GET     /rest                    Display this usage
      GET     /rest/v1/help            Controller.onHelp
      GET     /rest/v1/game            Controller.onStartGame
      GET     /rest/v1/cleanState      Controller.onReset
      PUT     /rest/v1/player/name     Controller.onAddPlayer(name)
      DELETE  /rest/v1/player/name     Controller.onRemovePlayer(name)
      POST    /rest/v1/answer/answer   Controller.onAnswerChosen(answer:Int)
      GET     /rest/v1/maxPlayerCount  Controller.getMaxPlayerCount"""

    complete(
      HttpEntity(ContentTypes.`text/plain(UTF-8)`, message))
  }

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
