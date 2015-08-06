package FunctionalProgramming
import Crypt._

case class User(val name: String, val cryptedPassword: String)

object DB {
  def findUser(name: String) = Some(User(name, "fakeCrypt"))
}

object Crypt {
  def crypt(s: String) = s
}

object WebExample {
  val successHtml = "<html>logged in!</html>"
  val failHtml = "<html>bad username and/or password</html>"
  /*
  * This is an example of taking code and making it more "functional"
  */

  // Before: typical login handler in a web service
  def loginRequestHandler(params: Map[String, String]): String = {
    val userOption = params.get("user").flatMap(DB.findUser)
    userOption match {
      case Some(user) if crypt(params("password")) == user.cryptedPassword =>
        successHtml
      case None =>
        failHtml
    }
  }


  // After: let's minimize the surface area of side effects, splitting business logic from side effects
  def loginRequestHandlerFP(params: Map[String, String]): String = {
    val user = for {
      username <- params.get("user")
      password <- params.get("password")
      user <- DB.findUser(username) if isValidUserAndPassword(username, password, user)
    } yield {
      user
    }

    user.map(_ => successHtml).getOrElse(failHtml)
  }
  def isValidUserAndPassword(username: String, password: String, user: User): Boolean = {
    username == user.name && crypt(password) == user.cryptedPassword
  }
}
