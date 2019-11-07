package management;

class User {
	private String username;
	// NOT save! Add propper HASHING here!!!!.
	private String pseudopassword;
	private boolean online;
	
	User(String username, String pwd) {
		this.username = username;
		this.pseudopassword = pwd;
		this.online = true;
	}
	
	boolean verifyPWD(String pwd) {
		if(pwd.equals(pseudopassword)) {
			return true;
		} else {
			return false;
		}
	}
	
	String getUsername() {
		return username;
	}
	
	boolean isOnline() {
		return online;
	}

	void setOnline(boolean online) {
		this.online = online;
	}
}
