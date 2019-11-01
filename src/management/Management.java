package management;

import java.util.HashMap;

public class Management {
	private HashMap<String, User> users = new HashMap<String, User>();
	
	public boolean addUser(String username, String pwd) {
		if(pwd == null || username == null || users.containsKey(username)) {
			return false;
		} else {
			users.put(username, new User(username, pwd));
			return true;
		}
	}
	
	public boolean verifyUser(String username, String pwd) throws UserNotFoundException {
		if(pwd == null || username == null) {
			return false;
		
		// If user does not exist, a new user is added.
		} else if(!users.containsKey(username)) {
			// throw new UserNotFoundException("There is no user with this name registered!");
			addUser(username, pwd);
			return true;
		} else if(users.get(username).verifyPWD(pwd)){
			return true;
		} else {
			return false;
		}
	}
	
}
