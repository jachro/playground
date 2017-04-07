package catslearning

import cats._
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class MonoidSpec extends WordSpec with Matchers {

  "Monoid" should {

    "define 'empty' for standard types" in {
      Monoid[String].empty shouldBe ""
      Monoid[Int].empty shouldBe 0
      Monoid[Option[Int]].empty shouldBe None
      Monoid[List[Int]].empty shouldBe Nil
      Monoid[Int => Int].empty(100) shouldBe 0
    }

    "allow to combine all" in {
      Monoid[String] combineAll Nil shouldBe ""

      Monoid[String] combineAll Seq("a", "b", "c") shouldBe "abc"
      Monoid[Int] combineAll Seq(1, 2, 3) shouldBe 6
      Monoid[Option[Int]] combineAll Seq(Some(1), None, Some(3)) shouldBe Some(4)
      Monoid[List[Int]] combineAll Seq(List(1, 2), Nil, List(3, 4)) shouldBe List(1, 2, 3, 4)
      Monoid[Int => Int].combineAll(Seq(_ + 1, _ + 2))(1) shouldBe 5

      Monoid[Map[String, Int]] combineAll List(Map("a" → 1, "b" → 2), Map("a" → 3)) shouldBe Map("a" -> 4, "b" -> 2)
    }

    "allow to combineAllOption" in {
      Monoid[Int] combineAllOption Seq(1, 2, 3) shouldBe Some(6)
    }

    "allow to combine an object n times" in {
      Monoid[String] combineN ("a", 3) shouldBe "aaa"
      Monoid[Int] combineN (2, 3) shouldBe 6
    }

    "allow to be used with Foldable and results with the same as combineAll" in {
      List(1, 2, 3) foldMap identity shouldBe 6
      List(1, 2, 3).foldMap(_.toString) shouldBe "123"

      List(Map("a" → 1, "b" → 2), Map("a" → 3)) foldMap identity shouldBe Map("a" -> 4, "b" -> 2)
    }

    "allow to work with self defined implicits" in {
      def tupleMonoid[A: Monoid, B: Monoid]: Monoid[(A, B)] = new Monoid[(A, B)] {

        override val empty = (Monoid[A].empty, Monoid[B].empty)

        override def combine(x: (A, B), y: (A, B)) = Monoid[A].combine(x._1, y._1) -> Monoid[B].combine(x._2, y._2)
      }

      List(1, 2, 3, 4).foldMap(v => v -> v.toString)(tupleMonoid) shouldBe (10 -> "1234")
    }
  }
}
