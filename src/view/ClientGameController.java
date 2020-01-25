package view;

import javafx.fxml.Initializable;
import model.Game;
import server.Client;

import java.net.URL;
import java.util.ResourceBundle;

abstract class ClientGameController implements Initializable {
	Game game;
	Client client;
	int width;
	int height;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	/**
	 * This method is called whenever the opponent makes a new move. It updates the
	 * current displayment of the game to the changed data of the game.
	 */
	abstract void updateView();
	
	abstract void initializeView();

	void setGame(Game game) {
		this.game = game;
	}

	void setClient(Client client) {
		this.client = client;
	}

	void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
