import cats.effect.IO

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import cats.effect.unsafe.implicits.global

object Main extends App {
  def rule110(n: (Int, Int, Int)): Int = {
    val (p, q, r) = (n._1 != 0, n._2 != 0, n._3 != 0)
    if ((q && !p) || (q ^ r)) 1 else 0
  }

  def iteration(str: Array[Int], i: Int = 0): Array[Int] = {
    def tryVal(index: Int): Option[Int] =
      try {
        Option(str(index))
      } catch {
        case e: IndexOutOfBoundsException => None
      }
    val result = rule110(str(i), tryVal(i + 1).getOrElse(0), tryVal(i + 2).getOrElse(0))
    str(i) = result
    if (i < str.length - 1) {
      iteration(str, i + 1)
    } else {
      str
    }
  }

  var result = Array(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0)
  (0 to 100).map(i => { result = iteration(result); println((for (x <- result) yield {x match {
    case 0 => " "
    case 1 => "*"
    case _ => None
  }}).mkString(""))})


}