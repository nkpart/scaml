package org.prohax

object Scaml {
  case class Tag(attrs: Map[Symbol, String], tags: Tag*) {
    val name = getClass.getSimpleName

    override def toString = {
      val tagOpen = "<" + name + attrs.map(kv => " " + kv._1.name + "='" + kv._2.replaceAll("'", "\\\\'") + "'").mkString + ">"
      if (tags.isEmpty) {
        tagOpen + "</" + name + ">"
      } else {
        "\n" + tagOpen + tags.map(_.toString).mkString + "</" + name + ">\n"
      }
    }
  }

  trait TagV2 {
    val attrs: Map[Symbol, String]
    val tags: Seq[Tag]
    val name: String

    override def toString = {
      val tagOpen = "<" + name + attrs.map(kv => " " + kv._1.name + "='" + kv._2.replaceAll("'", "\\\\'") + "'").mkString + ">"
      if (tags.isEmpty) {
        tagOpen + "</" + name + ">"
      } else {
        "\n" + tagOpen + tags.map(_.toString).mkString + "</" + name + ">\n"
      }
    }
  }

  trait NamedByClass {
    val name = getClass.getSimpleName
  }

  implicit def stringToTag(s: String) = new Tag(Map()) {
    override def toString = s
  }

  case class TagBuilder(n: String) {
    def apply() = new TagV2 {
      val attrs: Map[Symbol, String] = Map()
      val name: String = n
      val tags: Seq[Tag] = Seq()
    }

    def apply(ts: Tag*) = new TagV2 {
      val attrs: Map[Symbol, String] = Map()
      val name = n
      val tags: Seq[Tag] = ts
    }

    def apply(as: Map[Symbol, String], ts: Tag*) = new TagV2 {
      val attrs: Map[Symbol, String] = as
      val name = n
      val tags: Seq[Tag] = ts
    }

    def apply(a1: (Symbol, String), ts: Tag*): TagV2 = apply(Map(a1))

    def apply(a1: (Symbol, String), a2: (Symbol, String), ts: Tag*): TagV2 = apply(Map(a1, a2))

    def apply(a1: (Symbol, String), a2: (Symbol, String), a3: (Symbol, String), ts: Tag*): TagV2 = apply(Map(a1, a2, a3))
  }

  val List(html, head, title, body, h1, h2, p, span, div) = List("html", "head", "title", "body", "h1", "h2", "p", "span", "div").map(TagBuilder(_))

  implicit def toOldTag(t: TagV2): Tag = new Tag(t.attrs, t.tags: _ *) {
    override val name = t.name
  }
}
