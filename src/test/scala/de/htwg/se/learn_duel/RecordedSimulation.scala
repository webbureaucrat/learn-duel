package de.htwg.se.learn_duel

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:8080")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.9,en-US;q=0.8,de;q=0.7")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val uri1 = "http://localhost:8080/rest/v1"

  val gameStatus = http("request_game_status")
    .get("/rest/v1/${session_id}/game")
    .headers(headers_0)

	val scn: ScenarioBuilder = scenario("RecordedSimulation")
		.exec(http("request_register")
			.get("/rest/v1/register")
			.headers(headers_0)
      .check(status.in(200))
      .check(bodyString.saveAs("session_id")))
    .exec(http("request_add_player")
      .put("/rest/v1/${session_id}/player/Player2")
      .headers(headers_0))
    .exec(http("request_remove_player")
      .delete("/rest/v1/${session_id}/player/Player2")
      .headers(headers_0))
    .exec(http("request_start")
      .get("/rest/v1/${session_id}/start")
      .headers(headers_0))
    .exec(gameStatus)
    .exec(http("request_first_answer")
      .post("/rest/v1/${session_id}/answer/1")
      .headers(headers_0))
    .exec(http("request_second_answer")
      .post("/rest/v1/${session_id}/answer/3")
      .headers(headers_0))
    .exec(gameStatus)

	setUp(scn.inject(constantUsersPerSec(200) during (15 seconds) randomized).protocols(httpProtocol))
}