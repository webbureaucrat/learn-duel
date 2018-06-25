package de.htwg.se.learn_duel.view.impl

import akka.pattern.ask
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.google.inject.{Inject, Injector}
import de.htwg.se.learn_duel.controller.ControllerActorFactory

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}

class RestUi @Inject()(injector: Injector, implicit val system: ActorSystem) {

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val controllerActorFactory = injector.getInstance(classOf[ControllerActorFactory])
  implicit private val timeout: Timeout = Timeout(5.seconds) // used for akka ask pattern

  val route: Route =
    get {
      pathSingleSlash {
        // FIXME #10 redirect to game
        complete(
          HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Learn Duel</h1>")
        )
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

          complete(
            HttpEntity(ContentTypes.`text/html(UTF-8)`, "<p>Path overview not yet implemented</p>")
          )
        } ~
        path("register") {
          val id = java.util.UUID.randomUUID().toString
          controllerActorFactory.createInstance(id)
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, id))
        } ~
        pathPrefix(Segment) { id =>
          path("help") {
            val actor = controllerActorFactory.getInstance(id)
            actor match {
              case Some(controller) => onComplete(controller.ask("onHelp")) (res =>
                complete(HttpEntity(ContentTypes.`application/json`, res.toString))
              )
              case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
            }
          } ~
          path("start") {
            val actor = controllerActorFactory.getInstance(id)
            actor match {
              case Some(controller) => onComplete(controller.ask("onStartGame")) (res =>
                complete(HttpEntity(ContentTypes.`application/json`, res.toString))
              )
              case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
            }
          } ~
          path("game") {
            val actor = controllerActorFactory.getInstance(id)
            actor match {
              case Some(controller) => onComplete(controller.ask("onGetGame")) (res =>
                complete(HttpEntity(ContentTypes.`application/json`, res.toString))
              )
              case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
            }
          } ~
          path("cleanState") {
            val actor = controllerActorFactory.getInstance(id)
            actor match {
              case Some(controller) => onComplete(controller.ask("reset")) (res =>
                complete(HttpEntity(ContentTypes.`application/json`, res.toString))
              )
              case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
            }
          } ~
          path("maxPlayerCount") {
            val actor = controllerActorFactory.getInstance(id)
            actor match {
              case Some(controller) => onComplete(controller.ask("maxPlayerCount")) (res =>
                complete(HttpEntity(ContentTypes.`application/json`, res.toString))
              )
              case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
            }
          }
        }
      }
    } ~
    post {
      pathPrefix("rest" / "v1" / Segment) { id =>
        path("answer" / IntNumber) { answerId => {
          val actor = controllerActorFactory.getInstance(id)
          actor match {
            case Some(controller) => onComplete(controller.ask(("onAnswerChosen", answerId))) (res =>
              complete(HttpEntity(ContentTypes.`application/json`, res.toString))
            )
            case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
          }
        }}
      }
    } ~
    put {
      pathPrefix("rest" / "v1" / Segment) { id =>
        path("player") {
          val actor = controllerActorFactory.getInstance(id)
          actor match {
            case Some(controller) => onComplete(controller.ask("onAddPlayer")) (res =>
              complete(HttpEntity(ContentTypes.`application/json`, res.toString))
            )
            case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
          }
        } ~
        path("player" / Segment) { name => {
          val actor = controllerActorFactory.getInstance(id)
          actor match {
            case Some(controller) => onComplete(controller.ask(("onAddPlayer", name))) (res =>
              complete(HttpEntity(ContentTypes.`application/json`, res.toString))
            )
            case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
          }
        }}
      }
    } ~
    delete {
      pathPrefix("rest" / "v1" / Segment) { id =>
        path("player" / Segment) { name => {
          val actor = controllerActorFactory.getInstance(id)
          actor match {
            case Some(controller) => onComplete(controller.ask(("onRemovePlayer", name)))(res =>
              complete(HttpEntity(ContentTypes.`application/json`, res.toString))
            )
            case None => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ID not found"))
          }
        }}
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
