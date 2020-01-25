package management;

public class User {
	private String username;
	private String pwd;
	private boolean online;
	private boolean isComputer;

	public boolean isComputer() {
		return isComputer;
	}

	public void setComputer(boolean computer) {
		isComputer = computer;
	}

	User(String username, String pwd) {
		this.username = username;
		this.pwd = pwd;
		this.online = true;
	}
	
	public User(String username) {
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
	
	public String getUsername() {
		return username;
	}
	
	boolean isOnline() {
		return online;
	}

	void setOnline(boolean online) {
		this.online = online;
	}
}
