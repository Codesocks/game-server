package management;

public class User {
	private String username;
	// NOT save! Add propper HASHING here!!!!.
	private String pseudopassword;
	
	public User(String username, String pwd) {
		this.username = username;
		this.pseudopassword = pwd;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean checkPWD(String pwd) {
		if(pwd.equals(pseudopassword)) {
			return true;
		} else {
			return false;
		}
	}
}
