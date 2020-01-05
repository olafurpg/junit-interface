package example

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatestplus.junit.JUnitRunner

class ScalatestSuite extends FunSuite {
  test("basic") {
    println("Hello world!")
  }
  test("listener should never stop listening to awesome music") {
    ???
//    println("fail")
  }
}
