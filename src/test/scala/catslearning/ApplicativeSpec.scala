package catslearning

import cats._
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class ApplicativeSpec extends WordSpec with Matchers {

  "Applicative" should {

    "wrap the given value in the applicative's context" in {
      val listOfOptions = Applicative[List] compose Applicative[Option]

      listOfOptions.pure("a") shouldBe List(Some("a"))

      val lenghtsSum = (s1: String, s2: String) => s1.length + s2.length
      listOfOptions.map2(listOfOptions.pure("a"), listOfOptions.pure("bc"))(lenghtsSum) shouldBe List(Some(3))

      val optionalsLenghtsSum = (s1: Option[String], s2: Option[String]) => (s1 |@| s2) map lenghtsSum
      val listsOfOptions = listOfOptions.pure("a") |@| listOfOptions.pure("bc")
      listsOfOptions map optionalsLenghtsSum shouldBe List(Some(3))
    }
  }
}