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
    def tryVal(index: Int): Int =
      try {
        str(index)
      } catch {
        case e: IndexOutOfBoundsException => str(0 + i)
      }

    val result = rule110(str(i), tryVal(i + 1), tryVal(i + 2))

    str(i) = result
    if (i < str.length - 1) {
      iteration(str, i + 1)
    } else {
      str
    }
  }

  val allPossibleStates = (0 to 16384).map {
    i => {
      val bNumber = i.toBinaryString
      (bNumber + "0" * (15 - bNumber.length)).toCharArray.map(_.toString.toInt)
    }
  }.map(iteration(_))

  println(allPossibleStates(0).length)
  println(allPossibleStates(16382).mkString(" "))

/*  val arr = Array(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1)
  println(arr.length)
  println(iteration(arr).mkString(" "))*/

  //  var result = Array(0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0)
  //  (0 to 100).map(i => {
  //    result = iteration(result);
  //    println((for (x <- result) yield {
  //      x match {
  //        case 0 => " "
  //        case 1 => "*"
  //        case _ => None
  //      }
  //    }).mkString(""))
  //  })


}