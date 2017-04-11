package catslearning

import cats._
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class FoldableSpec extends WordSpec with Matchers {

  "Foldable" should {

    "provide 'foldRight' taking instance of Eval" in {
      Foldable[List].foldRight(List(1, 2, 3), Now(1))((i, all) => Later(i + all.value)).value shouldBe 7
    }

    "provide 'fold'" in {
      Foldable[List].fold(List(1, 2, 3)) shouldBe 6
    }

    "provide 'foldMap'" in {
      Foldable[List].foldMap(List("ab", "c"))(_.length) shouldBe 3
    }

    "provide 'foldK'" in {
      Foldable[List].foldK(List(List("ab", "c"), List("de", "fg"))) shouldBe List("ab", "c", "de", "fg")
    }

    "provide 'foldM'" in {
      Foldable[List].foldM[Option, String, Int](List("ab", "c"), 0)((acc, i) => Option(acc + i.length)) shouldBe Some(3)
    }

    "provide 'compose'" in {
      val foldableListOption = Foldable[List].compose[Option]
      foldableListOption.fold(List(Option(1), None, Option(3), Option(4))) shouldBe 8
    }
  }
}
