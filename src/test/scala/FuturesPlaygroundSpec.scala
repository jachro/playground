import org.scalatest.concurrent.ScalaFutures._
import org.scalatest.time.{Span, Seconds}
import org.scalatest.{Matchers, WordSpec}

class FuturesPlaygroundSpec extends WordSpec with Matchers {

  "multiply" should {
    "return the given value multiplied by 2 if the given value is greater than 0" in {
      whenReady(FuturesPlayground.multiply(4), timeout(Span(6, Seconds))) {
        _ shouldBe 8
      }
    }

    "throw an exception if the given value is less than 0" in {
      whenReady(FuturesPlayground.multiply(-1).failed, timeout(Span(6, Seconds))) {
        _ shouldBe an [IllegalArgumentException]
      }
    }
  }
}
