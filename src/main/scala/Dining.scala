import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.duration._

object Dining {

  final case class Simulation()

  val tableSize = 5

  def main(args: Array[String]): Unit = {
    val creator: Behavior[Simulation] =
      Behaviors.setup { context =>
        val forks = for (i <- 1 to tableSize) yield context.spawn(Fork.available, "Fork " + i)
        val philosophers = for (i <- 1 to tableSize) yield context.spawn(Philosopher.init, "Philosopher " + i)
        val seats = for (i <- 0 until tableSize) yield {
          TableSeat(philosophers(i), forks(i), forks((i + 1) % tableSize))
        }

        Behaviors.receiveMessage { _ =>
          seats.foreach { seat =>
            seat.philosopher ! Philosopher.Think(seat)
          }
          Behaviors.same
        }
      }

    val system: ActorSystem[Simulation] = ActorSystem(creator, "creator")
    system ! Simulation()
  }
}
