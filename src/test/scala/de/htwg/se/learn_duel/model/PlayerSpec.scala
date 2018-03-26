package de.htwg.se.learn_duel.model

import java.security.InvalidParameterException

import de.htwg.se.learn_duel.model.impl.{Player => PlayerImpl}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.Json

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {
  "A Player" when {
    "new" should {
      val player = PlayerImpl("YourName")
      "have a name" in {
        player.name should be("YourName")
      }
      "have a nice String representation" in {
        player.toString should be("YourName")
      }
      "have no points" in {
        player.points should be(0)
      }
      "have no correct answers" in {
        player.correctAnswers.length should be(0)
      }
      "have no wrong answers" in {
        player.wrongAnswers.length should be(0)
      }
    }

    "constructed by factory method" should {
      "be the same as constructing it directly" in {
        val player = Player.create("YourName")
        player.name should be("YourName")
        player.toString should be("YourName")
        player.points should be(0)
        player.correctAnswers.length should be(0)
        player.wrongAnswers.length should be(0)
      }
    }

    "constructed without name" should {
      "not accept emtpy names" in {
        assertThrows[InvalidParameterException] {
          val player = PlayerImpl("")
        }
      }
    }

    "constructed with a name with withspace" should {
      "not accept names with whitespace" in {
        assertThrows[InvalidParameterException] {
          val player = PlayerImpl("Your Name")
        }
      }
    }
    "serialized to JSON" should {
      "be correct" in {
        val player = PlayerImpl("YourName")

        val jsonValue = Json.parse(
          "{ \"name\": \"YourName\", \"points\": 0, \"correctAnswers\": [], \"wrongAnswers\": [] }")

        Json.toJson(player) should be(jsonValue)
      }
    }
  }
}
