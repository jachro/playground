package catslearning

import cats.Semigroup
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class SemigroupSpec extends WordSpec with Matchers {

  "semigroup" should {

    "work for scala lib types" in {
      Semigroup[Int] combine(1, 2) shouldBe 3
      Semigroup[Option[Int]] combine(Some(1), None) shouldBe Some(1)
      Semigroup[Option[Int]] combine(Some(1), Some(2)) shouldBe Some(3)
      Semigroup[List[Int]] combine(List(1, 2), List(3, 4)) shouldBe List(1, 2, 3, 4)
      Semigroup[Set[Int]] combine(Set(1, 2), Set(2, 4)) shouldBe Set(1, 2, 4)
      Semigroup[Int => Int].combine(_ + 1, _ + 2)(5) shouldBe 13
    }

    "work with inline syntax" in {
      val option1: Option[Int] = Some(1)
      val option2: Option[Int] = None
      val option3: Option[Int] = Some(3)
      option1 |+| option2 |+| option3 shouldBe Some(4)
    }

    "allow semigroups composition" in {
      Map("foo" -> Map("bar" -> 5)) combine Map("foo" -> Map("bar" -> 6), "baz" -> Map()) shouldBe Map("foo" -> Map("bar" -> 11), "baz" -> Map())
      Map("foo" -> Map("bar" -> 5)) |+| Map("foo" -> Map("bar" -> 6), "baz" -> Map()) shouldBe Map("foo" -> Map("bar" -> 11), "baz" -> Map())
    }
  }
}
