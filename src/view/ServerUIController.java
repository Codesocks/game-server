package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import management.ServerManagement;
import server.Server;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerUIController implements Initializable {
	private ServerManagement management;
	Server server;
	
	@FXML
	ListView<String> leftUserList;
	@FXML
	ListView<String> rightLogList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		// Starts a new server and a new updater.
		server = new Server();
		management = server.getManagement();
		
		Thread s = new Thread(server);
		s.start();
		(new Thread(new ServerUIUpdater(this))).start();
	}
	
	void loadLog() {
		ObservableList<String> logMessages = FXCollections.observableArrayList();
		logMessages.addAll(server.getLogs());

		rightLogList.setItems(logMessages);
		rightLogList.refresh();
	}
	
	void loadUser() {
		ObservableList<String> userList = FXCollections.observableArrayList();
		userList.addAll(management.getUsersOnline());

		leftUserList.setItems(userList);
		leftUserList.refresh();
	}
}
