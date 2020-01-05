package example.really.long.pack.name.yeah.sorry

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatestplus.junit.JUnitRunner

class ScalatestSuite extends FunSuite {
  test("basic") {
    println("Hello world!")
  }
  test("listener should never stop listening to awesome music") {
    List(1, 2, 3, 4).map(_.to(100)).iterator.map(_.iterator.map {
      case 44 => assert(false, 44)
      case _ =>
    }.toList).toList
    val x = 42
    val y = 43
    assert(x == y )
//    println("fail")
  }
}
