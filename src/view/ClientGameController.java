package view;

import javafx.fxml.Initializable;
import model.Game;
import server.Client;

import java.net.URL;
import java.util.ResourceBundle;

abstract class ClientGameController implements Initializable {
	private Game game;
	private Client client;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	/**
	 * This method is called whenever the opponent makes a new move. It updates the
	 * current displayment of the game to the changed data of the game.
	 */
	abstract void updateView();

	void setGame(Game game) {
		this.game = game;
		updateView();
	}

	void setClient(Client client) {
		this.client = client;
	}
}
