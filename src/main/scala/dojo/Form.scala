package dojo

import cats._
import cats.data._
import cats.implicits._

case class Person(firstName: String, lastName: String, age: Int)

case class Form(firstName: String, lastName: String, age: Int)

sealed trait ValidationError
case class AgeGreaterThanHundred(age: Int) extends ValidationError
case class FirstNameLongerThanFiftyChars(name: String) extends ValidationError

object Person {

  def fromForm(form: Form): ValidatedNel[ValidationError, Person] = {

    val age: Validated[NonEmptyList[ValidationError], Int] =
      if (form.age > 100) AgeGreaterThanHundred(form.age).invalidNel else form.age.validNel
    val firstName: Validated[NonEmptyList[ValidationError], String] =
      if (form.firstName.length > 50) FirstNameLongerThanFiftyChars(form.firstName).invalidNel else form.firstName.validNel
    val lastName: Validated[NonEmptyList[ValidationError], String] =
      if (form.lastName.length > 50) FirstNameLongerThanFiftyChars(form.lastName).invalidNel else form.lastName.validNel

    (firstName, lastName, age).map3 {
      case (first, l, a) => Person(first, l, a)
    }
  }
}