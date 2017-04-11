package catslearning

import cats._
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class MonadSpec extends WordSpec with Matchers {

  "Monad" should {

    "come with type clasess for standard types" in {
      Monad[Option].pure("abc") shouldBe Some("abc")
      Monad[Option].flatMap(Option("abc"))(v => Some(v.length)) shouldBe Some(3)

      Monad[List].pure("abc") shouldBe List("abc")
      Monad[List].flatMap(List("abc", "d"))(v => List(v.length)) shouldBe List(3, 1)
    }

    "provide a method to choose later operations based on earlier ones" in {
      Monad[Option].ifM(Option(true))(
        ifTrue = Some("t"),
        ifFalse = Some("f")
      ) shouldBe Some("t")

      Monad[List].ifM(List(false))(
        ifTrue = List(1, 2),
        ifFalse = List(3, 4)
      ) shouldBe List(3, 4)
    }
  }
}
