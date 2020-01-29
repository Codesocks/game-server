package view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import model.ChGame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ClientChompController extends ClientGameController {
	@FXML
	VBox mainVBox;

	@Override
	void updateView() {
		if (game == null) {
			// Game is already over
		} else {
			root.getChildren().clear();

			for (int i = 0; i < game.getWidth(); i++) {
				for (int j = 0; j < game.getHeight(); j++) {
					// Add image for each field.
					ImageView fieldContent = null;
					try {
						if (i == 0 && j == 0) {
							fieldContent = new ImageView(new Image(new FileInputStream("./assets/CH_LEER.png")));
						} else if (game.getPlayerAt(i, j) == null) {
							fieldContent = new ImageView(new Image(new FileInputStream("./assets/CH_CHOCOLATE.png")));
						} else if(game.getPlayerAt(i, j).equals(game.getPlayer1())){
							fieldContent = new ImageView(new Image(new FileInputStream("./assets/CH_WOOD_2.png")));
						} else {
							fieldContent = new ImageView(new Image(new FileInputStream("./assets/CH_WOOD_1.png")));
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					fieldContent.setFitHeight((double) height / game.getHeight());
					fieldContent.setFitWidth((double) width / game.getWidth());
					root.add(fieldContent, i, j);
				}
			}
		}
		
		// Handle win of the game.
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
					alert.setHeaderText("You lost this challenge. Now get home and practice!");
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

	void initializeView() {
		if (game == null) {
			// Game is already over.
		} else {
			root = new GridPane();
			root.setHgap(0); // horizontal gap in pixels
			root.setVgap(0); // vertical gap in pixels
			root.setPadding(new Insets(0, 0, 0, 0)); // margins around the whole grid
			root.setGridLinesVisible(true);

			// Set size of the cells.
			ArrayList<ColumnConstraints> columnConstr = new ArrayList<ColumnConstraints>();
			ArrayList<RowConstraints> rowConstr = new ArrayList<RowConstraints>();
			for (int i = 0; i < game.getWidth(); i++) {
				columnConstr.add(new ColumnConstraints());
				columnConstr.get(i).setPercentWidth(100.0 / game.getWidth());
			}
			for (int i = 0; i < game.getHeight(); i++) {
				rowConstr.add(new RowConstraints());
				rowConstr.get(i).setPercentHeight(100.0 / game.getHeight());
			}
			root.getColumnConstraints().addAll(columnConstr);
			root.getRowConstraints().addAll(rowConstr);
		}

		root.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (game == null) {
					 // Do nothing. Game is already over.
				} else if (game.getCurrentPlayer().equals(game.getPlayer1()) && !game.isWon()) {
					boolean validMove = ((ChGame) game).move(game.getPlayer1(),
							(int) (e.getX() * ((double) game.getWidth() / width)),
							(int) (e.getY() * ((double) game.getHeight() / height)));

					if (validMove && !game.getPlayer2().isComputer()) {
						client.execute("makemv " + (int) (e.getX() * ((double) game.getWidth() / width)) + "-"
								+ (int) (e.getY() * ((double) game.getHeight() / height)) + ";"
								+ game.getPlayer2().getUsername());
					}
					updateView();
				}				
			}
		});

		mainVBox.getChildren().add(root);
		updateView();
	}
}
