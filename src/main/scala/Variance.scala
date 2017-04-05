class Grandpa(name: String = "grandpa") {
  override def toString: String = name
}

//object Grandpa extends Grandpa

class Dad(name: String = "dad") extends Grandpa(name)

//object Dad extends Dad

class Child() extends Dad("child")

//object Child extends Child

trait Covariance[+A] {
  def create(name: String): A
}

class CovarianceImpl extends Covariance[Grandpa] {
  override def create(name: String) = new Dad()
}

class Contravariance[-A] {
  def print(person: A) = println(person)
}

object VarianceTest {
  def test() = {
    val covarianceImpl: CovarianceImpl = new CovarianceImpl
    covarianceImpl.create("child name")

    val contravariance = new Contravariance[Dad]
    contravariance.print(new Child())

    val f: (Dad) => Dad =
      (p: Grandpa) => new Child()

    val dad: Dad = f(new Dad())

  }
}