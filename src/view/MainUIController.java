package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@FXML
	public void displayTxt() {
		leftLogoTxt.setText(leftTextArea.getText());
		updateUserData();
	}

	public void setClientManagement(ClientManagement management) {
		this.management = management;
		client = new Client(this.management);
		client.execute("update");
		updateUserData();
	}

	private void updateUserData() {
		ObservableList<String> userList = FXCollections.observableArrayList("First word");
		for (String username: management.getUsersOnline())
			userList.add(username);
			
		leftUserList.setItems(userList);
		leftUserList.refresh();
	}
}
