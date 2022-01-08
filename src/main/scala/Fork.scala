import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import scala.concurrent.duration._


object Fork {
  import Philosopher._

  sealed trait Fork_

  final case class PutFork(seat: SeatTable) extends Fork_
  final case class TakeFork(seat: SeatTable) extends Fork_

  val available: Behavior[Fork_] =
    Behaviors.receive { (context, message) =>
      message match {
        case TakeFork(seat) =>
          seat.philosopher ! ForkTaken(context.self, seat)
          Fork.taken
        case _ => Behaviors.same
      }
    }

  val taken: Behavior[Fork_] =
    Behaviors.receive { (context, message) =>
      message match {
        case TakeFork(otherSeat) =>
          otherSeat.philosopher ! ForkBusy(context.self, otherSeat)
          Behaviors.same
        case PutFork(seat) => Fork.available
        case _ => Behaviors.same
      }
    }
}

