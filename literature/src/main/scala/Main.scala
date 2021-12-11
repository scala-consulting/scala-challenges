import java.time.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import java.util.concurrent.Executors
import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success}
//https://raw.githubusercontent.com/benschw/shakespeare-txt/master/shakespeare-hamlet-25.txt

object Main {
  given ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

  def avgStrInText(text: String): Double = {
    val lengthStr = text.split(" ").map(_.length)

    (lengthStr.sum.toFloat / lengthStr.length)
  }


  def main(args: Array[String]): Unit = {
    val text = Future[String] { Source.fromURL(args(0)).getLines().mkString(" ") }
    text.onComplete {
      case Success(posts) => println(avgStrInText(posts))
      case Failure(t) => t.printStackTrace()
    }
  }
}
