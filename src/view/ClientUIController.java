package view;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import management.ClientManagement;
import management.GameInvitation;
import model.ChGame;
import model.Game;
import server.Client;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientUIController implements Initializable {
    private ClientManagement management;
    private Client client;
    private ClientGameController gameController;

    @FXML
    private Label leftLogoTxt; // Text of logo.
    @FXML
    GridPane chatArea;
	@FXML
	ScrollPane chatView;
	@FXML
	TextArea leftTextArea; // Text input for messages.
	@FXML
	ListView<Text> leftChatList;
	@FXML
	Button leftButtonContacts;

	@FXML
	private Button menueButtonCollapse;
	@FXML
	private VBox menueVBox;
	@FXML
	private StackPane menueStackPane;
	@FXML
	ListView<String> menueUserList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Menue.
		menueStackPane.setVisible(false);
		menueStackPane.setPickOnBounds(false);
		menueUserList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, final String oldvalue, final String newvalue) {
				System.out.println("User selected: " + menueUserList.getSelectionModel().getSelectedItem());
				openSelectedChat();
			}
		});

		// Send messages when key 'enter' is pressed.
		leftTextArea.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				sendMsg();
				leftTextArea.setText("");
				leftTextArea.clear();
			}
		});
	}

	@FXML
	public void collapseMenue() {
		FadeTransition hideFileRootTransition = new FadeTransition(Duration.millis(500), menueVBox);
		hideFileRootTransition.setFromValue(1.0);
		hideFileRootTransition.setToValue(0.0);
		hideFileRootTransition.play();
		menueStackPane.setVisible(false);
	}

	@FXML
	public void showMenue() {
		// Update the list of contacts.
		ObservableList<String> userList = FXCollections.observableArrayList();
		for (String username : management.getUsersOnline()) {
			userList.add(username);
		}
		menueUserList.setItems(userList);
		menueUserList.refresh();

		menueStackPane.setVisible(true);
		FadeTransition hideFileRootTransition = new FadeTransition(Duration.millis(500), menueVBox);
		hideFileRootTransition.setFromValue(0.0);
		hideFileRootTransition.setToValue(1.0);
		hideFileRootTransition.play();
	}

	@FXML
	public void sendMsg() {
		try {
			if (!leftTextArea.getText().trim().isEmpty()
					&& menueUserList.getSelectionModel().getSelectedItem() != null) {
				client.execute("sendmsg " + leftTextArea.getText() + ";"
						+ menueUserList.getSelectionModel().getSelectedItem());

				openSelectedChat(); // Update Chat.
				leftTextArea.setText("");
			} else {
				System.out.println("No receiving user selected or message empty. Therefore message will NOT be send.");
			}
		} catch (Exception e) {
			System.out.println("Failed to send message!");
		}
	}

	@FXML
	public void playChomp() {
		openGameInvitationDialogue(Game.GAME_CHOMP);
	}

	@FXML
	public void playConnectFour() {
		openGameInvitationDialogue(Game.GAME_CONNECTFOUR);
	}

	public void openMainGame() {
		// Load second scene
		FXMLLoader loader;
		Parent root;
		if (management.getGame() instanceof ChGame) {
			loader = new FXMLLoader(getClass().getResource("ClientChomp.fxml"));
		} else {
			loader = new FXMLLoader(getClass().getResource("ClientConnectfour.fxml"));
		}

		try {
			// Get controller for the stage of the game. Pass necessary data to the
			// controller of the new stage.
			root = loader.load();
			gameController = loader.getController();
			gameController.setClient(client);
			gameController.setClientManagement(management);

			// Compute size.
			int width, height;
			if (management.getGame().getHeight() < 11 && management.getGame().getWidth() < 15) {
				height = 100 * management.getGame().getHeight();
				width = 100 * management.getGame().getWidth();
			} else {
				if (management.getGame().getWidth() >= management.getGame().getHeight()) {
                    width = 1000;
                    height = (int) ((1000.0 / management.getGame().getWidth()) * management.getGame().getHeight());
                } else {
                    height = 1000;
                    width = (int) ((1000.0 / management.getGame().getHeight()) * management.getGame().getWidth());
                }
            }
            gameController.setDimensions(width, height);

            // Create new stage for game.
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle((management.getGame() instanceof ChGame ? "CHOMP" : "CONNECT FOUR")
                    + " - Games by Codesocks / j-bl");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    gameController.closeGame();
                    gameController.closeWindow();
                }
            });
			stage.show();
			gameController.initializeView();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void updateGameView() {
		gameController.updateView();
	}

	void setClientManagement(ClientManagement management) {
		this.management = management;
		client = new Client(this.management);
		client.execute("update");

		// Create Updater.
		System.out.println(management.getCredentials().toString());
		Thread updater = new Thread(new ClientUIUpdater(this, management));
		updater.start();
	}

	Client getClient() {
		return client;
	}

	void openGameAcceptationDialogue() {
		GameInvitation invitation = management.getReceivedInvitations()
				.get(management.getReceivedInvitations().size() - 1);
		System.out.println("Long. " + invitation.getGame());

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("CHALLENGE!");
		alert.setHeight(800);
		alert.setHeaderText("You received a challenge by @" + invitation.getFromUsername() + " to a game of "
				+ (invitation.getGame() == Game.GAME_CHOMP ? "Chomp" : "Connect Four") + " on a "
				+ invitation.getWidth() + "x" + invitation.getHeight()
				+ "-board! Do you dare to accept that challenge?");

		ButtonType buttonTypeAccept = new ButtonType("ACCEPT");
		ButtonType buttonTypeDecline = new ButtonType("DECLINE", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeDecline);

		Optional<ButtonType> result = alert.showAndWait();
		// Choose a player to play against.
		if (result.get() == buttonTypeAccept) {
			long errorCode = client.execute("accept " + invitation.getGame() + "-" + invitation.getWidth() + "-"
					+ invitation.getHeight() + ";" + invitation.getFromUsername());
			if (errorCode == 0) {
				management.setGame(invitation.getGame(), invitation.getWidth(), invitation.getHeight(),
						invitation.getFromUsername(), true);
				openMainGame();
			}
		} else if (result.get() == buttonTypeDecline) {
			System.out.println("DECLINE");
		} else {
			// User pressed cancel or closed dialogue...
		}
		management.closeInvitation(invitation);
	}

	void openSelectedChat() {
		String username = menueUserList.getSelectionModel().getSelectedItem();

		// Show empty plane if no user is selected.
		if (username == null) {
			chatArea.setVisible(false);
			chatView.setVisible(false);
			return;
		}

		// Make the corresponding chat visible if user is selected.
		chatArea.setVisible(true);
		chatView.setVisible(true);

		ObservableList<Text> chatMessages = FXCollections.observableArrayList();
		for (String[] message : management.getMessages(username)) {
			// Do not show internal messages.
			if (message[1].length() < 2 || !message[1].substring(0, 2).equals("$$")) {
				Text text = new Text();
				text.wrappingWidthProperty().bind(leftChatList.widthProperty().subtract(15));
				text.setText("@" + message[0] + ": " + message[1]);
				chatMessages.add(text);
			}
		}
		leftChatList.setItems(chatMessages);
		leftChatList.refresh();
	}

	private void openGameInvitationDialogue(long game) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle((game == Game.GAME_CHOMP ? "CHOMP" : "CONNECT FOUR") + " - Choose your opponent!");
		alert.setHeight(800);

		// Set currently fitting alert text.
		ButtonType buttonTypeHuman = new ButtonType("Human");
		ButtonType buttonTypeComputer = new ButtonType("Computer");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		if (menueUserList.getSelectionModel().getSelectedItem() != null) {
			alert.setHeaderText("Do you want to play against a computer or the currently selected player?");
			alert.getButtonTypes().setAll(buttonTypeHuman, buttonTypeComputer, buttonTypeCancel);
		} else {
			alert.setHeaderText(
					"Do you want to play against the computer? If not press cancel and select a player to play against.");
			alert.getButtonTypes().setAll(buttonTypeComputer, buttonTypeCancel);
		}

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField widthBoard = new TextField("8");
		TextField heightBoard = new TextField("5");

		grid.add(new Label("Width:"), 0, 0);
		grid.add(widthBoard, 1, 0);
		grid.add(new Label("Height:"), 0, 1);
		grid.add(heightBoard, 1, 1);
		alert.getDialogPane().setContent(grid);

		// If board sizes invalid do something to display error.
		// Wait for user to choose a player to play against.
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeHuman || result.get() == buttonTypeComputer) {
			// Check inputs for validity.
			int width = 7;
			int height = 6;
			try {
				width = Integer.valueOf(widthBoard.getText());
				height = Integer.valueOf(heightBoard.getText());

				if (width < 1 || width >= 999 || height < 1 || height > 999) {
					System.out.println("Width and height of the board may not exceed 999 or 1.");
					return;
				}
			} catch (Exception e) {
				System.out.println("Invalid parameters for width or height of the board!");
				return;
			}

			// Choose a player to play against.
			if (result.get() == buttonTypeHuman) {
				invitePlayer(game, width, height);
			} else {
				// Computer opponent.
				management.setGame(game, width, height, true);
				openMainGame();
			}
		} else {
			// User pressed cancel or closed dialogue...
		}
	}

	private void invitePlayer(long game, int width, int height) {
		try {
			if (menueUserList.getSelectionModel().getSelectedItem() != null) {
				System.out.println("Attempting to invite @" + menueUserList.getSelectionModel().getSelectedItem()
						+ " to a new game of " + (game == Game.GAME_CHOMP ? "chomp" : "connect four") + ".");
				client.execute("invite " + game + "-" + width + "-" + height + ";"
						+ menueUserList.getSelectionModel().getSelectedItem());
			} else {
				throw new IllegalArgumentException("You have to choose a player to invite!");
			}
		} catch (Exception e) {
			System.out.println("Failed to send message!");
		}
	}

	public void opponentSurrendered() {
		if (gameController != null) {
			gameController.opponentSurrendered();
		}
	}
}
