package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object tag_parsing extends ScamlFile {
  def renderXml() = {
    <div class='tags'>
      <foo>1</foo>
      <FOO>2</FOO>
      <fooBAR>3</fooBAR>
      <fooBar>4</fooBar>
      <foo_bar>5</foo_bar>
      <foo-bar>6</foo-bar>
      <foo:bar>7</foo:bar>
      <foo class='bar'>8</foo>
      <fooBAr_baz:boom_bar>9</fooBAr_baz:boom_bar>
      <foo13>10</foo13>
      <foo2u>11</foo2u>
    </div>
    <div class='classes'>
      <p id='baz' class='foo bar'/>
      <div class='fooBar'>a</div>
      <div class='foo-bar'>b</div>
      <div class='foo_bar'>c</div>
      <div class='FOOBAR'>d</div>
      <div class='foo16'>e</div>
      <div class='123'>f</div>
      <div class='foo2u'>g</div>
    </div>
  }
}