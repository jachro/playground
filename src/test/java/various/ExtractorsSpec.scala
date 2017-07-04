package various

import org.scalatest.{Matchers, WordSpec}
import various.Extractors.{Domain, Email, NoDot}

class ExtractorsSpec extends WordSpec with Matchers {

  "email extractor" should {

    "extract valid email" in {

      val Email(user, domain) = "kuba@abc.com"

      user shouldBe "kuba"
      domain shouldBe "abc.com"
    }

    "not extract invalid email" in {

      val userAndDomain = "kuba AT abc.com" match {
        case Email(user, domain) => user -> domain
        case s => s
      }

      userAndDomain shouldBe "kuba AT abc.com"
    }
  }

  "domain extractor" should {

    "extract all domain parts" in {

      val Domain(all@_*) = "amdg.co.uk"

      all shouldBe List("amdg", "co", "uk")


      val Domain(f, _, l) = "amdg.co.uk"

      f shouldBe "amdg"
      l shouldBe "uk"


      val Domain(a, _, _*) = "a.amdg.co.uk"

      a shouldBe "a"
    }
  }

  "boolean extractor" should {

    "check if matches" in {

      val isOk = "kuba@mail.com" match {
        case Email(NoDot(), Domain(parts@_*)) => true
        case _ => false
      }

      isOk shouldBe true


      val isNotOk = "kuba.c@mail.com" match {
        case Email(NoDot(), Domain(parts@_*)) => true
        case _ => false
      }

      isNotOk shouldBe false

    }
  }

  "combined extractors" should {

    "all work together" in {

      val res = "kuba@mail.com" match {
        case Email(user@NoDot(), Domain(parts@_*)) => user -> parts
      }

      res shouldBe "kuba" -> List("mail", "com")
    }
  }
}
