/*
 * This is an example of taking a code of code and making it more "functional style"
 */

// Before: typical login handler in a web service
def loginRequestHandler(val params: Map[String, String]): String = {
	val user = DB.findUser(params("user")) // external state dependency
	user match {
		case Some(user) if crypt(params("password")) == user.password_crypted =>
			setLoggedInUser(user)  // side effect
			"<html>logged in!</html>"
		case None =>
			"<html>bad username and/or password</html>"
	}
}