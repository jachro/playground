package dojo

import cats.data._
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class PersonSpec extends WordSpec with Matchers {

  "fromForm" should {

    "validate" in {

      Person.fromForm(Form("first", "second", 20)) shouldBe Person("first", "second", 20).validNel

    }
  }
}
