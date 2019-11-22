package management;

class User {
	private String username;
	private String pwd;
	private boolean online;
	
	User(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
		this.online = true;
	}
	
	User(String username) {
		this.username = username;
		this.online = true;
	}
	
	boolean verifyPWD(String pwd) {
		if(pwd.equals(this.pwd)) {
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
