import cats.effect.IO

import java.time.Duration
import scala.io.{Source, BufferedSource}
import scala.util.{Failure, Success}
import scala.concurrent.{ExecutionContext, Future, Await}
import java.util.concurrent.Executors
import cats.effect.unsafe.implicits.global


object Main {
  def main(args: Array[String]): Unit = {
    val url = "https://raw.githubusercontent.com/benschw/shakespeare-txt/master/shakespeare-hamlet-25.txt"

    val cpuPool = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))

    val answer = (for {
      text <- IO {
        Source
          .fromURL("https://raw.githubusercontent.com/benschw/shakespeare-txt/master/shakespeare-hamlet-25.txt")
          .getLines()
          .mkString(" ")
    }
      words <- countWorld(text).evalOn(cpuPool)
      symbols <- countSymbol(text).evalOn(cpuPool)
  } yield {
      println(symbols.toFloat * (1.0 / words.toFloat))
    })

    answer.unsafeRunSync()
    cpuPool.shutdown()

    def countWorld(words: String): IO[Int] =
      IO {
        println(Thread.currentThread.getName)
        words
          .split(" ")
          .length
      }

    def countSymbol(lines: String): IO[Int] =
      IO {
        println(Thread.currentThread.getName)
        lines
          .replace(" ", "")
          .length
      }
}

