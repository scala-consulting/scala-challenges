import cats.effect.IO

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import cats.effect.unsafe.implicits.global

object Main extends App {
  def rule110(n: (Int, Int, Int)): Int = {
    val (p, q, r) = (n._1 != 0, n._2 != 0, n._3 != 0)
    if ((q && !p) || (q ^ r)) 1 else 0
  }

  val x = (3, 5, 3)

  println(rule110(1, 1, 1))
  println(rule110(1, 1, 1))

  println()
}