package assignments

import org.scalatest.{Matchers, WordSpec}

class ValtechSpec extends WordSpec with Matchers {

  private val alphabet: Set[Int] = (32 :: (97 to 122).toList).toSet

  def isPangram(strings: Array[String]): String =
    strings.foldLeft("") {
      (result, string) =>
        alphabet diff string.toSet.map((c: Char) => c.toInt) match {
          case s if s.isEmpty => result + "1"
          case _ => result + "0"
        }
    }

  "v" should {

    "a" in {

//      isPangram(Array()) shouldBe ""
//
//      isPangram(Array("asdbc")) shouldBe "0"

//      isPangram(Array("asdbc", "qwertyuioplkjhgfdsamnbvcxz ")) shouldBe "01"

      isPangram(Array(
        "we promptly judged antique ivory buckles for the next prize",
        "we promptly judged antique ivory buckles for the prizes",
        "the quick brown fox jumps over the lazy dog",
        "the quick brown fox jump over the lazy dog"
      )) shouldBe "1010"
    }
  }

  def compress(str: String): String = {

    val charsOrder = str.foldLeft(Seq.empty[Char]){
      (dedup, char) =>
        if (dedup.contains(char)) dedup
        else dedup :+ char
    }

    val charsWithCounts = str.foldLeft(Map.empty[Char, Int]) {
      (aggregates, char) => aggregates.get(char) match {
        case Some(count) => aggregates + (char -> (count + 1))
        case None => aggregates + (char -> 1)
      }
    }

    def countAsString(char: Char): String =
      charsWithCounts.get(char)
        .filter(_ > 1)
        .map(_.toString)
        .getOrElse("")

    charsOrder.foldLeft("") {
      (result, char) => result + char.toString + countAsString(char)
    }
  }

  "compres" in {
    compress("aaaaabbbbbbbbbccccpqrstuv") shouldBe "a5b9c4pqrstuv"
  }

  def closestNumbers(numbers: Array[Int]) = {

    val numbersAsSeq = numbers.foldLeft(Seq.empty[Int]) { (s, n) => s :+ n }

    def findPairs(pairs: Seq[(Int, Int)], numbersToGo: Seq[Int]): Seq[(Int, Int)] =
      numbersToGo match {
        case Nil => pairs
        case head :: tail => findPairs(pairs ++: tail.map(n => head -> n), tail)
      }


    val pairs = findPairs(Nil, numbersAsSeq)

    val pairsWithDiffs = pairs.map{
      case p@(n1, n2) => p -> Math.abs(n1 - n2)
    }

    val sortedByDiff = pairsWithDiffs.sortBy {
      case (p, diff) => diff
    }

    val minDiff = sortedByDiff.head._2

    val pairsWithMinDiff = sortedByDiff.takeWhile {
      case (p, diff) => diff == minDiff
    }

    val sortedWithinPairs = pairsWithMinDiff.map {
      case (p@(n1, n2), diff) =>
        if (n1 > n2) (n2 -> n1) -> diff else p -> diff
    }

    val sortedByPairs = sortedWithinPairs.sortBy {
      case (p@(n1, n2), _) => n1
    }

    sortedByPairs.map {case ((n1, n2), _) => s"$n1  $n2"}.mkString("\n")
  }

  "closest" in {
    closestNumbers(Array(4, 2, 1, 3)) shouldBe "1  2\n2  3\n3  4"
    closestNumbers(Array(4, -2, -1, 3)) shouldBe "-2  -1\n3  4"
  }
}

//object Solution extends App {
//
//  val n = StdIn.readInt()
//  println(s"hello: $n")
//}