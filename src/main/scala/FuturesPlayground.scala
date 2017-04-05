import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.Random

object FuturesPlayground {

  def multiply(number: BigDecimal): Future[BigDecimal] = {
    val promiseToMultiply = Promise[BigDecimal]()

    Future {
      println(s"Starting process to multiply $number")
      Thread.sleep(1000 + Random.nextInt(1000))

      if (number > 0) {
        val result = number * 2
        promiseToMultiply.success(result)

        println(s"Multiply process finished with result $result")
      } else {
        promiseToMultiply.failure(new IllegalArgumentException(s"$number less than 0"))

        println(s"Multiply process finished with exception")
      }
    }

    promiseToMultiply.future
  }
}
