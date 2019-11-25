package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import management.ClientManagement;
import server.Client;

public class Main extends Application {
	private Stage primaryStage;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		// Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml"));
		Parent root = FXMLLoader.load(getClass().getResource("LoginUI.fxml"));
		// Parent root = FXMLLoader.load(getClass().getResource("MainUI2.fxml"));
		Scene scene = new Scene(root, 800, 450);
    
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	public void changeScene(String fxml) throws IOException{
	    Parent pane = FXMLLoader.load(
	           getClass().getResource(fxml));

	   primaryStage.getScene().setRoot(pane);
	}
}
