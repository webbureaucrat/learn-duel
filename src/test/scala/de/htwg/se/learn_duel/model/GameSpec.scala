package de.htwg.se.learn_duel.model

import java.security.InvalidParameterException

import de.htwg.se.learn_duel.model.impl.{Answer => AnswerImpl, Game => GameImpl, Player => PlayerImpl, Question => QuestionImpl}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import play.api.libs.json.Json

@RunWith(classOf[JUnitRunner])
class GameSpec extends WordSpec with Matchers {
    "A Game" when {
        "new" should {
            val game = GameImpl()

            "have no questions" in {
                game.questions.length should be(0)
            }
            var players: List[Player] = List()
            var helpText: List[String] = List()
            var currentQuestion: Option[Question] = None
            var currentQuestionTime: Option[Int] = None
            "have a standard player" in {
                game.players.length should be(1)
            }
            "have no current question" in {
                game.currentQuestion should be(None)
            }
            "have no current question time" in {
                game.currentQuestionTime should be(None)
            }
            "should have a help text" in {
                game.helpText should be(List(
                    "Learn Duel is based on QuizDuel and works in a similar fashion, but with a new twist:\nYou play with questions based on your school or study assignments.",
                    "For now, there is only local play, but online features will be added later.\nIf you are playing alone, the answers can be selected with the mouse or the keys 1-4.\nIn local multiplayer mode player 1 can specify his answer with the keys 1-4 and\nplayer 2 with the keys 6-9.",
                    "Future features:",
                    "* define your own questions",
                    "* play with up to 3 friends online and compete against each other"
                ))
            }
        }
        "new with supplied questions" should {
            val answers = List(AnswerImpl(0, "text"), AnswerImpl(1, "text2"))
            val question1 = QuestionImpl(0, "text1", 20, answers, 0, 20)
            val question2 = QuestionImpl(1, "text2", 30, answers, 0, 30)
            val questionList = List(question1, question2)
            val game = GameImpl(questions = questionList)

            "have these questions" in {
                game.questions should be(questionList)
            }
        }
        "serialized to JSON" should {
            "be correct" in {
                val game = GameImpl()

                val jsonValue = Json.parse("{\"helpText\":[\"Learn Duel is based on QuizDuel and works in a similar fashion, but with a new twist:\\nYou play with questions based on your school or study assignments.\",\"For now, there is only local play, but online features will be added later.\\nIf you are playing alone, the answers can be selected with the mouse or the keys 1-4.\\nIn local multiplayer mode player 1 can specify his answer with the keys 1-4 and\\nplayer 2 with the keys 6-9.\",\"Future features:\",\"* define your own questions\",\"* play with up to 3 friends online and compete against each other\"],\"players\":[{\"name\":\"Player1\",\"points\":0,\"correctAnswers\":[],\"wrongAnswers\":[]}],\"questions\":[],\"currentQuestion\":null,\"currentQuestionTime\":null}")

                Json.toJson(game) should be(jsonValue)
            }
        }
        "constructed" should {
            val player = PlayerImpl("player")
            val question = QuestionImpl(0, "text1", 20, List(), 0, 20)
            val game = GameImpl()

            "correctly add players" in {
                game.playerCount() should be(1)
                game.addPlayer(player)
                game.playerCount() should be(2)
            }

            "correctly remove players" in {
                game.playerCount() should be(2)
                game.removePlayer(player)
                game.playerCount() should be(1)
            }

            "correclty add questions" in {
                game.questionCount() should be(0)
                game.addQuestion(question)
                game.questionCount() should be(1)
            }

            "correctly remove questions" in {
                game.questionCount() should be(1)
                game.removeQuestion(question)
                game.questionCount() should be(0)
            }
        }
    }
}

