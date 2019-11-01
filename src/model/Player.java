package model;

/**
 * Player of either a game of chomp or connect four. Can be a computer player or
 * a human player.
 * 
 * @author j-bl (Jan), Codesocks (Christian)
 *
 */
public class Player {
	private String name;
	private boolean isComputer;

	public String getName() {
		return name;
	}

	public boolean isComputer() {
		return isComputer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setComputer(boolean computer) {
		isComputer = computer;
	}
}
