package management;

/**
 * User that can register at the server and play games.
 */
public class User {
	private String username;
	private String pwd;
	private boolean online;
	private boolean isComputer;

	/**
	 * Creates a new user with the given username.
	 * 
	 * @param username Username.
	 */
	public User(String username) {
		this.username = username;
		this.online = true;
	}

	/**
	 * Creates a new User with the given username and password.
	 * 
	 * @param username Username.
	 * @param pwd      Password.
	 */
	User(String username, String pwd) {
		this(username);
		this.pwd = pwd;
	}

	/**
	 * Checks if given password equals user's password.
	 *
	 * @param pwd Password to be verified.
	 * @return True if password equals user's password.
	 */
	boolean verifyPWD(String pwd) {
		return pwd.equals(this.pwd);
	}

	/**
	 * Returns whether this user is a real user or controlled by the computer.
	 * 
	 * @return Whether this is user is controlled by the computer.
	 */
	public boolean isComputer() {
		return isComputer;
	}

	/**
	 * If {@code true} sets the user to being controlled by the computer.
	 * 
	 * @param computer Whether this user shall be controlled by the computer.
	 */
	public void setComputer(boolean computer) {
		isComputer = computer;
	}

	/**
	 * Returns the username of this user.
	 * 
	 * @return Username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns whether this is user is currently online.
	 * 
	 * @return Whether user is currently online.
	 */
	boolean isOnline() {
		return online;
	}

	/**
	 * If {@code true} this user is set to being currently online.
	 * 
	 * @param online Whether user is currently online.
	 */
	void setOnline(boolean online) {
		this.online = online;
	}
}
