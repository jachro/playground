package various

object Extractors {

  object Email {

    def unapply(email: String): Option[(String, String)] =
      if (email.isEmpty) None
      else {
        val parts = email.split("@")
        if (parts.length != 2) None
        else Some(parts(0) -> parts(1))
      }
  }

  object Domain {

//    def apply(parts: String*): String = parts.mkString(".")

    def unapplySeq(domain: String): Option[Seq[String]] =
      if (domain.isEmpty) None
      else Some(domain.split("\\."))
  }

  object NoDot {

    def unapply(part: String): Boolean = !part.contains(".")
  }
}
