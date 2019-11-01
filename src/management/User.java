package management;

class User {
	private String username;
	// NOT save! Add propper HASHING here!!!!.
	private String pseudopassword;
	
	User(String username, String pwd) {
		this.username = username;
		this.pseudopassword = pwd;
	}
	
	String getUsername() {
		return username;
	}
	
	boolean verifyPWD(String pwd) {
		if(pwd.equals(pseudopassword)) {
			return true;
		} else {
			return false;
		}
	}
}
