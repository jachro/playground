package catslearning

import cats._
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

class ApplySpec extends WordSpec with Matchers {

  private val stringLength = (string: String) => string.length
  private val stringLengthPlusOne = (string: String) => string.length + 1
  private val lengthsSumOfTwo = (s1: String, s2: String) => s1.length + s2.length

  "Apply" should {

    "allow to map for standard Scala types" in {
      Apply[Option].map(Some("a"))(_.length) shouldBe Some(1)
      Apply[Option].map(None: Option[String])(_.length) shouldBe None

      Apply[List].map(List(1, 2, 3))(_ + 2) shouldBe List(3, 4, 5)
    }

    "allow to compose Apply instances" in {
      val listOfOptions = Apply[List] compose Apply[Option]
      listOfOptions.map(List(Some("abc"), None, Some("")))(stringLength) shouldBe List(Some(3), None, Some(0))
    }

    "allow to use the 'ap' method taking a higher order function (not a function like in the case of map)" in {
      Apply[Option].ap(Some(stringLength))(Some("abcd")) shouldBe Some(4)
      Apply[Option].ap(Some(stringLength))(None) shouldBe None
      Apply[List].ap(List(stringLength))(List("abcd", "")) shouldBe List(4, 0)
      Apply[List].ap(List(stringLength, stringLengthPlusOne))(List("abcd", "")) shouldBe List(4, 0, 5, 1)

      val listOfOptions = Apply[List] compose Apply[Option]
      listOfOptions.ap(List(Option(stringLength)))(List(Some("abcd"), None, Some(""))) shouldBe List(Some(4), None, Some(0))

      listOfOptions.ap(List(Option(stringLength), None))(List(Some("abcd"), None, Some(""))) shouldBe List(Some(4), None, Some(0), None, None, None)
    }

    "allow transforming two arguments in some context" in {
      Apply[Option].ap2(Some(lengthsSumOfTwo))(Some("ab"), Some("efg")) shouldBe Some(5)
      Apply[Option].map2(Some("ab"), Some("efg"))(lengthsSumOfTwo) shouldBe Some(5)
    }

    "allow transforming args into a tuple" in {
      Apply[Option].tuple2(Some("a"), Some("b")) shouldBe Some("a", "b")
      Apply[Option].tuple2(Some("a"), None) shouldBe None
    }

    "allow do products using two functions" in {
      Apply[Option].product(Some("a"), None) shouldBe None
    }

    "allow to be used with the |@| operator" in {
      val option = Option("abc") |@| Option("de")
      val optionWithNone = option |@| Option.empty[String]

      val lengthsSumOfThree = (s1: String, s2: String, s3: String) => s1.length + s2.length + s3.length

      option map lengthsSumOfTwo shouldBe Some(5)
      optionWithNone map lengthsSumOfThree shouldBe None

      option apWith Some(lengthsSumOfTwo) shouldBe Some(5)

      option.tupled shouldBe Some("abc" -> "de")
    }
  }
}
