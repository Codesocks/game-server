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
	 * This method is called whenever the opponent makes a new move. It updates the
	 * current displayment of the game to the changed data of the game.
	 */
	abstract void updateView();
	
	abstract void initializeView();

	void setClientManagement(ClientManagement management) {
		this.management = management;
		this.game = management.getGame();
	}
	
	void setClient(Client client) {
		this.client = client;
	}

	void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
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
	
	void closeWindow() {
		Stage currentStage = (Stage) root.getScene().getWindow();
		currentStage.close();
	}
	
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
}
