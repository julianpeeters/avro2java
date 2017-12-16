package com.julianpeeters.avro2java

import org.scalajs.dom
import dom.document
import utest._

object TutorialTest extends TestSuite {

  def tests = TestSuite {
    'HelloWorld {
      assert("Hello World" == "Hello World")
    }
  }
}
