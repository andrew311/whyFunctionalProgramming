// non-RT

class CandyMachine {
  var candies: List[Candy] = List.empty()
  
  def addCandy(candy: Candy): Unit = {
    candies = candy :: candies
  }
  
  def giveMeCandy: Option[Candy] = {
    candies match {
      case head :: tail =>
        candies = tail
        head
      case _ => None
    }
  }
}

// RT

case class CandyMachine(candies: List[Candy]) {
  def addCandy(candy: Candy) {
    CandyMachine(candy :: candies)
  }
  
  def giveMeCandy: (Option[Candy], CandyMachine) = {
    (candies.headOption, CandyMachine(candies.drop(1)))
  }
}