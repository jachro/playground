package various

import org.scalatest.{Matchers, WordSpec}

class BikePartsSpec extends WordSpec with Matchers {

  import Part._

  "BikeParts" should {

    "allow to build a set of the given parts" in {
      BikeParts(Saddle, Frame, FrontWheel, Saddle) shouldBe BikeParts(Saddle, Frame, FrontWheel)
    }

    "allow to add another part" in {

      val parts = BikeParts(Saddle) + Frame

      parts shouldBe a[BikeParts]

      parts shouldBe BikeParts(Saddle, Frame)
    }

    "allow to map over parts" in {

      val parts: BikeParts = BikeParts(Saddle, Frame, FrontWheel, Saddle) map {
        case Saddle => FrontWheel
        case d => d
      }

      parts shouldBe a[BikeParts]

      parts shouldBe BikeParts(FrontWheel, Frame)
    }
  }
}
