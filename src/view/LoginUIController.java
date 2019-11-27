package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import management.ClientManagement;
import server.Client;

public class LoginUIController implements Initializable {
	private ClientManagement management = new ClientManagement();
	private Client client = new Client(management);

	@FXML
	private Label loginText; // Used to inform user of invalid logins and to determine current stage.

	@FXML
	private TextField loginUsername;

	@FXML
	private PasswordField loginPwd;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginUsername.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER){
				attemptLogin();
			}
		});

		loginPwd.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER){
				attemptLogin();
			}
		});
	}

	@FXML
	public void attemptLogin() {
		if (checkCredentials()) {
			switchToMain();
		}
	}

	private boolean checkCredentials() {
		// Firstly: check whether credentials are given.
		if (loginUsername.getText().trim().isEmpty() || loginPwd.getText().trim().isEmpty()) {
			loginText.setText("Password and username are not allowed to be empty!");
			return false;

			// Secondly: check whether credentials are valid.
		} else {
			management.setCredentials(loginUsername.getText(), loginPwd.getText());

			try {
				if (client.execute("signin") != 0) {
					loginText.setText("Username or password invalid!");
					return false;
				}
			} catch (Exception e) {
				loginText.setText("Failed login. Check your internet connection!");
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	private void switchToMain() {
		// Switch Scene.
		try {
			// Load second scene
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientUI.fxml"));
			Parent root = loader.load();

			// Get controller of scene2 and pass data.
			ClientUIController controllerOfMainUI = loader.getController();
			controllerOfMainUI.setClientManagement(management);

			// Show scene 2 in this window.
			Stage stage = (Stage) loginText.getScene().getWindow();
			stage.setScene(new Scene(root, 800, 450));
			stage.setTitle("Games by Codesocks / j-bl");
			stage.show();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	// USEFULL, KEEP THIS AS EXAMPLE!
	/*
	 * @FXML public void loadMainAndSendMessage() {
	 * System.out.println("Button pressed."); try { //Load second scene FXMLLoader
	 * loader = new FXMLLoader(getClass().getResource("MainUI.fxml")); Parent root =
	 * loader.load();
	 * 
	 * //Get controller of scene2 MainUIController scene2Controller =
	 * loader.getController(); //Pass whatever data you want. You can have multiple
	 * method calls here scene2Controller.transferMessage("t");
	 * 
	 * //Show scene 2 in new window Stage stage = new Stage(); stage.setScene(new
	 * Scene(root)); stage.setTitle("Second Window"); stage.show(); } catch
	 * (IOException ex) { System.err.println(ex); } }
	 */
}
