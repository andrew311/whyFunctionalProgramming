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


// After: let's minimize the surface area of side effects, splitting business logic from side effects
def loginRequestHandler(params: Map[String, String]): String {
	val userParamOption: Option[String] = params("user")
	val validUserOption = pureLoginLogic(userParamOption, params("password"), userParamOption.map(DB.findUser))
	validUserOption.fold("<html>bad username and/or password</html>")("<html>logged in!</html>")
}

def pureLoginLogic(userParamOption: Option[String], passwordParamOption: Option[String], userOption: Option[User]): Option[User] = {
	for {
		userParam <- userParamOption
		passwordParam <- passwordParamOption
		user <- userOption if isValidUserAndPassword(userParam, passwordParam, user)
	} yield {
		user
	}
}

def isValidUserAndPassword(username: String, password: String, user: User): Boolean = {
	username == user.name && crypt(password) == user.password_crypted
}
