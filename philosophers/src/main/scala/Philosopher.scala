import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.duration._

case class TableSeat(philosopher: ActorRef[Philosopher.Philosopher_],
                     leftFork: ActorRef[Fork.Fork_],
                     rightFork: ActorRef[Fork.Fork_])

object Philosopher {
  import Fork._

  sealed trait Philosopher_

  final case class Eat(seat: TableSeat) extends Philosopher_
  final case class Think(seat: TableSeat) extends Philosopher_
  final case class ForkBusy(Fork: ActorRef[Fork_], seat: TableSeat) extends Philosopher_
  final case class ForkTaken(Fork: ActorRef[Fork_], seat: TableSeat) extends Philosopher_

  val thinking: Behavior[Philosopher_] =
    Behaviors.receive { (context, message) =>
      message match {
        case Eat(seat) =>
          seat.leftFork ! TakeFork(seat)
          seat.rightFork ! TakeFork(seat)
          hungry
        case _ => Behaviors.same
      }
    }

  val hungry: Behavior[Philosopher_] =
    Behaviors.receive { (context, message) =>
      message match {
        case ForkTaken(fork, seat) =>
          in_waiting
        case ForkBusy(fork, seat) =>
          fork_denied
        case _ => Behaviors.same
      }
    }

  val in_waiting: Behavior[Philosopher_] =
    Behaviors.receive { (context, message) =>
      message match {
        case ForkTaken(forkToWait, seat) =>
          println(s"${seat.philosopher.path.name} picked up ${seat.leftFork.path.name} and ${seat.rightFork.path.name} and began to eat")
          context.scheduleOnce(5.seconds, context.self, Think(seat))
          eating
        case ForkBusy(fork, seat) =>
          val otherFork = if (seat.leftFork == fork) seat.rightFork else seat.rightFork
          otherFork ! PutFork(seat)
          context.scheduleOnce(10.milliseconds, context.self, Eat(seat))
          thinking
        case _ => Behaviors.same
      }
    }

  val fork_denied: Behavior[Philosopher_] =
    Behaviors.receive { (context, message) =>
      message match {
        case ForkTaken(fork, seat) =>
          fork ! PutFork(seat)
          context.scheduleOnce(10.milliseconds, context.self, Eat(seat))
          thinking
        case ForkBusy(fork, seat) =>
          context.scheduleOnce(10.milliseconds, context.self, Eat(seat))
          thinking
        case _ => Behaviors.same
      }
    }

  val eating: Behavior[Philosopher_] =
    Behaviors.receive { (context, message) =>
      message match {
        case Think(seat) =>
          seat.leftFork ! PutFork(seat)
          seat.rightFork ! PutFork(seat)
          println(s"${seat.philosopher.path.name} put down his forks and began to think")
          context.scheduleOnce(5.seconds, context.self, Eat(seat))
          thinking
        case _ => Behaviors.same
      }
    }
    
  val init: Behavior[Philosopher_] =
    Behaviors.receive { (context, message) => 
      message match {
        case Think(seat) => 
          println(s"${seat.philosopher.path.name} began to think")
          context.scheduleOnce(5.seconds, context.self, Eat(seat))
          thinking
        case _ => Behaviors.same
      }
    }
}
