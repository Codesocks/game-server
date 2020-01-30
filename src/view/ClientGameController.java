package view;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import management.ClientManagement;
import model.Game;
import server.Client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for a game. This game can be for instance of game of chomp or a
 * game of connect four.
 */
abstract class ClientGameController implements Initializable {
	Game game;
	Client client;
	ClientManagement management;
	int width;
	int height;
	GridPane root;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	/**
	 * This method is to be called whenever the opponent makes a new move. It
	 * updates the current displayment of the game to the changed data of the game.
	 */
	abstract void updateView();

	/**
	 * This method is to be called when the view of the game is initialized.
	 */
	abstract void initializeView();

	/**
	 * Sets the ClientManagement of this game to the given management.
	 * 
	 * @param management Management.
	 */
	void setClientManagement(ClientManagement management) {
		this.management = management;
		this.game = management.getGame();
	}

	/**
	 * Sets the Client of this game to the given Client.
	 * 
	 * @param client Client.
	 */
	void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Sets the size of this game. This refers to the size of the window measured in
	 * pixels.
	 * 
	 * @param width  Width of the Window.
	 * @param height Height of the window.
	 */
	void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Attempts to close the currently running game. If it is an online game, this
	 * termination is tranferred to the server, too.
	 */
	void closeGame() {
		if (game != null && !game.getPlayer2().isComputer()) {
			try {
				client.execute("surrender ;" + game.getPlayer2().getUsername());
			} catch (Exception e) {
				System.out.println("Failed to terminate the game.");
				e.printStackTrace();
			}
		}
		game = null;
		management.closeGame();
	}

	/**
	 * Closes the currently open view of the game.
	 */
	void closeWindow() {
		Stage currentStage = (Stage) root.getScene().getWindow();
		currentStage.close();
	}

	/**
	 * Is to be called if the opponent surrendered. The user is informed, that they
	 * won the game due to this surrender.
	 */
	void opponentSurrendered() {
		// Empty game, so no further move can be made.
		game = null;
		management.closeGame();

		// Show alert stating the surrender.
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("A WINNER WAS FOUND!");
		alert.setHeight(800);
		try {
			alert.setHeaderText("Your opponend surrendered to your apparently godlike powers!");
			ImageView trophy = new ImageView(new Image(new FileInputStream("./assets/TROPHY.png")));
			trophy.setFitHeight(65);
			trophy.setPreserveRatio(true);
			alert.setGraphic(trophy);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		alert.showAndWait();

		// User pressed ok or closed the dialogue.
		closeWindow();
	}

	/**
	 * Checks whether the game is already won and deals with that information
	 * accordingly.
	 */
	void handleWin() {
		if (game != null && game.isWon()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("A WINNER WAS FOUND!");
			alert.setHeight(800);
			try {
				if (game.getWinner().equals(game.getPlayer1())) {
					alert.setHeaderText("Congratulations! You have just won a decisive victory!");
					ImageView trophy = new ImageView(new Image(new FileInputStream("./assets/TROPHY.png")));
					trophy.setFitHeight(65);
					trophy.setPreserveRatio(true);
					alert.setGraphic(trophy);
				} else {
					alert.setHeaderText("You lost this challenge. Now go home and practice!");
					ImageView trophy = new ImageView(new Image(new FileInputStream("./assets/TROPHY.png")));
					trophy.setFitHeight(65);
					trophy.setPreserveRatio(true);
					alert.setGraphic(trophy);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			closeGame();
			alert.showAndWait();

			// User pressed ok or closed the dialogue.
			closeWindow();
		}
	}
}
