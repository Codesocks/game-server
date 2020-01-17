package view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import management.ClientManagement;
import management.GameInvitation;
import model.Game;
import server.Client;

public class ClientUIController implements Initializable {
	private ClientManagement management = new ClientManagement();
	private Client client = new Client(management);

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
	public void displayTxt() {
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
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("CHOMP - Choose your opponent!");
		alert.setHeight(800);
		alert.setHeaderText("Do you want to play against a computer or the currently selected player?");

		ButtonType buttonTypeHuman = new ButtonType("Human");
		ButtonType buttonTypeComputer = new ButtonType("Computer");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeHuman, buttonTypeComputer, buttonTypeCancel);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField widthBoard = new TextField("9");
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
				invitePlayer(Game.GAME_CHOMP, width, height);
			} else {
				System.out.println("COMPUTER");
			}
		} else {
			// User pressed cancel or closed dialogue...
		}
	}

	@FXML
	public void playConnectFour() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Connect Four - Choose your opponent!");
		alert.setHeight(800);
		alert.setHeaderText("Do you want to play against a computer or the currently selected player?");

		ButtonType buttonTypeHuman = new ButtonType("Human");
		ButtonType buttonTypeComputer = new ButtonType("Computer");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeHuman, buttonTypeComputer, buttonTypeCancel);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField widthBoard = new TextField("9");
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
				invitePlayer(Game.GAME_CONNECTFOUR, width, height);
			} else {
				System.out.println("COMPUTER");
			}
		} else {
			// User pressed cancel or closed dialogue...
		}
	}

	private void invitePlayer(long game, int width, int height) {
		try {
			if (menueUserList.getSelectionModel().getSelectedItem() != null) {
				System.out.println("Attempting to invite @" + menueUserList.getSelectionModel().getSelectedItem()
						+ " to a new game.");
				client.execute("invite " + game + "-4-7" + ";" + menueUserList.getSelectionModel().getSelectedItem());
			} else {
				// Play against a computer player
				System.out.println("try to play against computer. not yet implemented.");
			}
		} catch (Exception e) {
			System.out.println("Failed to send message!");
		}
	}

	public void setClientManagement(ClientManagement management) {
		this.management = management;
		client = new Client(this.management);
		client.execute("update");

		// Create Updater.
		System.out.println(management.getCredentials().toString());
		Thread updater = new Thread(new ClientUIUpdater(this));
		updater.start();
	}

	Client getClient() {
		return client;
	}

	void openGameDialogue() {
		GameInvitation invitation = client.getManagement().getReceivedInvitations()
				.get(client.getManagement().getReceivedInvitations().size() - 1);
		System.out.println("Long. " + invitation.getGame());

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("CHALLENGE!");
		alert.setHeight(800);
		alert.setHeaderText("You received a challenge by @" + invitation.getFromUsername() + " to a game of "
				+ (invitation.getGame() == Game.GAME_CHOMP ? "Chomp" : "Connect Four")
				+ "! Do you dare to accept that challenge?");

		ButtonType buttonTypeAccept = new ButtonType("ACCEPT");
		ButtonType buttonTypeDecline = new ButtonType("DECLINE", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeDecline);

		Optional<ButtonType> result = alert.showAndWait();
		// Choose a player to play against.
		if (result.get() == buttonTypeAccept) {
			openMainGame(invitation.getGame());
		} else if (result.get() == buttonTypeDecline) {
			System.out.println("DECLINE");
		} else {
			// User pressed cancel or closed dialogue...
		}

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
			Text text = new Text();
			text.wrappingWidthProperty().bind(leftChatList.widthProperty().subtract(15));
			text.setText("@" + message[0] + ": " + message[1]);
			chatMessages.add(text);
		}
		leftChatList.setItems(chatMessages);
		leftChatList.refresh();
	}

	private void openMainGame(long game) {
		// Load second scene
		FXMLLoader loader;
		Parent root;
		if (game == Game.GAME_CHOMP) {
			loader = new FXMLLoader(getClass().getResource("ClientUICh.fxml"));
		} else {
			loader = new FXMLLoader(getClass().getResource("ClientUICF.fxml"));
		}

		try {
			root = loader.load();
			// Get controller of the game scene.
			ClientGameController gameController = loader.getController();

			// Pass whatever data you want. You can have multiple method calls here
			// scene2Controller.transferMessage("t");

			Stage stage = (Stage) new Stage();
			stage.setScene(new Scene(root, 800, 450));
			stage.setTitle(game == Game.GAME_CHOMP ? "CHOMP CHOMP CHOMP" : "Connect Four");
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					try {
						// client.execute("signout");
						// add aufgeben
					} catch (Exception e) {
						// System.out.println("Failed to sign out!");
						e.printStackTrace();
					}
					Platform.exit();
				}
			});
			stage.show();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
