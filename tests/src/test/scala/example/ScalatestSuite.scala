package example

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatestplus.junit.JUnitRunner

//@RunWith(classOf[JUnitRunner])
class ScalatestSuite extends FunSuite {
  test("basic") {
    System.err.println("HEllo err")
    System.out.println("HEllo out")
//    ???
    println("basic")
  }
  test("bar") {
    println("bar")
  }
}
