package de.htwg.se.learn_duel.model.micro_service_impl

import java.security.InvalidParameterException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import de.htwg.se.learn_duel.model.{Player => PlayerTrait, Question => QuestionTrait}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

case class Player(
 name: String,
 var points: Int = 0,
 var correctAnswers: List[QuestionTrait] = List(),
 var wrongAnswers: List[QuestionTrait] = List()
) extends PlayerTrait {

  if (name.isEmpty) {
    throw new InvalidParameterException("Player name cannot be empty")
  } else if (!name.matches("\\S+")) {
    throw new InvalidParameterException(
      "Player name may not contain whitespaces")
  }

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  override def toString: String = {
    val responseFuture: Future[HttpResponse] = Http()
      .singleRequest(HttpRequest(uri = "http://localhost:8081/rest/v1/model/player/string"))

    val result: HttpResponse = Await.result(responseFuture, 5000 millis)

    if (result.status.isSuccess()) {
      Unmarshal(result.entity).to[String].map(response => {
        return response
      })
    }

    throw new IllegalStateException("Failed to get player name, reason: " + result.status.reason())
  }
}
