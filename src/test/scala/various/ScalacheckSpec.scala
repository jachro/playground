package various

import org.scalacheck.{Arbitrary, Gen, Shrink}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.reflect.ClassTag

class ScalacheckSpec extends AnyWordSpec with ScalaCheckPropertyChecks with Matchers {

  sealed trait Trait

  case object A extends Trait

  case object B extends Trait

  //  implicit val noIntShrink: Shrink[Int] = Shrink.shrinkAny
  //  implicit val noListShrink: Shrink[List[SomeEnum.Value]] = Shrink.shrinkAny

  implicit def noShrink[T](implicit classTag: ClassTag[T]) = Shrink.shrinkAny

  implicit private val traitArbitrary: Arbitrary[Trait] = Arbitrary(Gen.oneOf(A, B))

  "traitArbitrary" should {

    "be available in a test" in {
      forAll { o: Trait =>
        println(o)
      }
    }

    "be available to generate tuples" in {
      forAll { (o1: Trait, o2: Trait) =>
        println(s"$o1 -> $o2")
      }
    }
  }

  object SomeEnum extends Enumeration {
    val E1, E2, E3, E4, E5 = Value
  }

  implicit private val enumArbitrary: Arbitrary[SomeEnum.Value] = Arbitrary(Gen.oneOf(SomeEnum.values.toSeq))

  "enumArbitrary" should {

    "be available in a test" in {
      forAll { o: SomeEnum.Value =>
        println(o)
      }
    }

    "be available to generate tuples" in {
      forAll { (o1: SomeEnum.Value, o2: SomeEnum.Value) =>
        println(s"$o1 -> $o2")
      }
    }

    "be available to create a generator of tuples" in {

      val tuples = for {
        v1 <- enumArbitrary.arbitrary
        v2 <- enumArbitrary.arbitrary.retryUntil(_ != v1)
      } yield v1 -> v2

      forAll(tuples) { case (o1, o2) =>
        assume(o1 != o2)
        println(s"$o1 -> $o2")
      }
    }

    "pick" in {
      forAll(Gen.pick(4, SomeEnum.values.toSeq)) { list =>
        list should have size 4
        list.distinct should have size 4
        println(list)
      }
    }
  }
}
