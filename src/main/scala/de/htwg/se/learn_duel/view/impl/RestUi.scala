package de.htwg.se.learn_duel.view.impl

import akka.pattern.ask
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.learn_duel.GuiceModule
import de.htwg.se.learn_duel.controller.ControllerActorFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.Random

class RestUi @Inject()(injector: Injector, implicit val system: ActorSystem) {

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val controllerActorFactory = injector.getInstance(classOf[ControllerActorFactory])
  implicit val timeout: Timeout = Timeout(5.seconds) // used for akka ask pattern

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

          complete (
            HttpEntity(ContentTypes.`text/html(UTF-8)`, "<p>Path overview not yet implemented</p>")
          )
        } ~
          path("help") {
            extractClientIP { ip =>
              val future = controllerActorFactory.getInstance(ip.toString()).ask("onHelp")
              onSuccess(future) ( res =>
                complete(
                  HttpEntity(ContentTypes.`application/json`, res.toString)
                )
              )
            }
          } ~
          path("game") {
            extractClientIP { ip =>
              val future = controllerActorFactory.getInstance(ip.toString()).ask("onStartGame")
              onSuccess(future) ( res =>
                complete(
                  HttpEntity(ContentTypes.`application/json`, res.toString)
                )
              )
            }
          } ~
          path("cleanState") {
            extractClientIP { ip =>
              val future = controllerActorFactory.getInstance(ip.toString()).ask("reset")
              onSuccess(future) ( res =>
                complete(
                  HttpEntity(ContentTypes.`application/json`, res.toString)
                )
              )
            }
          } ~
          path("maxPlayerCount") {
            extractClientIP { ip =>
              val future = controllerActorFactory.getInstance(ip.toString()).ask("maxPlayerCount")
              onSuccess(future) ( res =>
                complete(
                  HttpEntity(ContentTypes.`application/json`, res.toString)
                )
              )
            }
          }
      }
    } ~
      post {
        pathPrefix("rest" / "v1") {
          path("answer" / IntNumber) { id => {
            extractClientIP { ip =>
              val future = controllerActorFactory.getInstance(ip.toString()).ask(("onAnswerChosen", id))
              onSuccess(future) ( res =>
                complete(
                  HttpEntity(ContentTypes.`application/json`, res.toString)
                )
              )
            }
          }
          }
        }
      } ~
      put {
        pathPrefix("rest" / "v1") {
          path("player") {
            extractClientIP { ip =>
              val future = controllerActorFactory.getInstance(ip.toString()).ask("onAddPlayer")
              onSuccess(future) ( res =>
                complete(
                  HttpEntity(ContentTypes.`application/json`, res.toString)
                )
              )
            }
          } ~
            path("player" / Segment) { name => {
              extractClientIP { ip =>
                val future = controllerActorFactory.getInstance(ip.toString()).ask(("onAddPlayer", name))
                onSuccess(future) ( res =>
                  complete(
                    HttpEntity(ContentTypes.`application/json`, res.toString)
                  )
                )
              }
            }
            }
        }
      } ~
      delete {
        pathPrefix("rest" / "v1") {
          path("player" / Segment) { name => {
            extractClientIP { ip =>
              val future = controllerActorFactory.getInstance(ip.toString()).ask(("onRemovePlayer", name))
              onSuccess(future) ( res =>
                complete(
                  HttpEntity(ContentTypes.`application/json`, res.toString)
                )
              )
            }
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
