package catslearning

import cats.{Applicative, Id}
import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class TraverseSpec extends WordSpec with Matchers {

  private val parseToInt: String => ValidatedNel[String, Int] =
    s => Validated.catchOnly[NumberFormatException](s.toInt).leftMap(_.getMessage).toValidatedNel

  "Traverse" should {

    "be able to traverse given list's items and parse them if all are parseable" in {
      List("1", "2").traverseU(parseToInt) shouldBe valid(List(1, 2))
    }

    "return an error if one of the list's elements is not parseable" in {
      List("1", "a", "2", "b").traverseU(parseToInt) shouldBe invalid(
        NonEmptyList.of(
          """For input string: "a"""",
          """For input string: "b""""
        )
      )
    }

    "allow sequencing" in {
      List(Option(1), Option(2)).traverseU(identity) shouldBe Option(List(1, 2))
      List(Option(1), None, Option(2)).traverseU(identity) shouldBe None

      List(Option(1), Option(2)).sequenceU shouldBe Option(List(1, 2))
      List(Option(1), None, Option(2)).sequenceU shouldBe None
    }
  }
}
