package algorithms

import org.scalatest.{Matchers, WordSpec}

class RecursionSpec extends WordSpec with Matchers {

  import Recursion._

  "findSmallest" should {

    "find the smallest number from an empty list" in {
      findSmallest(Nil) shouldBe None
    }

    "find the smallest number from a list" in {
      findSmallest(Seq(3, 6, 1, 9)) shouldBe Some(1)
    }
  }
}
