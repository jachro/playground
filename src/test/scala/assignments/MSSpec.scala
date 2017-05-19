package assignments

import org.scalatest.{Matchers, WordSpec}

import scala.annotation.tailrec
import scala.math.BigDecimal.RoundingMode

class MSSpec extends WordSpec with Matchers {

  case class Row(keys: Seq[String], value: Double)

  def aggregate(rows: Seq[Row]): Seq[Seq[Row]] = {

    @tailrec
    def aggregateRowsAndReduceKeys(rows: Seq[(Seq[String], Double)], result: Seq[Seq[Row]]): Seq[Seq[Row]] =
      rows match {
        case Nil => Seq.empty[Seq[Row]]
        case head :: _ if head._1.isEmpty => result :+ Seq(Row(Seq.empty, rows.map(_._2).sum))
        case rws => aggregateRowsAndReduceKeys(
          rows = stripKeysHeads(rws),
          result = result :+ aggregateRows(rows))
      }

    aggregateRowsAndReduceKeys(stripKeysHeads(rows.map(r => r.keys.reverse -> r.value)), Nil)
  }

  private def round(d: Double): Double = BigDecimal(d).setScale(2, RoundingMode.HALF_DOWN).toDouble

  private def stripKeysHeads(rows: Seq[(Seq[String], Double)]) = rows.map { case (k, v) => k.tail -> v }

  private def aggregateRows(rows: Seq[(Seq[String], Double)]) = {

    def addToAggregate(keys: Seq[String], value: Double, aggregate: Seq[Row]) =
      aggregate.find(_.keys == keys) match {
        case None => aggregate :+ Row(keys, value)
        case Some(agg) if agg.keys == keys => aggregate.foldLeft(Seq.empty[Row]) {
          case (res, Row(rk, rv)) if rk == keys => res :+ Row(rk, round(rv + value))
          case (res, row) => res :+ row
        }
      }

    rows.foldLeft(Seq.empty[Row]) {
      case (aggregate, (k, v)) => addToAggregate(k.reverse, v, aggregate)
    }
  }

  "aggregate" should {

    "work for empty seq" in {
      aggregate(Nil) shouldBe Nil
    }

    "work for seq of 1 key" in {
      val rows = Seq(
        Row(Seq("New York"), 1.2),
        Row(Seq("London"), 3.4)
      )

      aggregate(rows) shouldBe Seq(
        Seq(
          Row(Seq.empty, 4.6)
        )
      )
    }

    "work for seq of 2 keys" in {
      val rows = Seq(
        Row(Seq("New York", "1"), 1.2),
        Row(Seq("New York", "2"), 1.2),
        Row(Seq("London", "3"), 3.4)
      )

      aggregate(rows) shouldBe Seq(
        Seq(
          Row(Seq("New York"), 2.4),
          Row(Seq("London"), 3.4)
        ),
        Seq(
          Row(Seq.empty, 5.8)
        )
      )
    }

    "work for seq of 3 keys" in {
      val rows = Seq(
        Row(Seq("New York", "Bonds", "Q1"), 125253.45),
        Row(Seq("New York", "Bonds", "Q2"), 37445.10),
        Row(Seq("New York", "Bonds", "Q3"), 32727.58),
        Row(Seq("New York", "Swaps", "Q1"), 61737.27),
        Row(Seq("New York", "Swaps", "Q2"), 72457.00),
        Row(Seq("New York", "Swaps", "Q3"), 1375853.47),
        Row(Seq("London", "Bonds", "Q1"), 97527.45),
        Row(Seq("London", "Bonds", "Q2"), 718322.1),
        Row(Seq("London", "Bonds", "Q3"), 105742.58)
      )

      aggregate(rows) shouldBe Seq(
        Seq(
          Row(Seq("New York", "Bonds"), 195426.13),
          Row(Seq("New York", "Swaps"), 1510047.74),
          Row(Seq("London", "Bonds"), 921592.13)
        ),
        Seq(
          Row(Seq("New York"), 1705473.87),
          Row(Seq("London"), 921592.13)
        ),
        Seq(
          Row(Seq.empty, 2627066)
        )
      )
    }
  }
}
