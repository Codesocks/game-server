package management;

public class User {
    private String username;
    private String pwd;
    private boolean online;
    private boolean isComputer;

    public User(String username) {
        this.username = username;
        this.online = true;
    }

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

    public boolean isComputer() {
        return isComputer;
    }

    public void setComputer(boolean computer) {
        isComputer = computer;
    }

    boolean isOnline() {
        return online;
    }

    void setOnline(boolean online) {
        this.online = online;
    }

    public String getUsername() {
        return username;
    }
}
