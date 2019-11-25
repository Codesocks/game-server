package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import management.ClientManagement;
import server.Client;

public class MainUIController implements Initializable {
	// Interne Variable.
	private ClientManagement management;
	private Client client;

	// FXML-Zugriff.
	@FXML
	private Label leftLogoTxt; // Text des Logos.
	@FXML
	TextArea leftTextArea; // Texteingabe Nachrichten.
	@FXML
	ListView<String> leftUserList;
	@FXML
	ListView<String> leftChatList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Selected item in list with usernames.
		leftUserList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, final String oldvalue, final String newvalue) {
				System.out.println("User selected: " + leftUserList.getSelectionModel().getSelectedItem());
				openChat(leftUserList.getSelectionModel().getSelectedItem());
			}
		});
		
		//updateUserData();
	}
	
	@FXML
	public void displayTxt() {
		leftLogoTxt.setText(leftTextArea.getText());
		System.out.println();
	}
	
	@FXML
	public void sendMsg() {
		try {
			if(!leftTextArea.getText().trim().isEmpty() && leftUserList.getSelectionModel().getSelectedItem() != null) {
				client.execute("sendmsg " + leftTextArea.getText() + ";" + leftUserList.getSelectionModel().getSelectedItem());
				leftTextArea.setText("");
			} else {
				System.out.println("No receiving user selected or message empty. Therefore message will NOT be send.");
			}
		} catch(Exception e) {
			System.out.println("Failed to send message!");
		}
	}

	public void setClientManagement(ClientManagement management) {
		this.management = management;
		client = new Client(this.management);
		client.execute("update");
		updateUserData();
	}

	private void updateUserData() {
		ObservableList<String> userList = FXCollections.observableArrayList("First word");
		for (String username : management.getUsersOnline())
			userList.add(username);

		leftUserList.setItems(userList);
		leftUserList.refresh();
	}
	
	private void openChat(String username) {
		ObservableList<String> chatMessages = FXCollections.observableArrayList();
		for (String[] message : management.getMessages(username)) {
			chatMessages.add(message[1]);
		}
		leftChatList.setItems(chatMessages);
		leftChatList.refresh();
	}
}
