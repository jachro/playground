package algorithms

import scala.annotation.tailrec

object Recursion {

  def findSmallest(list: Seq[Int]): Option[Int] = {
    
    @tailrec
    def find(left: Seq[Int], smallestSoFar: Option[Int] = None): Option[Int] = left match {
      case Nil => smallestSoFar
      case head :: Nil => smallestSoFar match {
        case Some(smallestFound) if head > smallestFound => smallestSoFar
        case _ => Some(head)
      }
      case head :: tail => smallestSoFar match {
        case Some(smallestFound) if head > smallestFound => find(tail, smallestSoFar)
        case _ => find(tail, Some(head))
      }
    }

    find(list)
  }
}
