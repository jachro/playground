package assignments

import org.scalatest.{Matchers, WordSpec}

import scala.annotation.tailrec

class IceCreamParlor extends WordSpec with Matchers {

  def spendAll(in: Array[String]): Seq[String] = {

    (1 to in(0).toInt).foldLeft(Seq.empty[String]) { (res, tripNb) =>

      val (amount, flavours) = in.findDataForTrip(tripNb)

      res ++: findAllCombinations(flavours)
        .map(pairsWithCosts)
        .filter(pairsCosting(amount))
        .sortBy(firstFlavourIdx)
        .map(toPairsOfFlavourIdxWithSmallerIdxFirst)
        .map(toStringOfFlavourIndexes)
    }
  }

  private implicit class InputParser(input: Array[String]) {

    def findDataForTrip(number: Int): (Int, Seq[Flavour]) = {

      val tripStartIdx = (number - 1) * 3

      val amount = input(tripStartIdx + 1).toInt

      val flavours = input(tripStartIdx + 3).split(" ").toList.map(_.toInt)

      amount -> flavours
        .zipWithIndex
        .map { case (price, idx) => price -> (idx + 1) }
        .map(Flavour.fromTuple)
    }

  }

  case class Flavour(price: Int, idx: Int)

  object Flavour {
    def fromTuple(tuple: (Int, Int)) = Flavour(tuple._1, tuple._2)
  }

  @tailrec
  private def findAllCombinations[A](seq: Seq[A], res: Seq[(A, A)] = Nil): Seq[(A, A)] =
    seq match {
      case Nil => res
      case head :: tail => findAllCombinations(
        tail,
        res ++: tail.map(i => head -> i)
      )
    }

  private val pairsWithCosts = (flavoursPair: (Flavour, Flavour)) => {
    val (fl1, fl2) = flavoursPair
    (fl1, fl2, fl1.price + fl2.price)
  }

  private def pairsCosting(amount: Int): ((Flavour, Flavour, Int)) => Boolean = {
    case (_, _, price) => price == amount
  }

  private val firstFlavourIdx: ((Flavour, Flavour, Int)) => Int = {
    case (Flavour(_, f1Idx), _, _) => f1Idx
  }

  private val toPairsOfFlavourIdxWithSmallerIdxFirst: ((Flavour, Flavour, Int)) => (Int, Int) = {
    case (Flavour(_, f1Idx), Flavour(_, f2Idx), _) => (f1Idx min f2Idx) -> (f1Idx max f2Idx)
  }

  private val toStringOfFlavourIndexes: ((Int, Int)) => String = {
    case (f1Idx, f2Idx) => s"$f1Idx $f2Idx"
  }

  "spendAll" should {

    "return two ice cream flavours that uses all the money" in {
      val in = Array(
        "2",
        "4",
        "5",
        "1 4 5 3 2",
        "4",
        "4",
        "2 2 4 3"
      )

      spendAll(in) shouldBe Seq(
        "1 4",
        "1 2"
      )
    }
  }
}
