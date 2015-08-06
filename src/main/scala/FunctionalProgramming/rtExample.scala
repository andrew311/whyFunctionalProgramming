package FunctionalProgramming

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

sealed trait Candy
object Chocolate extends Candy
object JellyBean extends Candy

object Application {
  def main(args: Array[String]) = {
    println("Hi!")
  }
}

// non-RT



object NonRT {
  class CandyMachine {
    var candies: List[Candy] = List.empty
  
    def addCandy(candy: Candy): Unit = {
      candies = candy :: candies
    }
  
    def giveMeCandy: Option[Candy] = {
      val candyOption = candies.headOption
      candies = candies.drop(1)
      candyOption
    }
  }
  
  // Concurrency
  def concurrencyExample {
    val machine = new CandyMachine
    val futureOne = Future {
      machine.addCandy(JellyBean)
    }
    val futureTwo = Future {
      machine.addCandy(Chocolate)
    }
    val future = for {
      _ <- futureOne
      _ <- futureTwo
    } yield {
      machine.candies
    }
    future.onSuccess {
      case x => println(s"futures done $x")
    }
    println("awaiting")
    Await.result(future, 10.seconds)
  }
  
  concurrencyExample
}

// RT
object RT {
  object CandyMachine {
    val empty = CandyMachine(List.empty[Candy])
  }

  case class CandyMachine(candies: List[Candy]) {
    def addCandy(candy: Candy) {
      CandyMachine(candy :: candies)
    }
  
    def giveMeCandy: (Option[Candy], CandyMachine) = {
      (candies.headOption, CandyMachine(candies.drop(1)))
    }
  }
}

