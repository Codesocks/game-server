package view;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class MainView extends Application {
	@Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("main_view.fxml"));
    
        Scene scene = new Scene(root, 800, 450);
    
        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }

	public static void main(String[] args) {
		launch(args);
	}
}