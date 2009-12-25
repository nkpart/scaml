package org.prohax.scaml

import scala.xml.{Text, NodeSeq, Unparsed}
import scala.util.parsing.combinator._

object Constants {
  val TRIPLE_QUOTES = "\"\"\""
  val DOCTYPE = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">"""
  val EMPTY = """Text("")"""

  def surround(name: String,body: => String) = """package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object """ + name + """ extends ScamlFile {
  def render() = {
""" + body + """
  }
}"""

  def indent(indentLevel: Int) = "  " * indentLevel
  def selfClosingTag(indentLevel: Int)(name: String) = indent(indentLevel) + "<" + name + "/>"
  def tagAround(indentLevel: Int, name: String, inner: String, selfClose: Boolean) = if (selfClose && inner.isEmpty) {
    selfClosingTag(indentLevel)(name)
  } else {
    indent(indentLevel) + "<" + name + ">\n" + inner + "\n" + indent(indentLevel) + "</" + name + ">"
  }
  def tagAround(indentLevel: Int, name: String, inner: String): String = tagAround(indentLevel, name, inner, false)
}

case class ScamlTag(level: Int, name: String)
case class ScamlParseResult(headers: List[String], tags: List[ScamlTag]) {
  def render = {
    println("headers = " + headers)
    (headers.map(Constants.indent(2) + _) ::: (if (tags.isEmpty) List(Constants.EMPTY) else {
      tags.map((x) => Constants.selfClosingTag(2)(x.name))
    })).mkString("\n")
  }
}

class Foo extends RegexParsers {
  override val whiteSpace = "".r
  def go: Parser[ScamlParseResult] = opt(header) ~ rep(tagLine) ^^ { (x) =>
    ScamlParseResult(x._1.map(List(_)).getOrElse(Nil), x._2)
  }
  def header: Parser[String] = "!!!".r ^^ (_ => "Text(" + Constants.TRIPLE_QUOTES + Constants.DOCTYPE + Constants.TRIPLE_QUOTES + ")")
  def tagLine: Parser[ScamlTag] = "\n".r ~> rep(indent) ~ tagName ^^ { (x) =>
    println("x = " + x)
    ScamlTag(x._1.length, x._2)
  }
  def indent: Parser[String] = "  ".r
//  def tag: Parser[String] = "^%".r ~> tagName ~ rep(subtag) ^^ ((x) => Constants.tagAround(2, x._1, x._2.mkString("\n"), true))
  def tagName: Parser[String] = """\w+""".r
//  def subtag: Parser[String] = """\n  %""".r ~> tagName ^^ Constants.selfClosingTag(3)
}

object Parser {
  private val foo = new Foo

  def parse(name: String, input: String) = {
    val parsed = foo.parseAll(foo.go, input)
    Constants.surround(name, if (parsed.successful) parsed.get.render else Constants.indent(2) + "Text(\"" + parsed.toString + "\")")
  }
}