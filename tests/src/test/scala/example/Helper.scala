package example


class Helper  {
   def foo(): Unit =
    1.to(5).iterator.foreach {
      case 3 => ???
      case _ =>
    }

}
