package ru.example.literature

import cats.effect.IO
import cats.effect.unsafe.implicits.global

import java.net.http.{HttpClient, HttpRequest}
import java.net.http.HttpResponse.BodyHandlers
import java.net.URI
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object LiteratureTaskApp {

  def main(args: Array[String]): Unit =
    val url = if (args.isEmpty)
      "https://raw.githubusercontent.com/benschw/shakespeare-txt/master/shakespeare-hamlet-25.txt"
    else
      args.head

    val cpuPool = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))

    val text = (for
      text <- load(url)
      s <- splitTextBy(text)
      count <- countWords(s).evalOn(cpuPool)
      sumLen <- countSumLength(s).evalOn(cpuPool)
    yield {
      println( (sumLen / count).setScale(1, BigDecimal.RoundingMode.HALF_UP) )
    })

    text.unsafeRunSync()
    cpuPool.shutdown()

  
  def load(url: String): IO[String] =
    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder(URI.create(url)).build()
    IO {
      client.send(request, BodyHandlers.ofString).body()
    }


  def splitTextBy(text: String): IO[Array[String]] =
    IO {
      println(Thread.currentThread.getName)
      text.split(" ")
    }

  def countWords(arr: Array[String]): IO[Int] =
    IO {
      println(Thread.currentThread.getName)
      arr.length
    }

  def countSumLength(arr: Array[String]): IO[BigDecimal] =
    IO {
      println(Thread.currentThread.getName)
      BigDecimal(arr.map(_.length).sum)
    }

}
