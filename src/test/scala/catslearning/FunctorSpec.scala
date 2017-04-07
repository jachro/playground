package catslearning

import cats._
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class FunctorSpec extends WordSpec with Matchers {

  "Functor" should {

    "be easily created for standard monadic Scala types" in {
      val seqFunctor = new Functor[Seq] {
        override def map[A, B](fa: Seq[A])(f: (A) => B) = fa map f
      }

      seqFunctor.map(Seq(1, 2, 3))(_ + 1) shouldBe Seq(2, 3, 4)
    }

    "work with standard types" in {
      Functor[Option].map(Option("abc"))(_.length) shouldBe Some(3)
      Functor[Option].map(None: Option[String])(_.length) shouldBe None

      Functor[List].map(List("a", "ab", "abc"))(_.length) shouldBe List(1, 2, 3)
    }

    "allow to create lift functions" in {
      val findLengthOf: (Option[String]) => Option[Int] = Functor[Option].lift(_.length)

      findLengthOf(Option("abc")) shouldBe Some(3)
      findLengthOf(None: Option[String]) shouldBe None
    }

    "allow to create fproducts" in {
      Functor[Option].fproduct(Option("abc"))(_.length) shouldBe Some("abc" -> 3)
      Functor[Option].fproduct(None: Option[String])(_.length) shouldBe None
    }

    "compose with each other" in {
      val operateOnListItems = Functor[List] compose Functor[Option]

      operateOnListItems.map(List(Some("a"), None, Some("ab")))(_.length) shouldBe List(Some(1), None, Some(2))
      List(Some("a"), None, Some("ab")).map(_.map(_.length)) shouldBe List(Some(1), None, Some(2))
    }

    "work using implicits" in {
      val f1: (Int => String) = i => i.toString
      val f2: (String => Int) = _.length

      (f1 map f2)(123) should be(3)
    }
  }
}
