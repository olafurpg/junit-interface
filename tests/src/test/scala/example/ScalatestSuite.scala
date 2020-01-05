package example

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ScalatestSuite extends FunSuite {
  test("basic") {
    println("Hello world!")
  }
  test("fail") {
    val x = 42;
    val y = 53
    assert(x == y, "foo")
  }
}