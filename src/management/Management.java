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
	public void verifyUser() {
		
	}
	
}
