package various

import scala.collection.generic.CanBuildFrom
import scala.collection.{SetLike, mutable}

sealed trait Part

object Part {

  case object Frame extends Part

  case object FrontWheel extends Part

  case object Saddle extends Part

}

final case class BikeParts private(parts: mutable.LinkedHashSet[Part])
  extends Set[Part]
    with SetLike[Part, BikeParts] {

  override def contains(elem: Part) = parts.contains(elem)

  override def +(elem: Part) = BikeParts(parts + elem)

  override def -(elem: Part) = BikeParts(parts - elem)

  override def iterator = parts.iterator

  override def empty: BikeParts = BikeParts.empty
}

object BikeParts {

  def apply(parts: Part*): BikeParts = BikeParts(mutable.LinkedHashSet(parts: _*))

  val empty = BikeParts(mutable.LinkedHashSet.empty[Part])

  private def newBuilder: mutable.Builder[Part, BikeParts] = new mutable.SetBuilder[Part, BikeParts](BikeParts.empty)

  implicit val canBuildFrom: CanBuildFrom[BikeParts, Part, BikeParts] =

    new CanBuildFrom[BikeParts, Part, BikeParts] {

      override def apply(from: BikeParts): mutable.Builder[Part, BikeParts] = newBuilder

      override def apply(): mutable.Builder[Part, BikeParts] = newBuilder
    }
}