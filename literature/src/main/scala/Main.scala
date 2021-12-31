import cats.effect.IO

import java.time.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import java.util.concurrent.Executors
import scala.io.{BufferedSource, Source}
import scala.sys.exit
import scala.util.{Failure, Success}
//https://raw.githubusercontent.com/benschw/shakespeare-txt/master/shakespeare-hamlet-25.txt

object Main {
  def main(args: Array[String]): Unit = {
    def countWorld(words: String) = IO {
      words.split(" ").length
    }

    def countChar(line: String) = IO {
      line.replace(" ", "").length
    }

    val avgStr = Source.fromURL(args(0)).getLines().mkString(" ").split("\n").map(s =>
      (for {
        x <- countWorld(s)
        y <- countChar(s)
      } yield {
        y.toFloat / x.toFloat
      }).unsafeRunSync()
    )

    val result = avgStr.sum / avgStr.length
    printf("%.1f\n", result)
  }

}
