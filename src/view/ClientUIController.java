package view;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import management.ClientManagement;
import model.Game;
import server.Client;

public class ClientUIController implements Initializable {
	private ClientManagement management = new ClientManagement();
	private Client client = new Client(management);

	@FXML
	private Label leftLogoTxt; // Text of logo.
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
		try {
			if (menueUserList.getSelectionModel().getSelectedItem() != null) {
				client.execute("invite " + Game.GAME_CHOMP + ";" + menueUserList.getSelectionModel().getSelectedItem());
			} else {
				System.out.println(
						"No receiving user selected or message empty. Therefore game invitation for chomp will NOT be send.");
			}
		} catch (Exception e) {
			System.out.println("Failed to send message!");
		}
	}

	@FXML
	public void playConnectFour() {
		try {
			if (menueUserList.getSelectionModel().getSelectedItem() != null) {
				client.execute(
						"invite " + Game.GAME_CONNECTFOUR + ";" + menueUserList.getSelectionModel().getSelectedItem());
			} else {
				System.out.println(
						"No receiving user selected or message empty. Therefore game invitation for CF will NOT be send.");
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
		// Load second scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginUI.fxml"));
		Parent root;
		try {
			root = loader.load();

			// Get controller of scene2
			LoginUIController scene2Controller = loader.getController();
			// Pass whatever data you want. You can have multiple method calls here

			// scene2Controller.transferMessage("t");

			Stage stage = (Stage) new Stage();
			stage.setScene(new Scene(root, 800, 450));
			stage.setTitle("Games by Codesocks / j-bl");
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					try {
						client.execute("signout");
					} catch (Exception e) {
						System.out.println("Failed to sign out!");
						e.printStackTrace();
					}
					Platform.exit();
					System.exit(0);
				}
			});
			stage.show();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	void openSelectedChat() {
		String username = menueUserList.getSelectionModel().getSelectedItem();

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
}