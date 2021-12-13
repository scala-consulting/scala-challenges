import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future, duration}
import scala.io.Source
import scala.util.{Failure, Success}

object Main {

  given ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

  def avgLenOfEnWrds(text: String): Double = {
    val wordLength = text
      .split(" ")
      .map(_.length)

    wordLength.sum.toFloat * (1.0 / wordLength.length)
  }


  def main(args: Array[String]): Unit = {
    val text = Future[String] {
      Source
      .fromURL("https://raw.githubusercontent.com/benschw/shakespeare-txt/master/shakespeare-hamlet-25.txt")
      .getLines()
      .mkString(" ")
    }

    text.onComplete {
      case Success(answer) => println("The result of getting text is " + avgLenOfEnWrds(answer))
      case Failure(exeption) => println("Could not access future value")
    }
  }
}