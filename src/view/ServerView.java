package view;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class ServerView extends Application {
	private StackPane rootPane = new StackPane();
	private static double HEIGHT = 400;
	private static double WIDTH = 800;

	// Scene scene = new Scene(rootPane, 400, 300);

	@Override
	public void start(Stage primaryStage) {
		//Label label = new Label("This is a label on a rootPane");
		//rootPane.getChildren().add(label);

		VBox mainVBox = new VBox();

		// LEFT PANEL ==================================================================================================
		ScrollPane leftPane = new ScrollPane();
		leftPane.setMaxSize(WIDTH / 3, HEIGHT);
		leftPane.setMinHeight(HEIGHT);
		leftPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Always show vertical scroll bar
		leftPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Horizontal scroll bar is only displayed when needed

		VBox leftVBox = new VBox();
		leftVBox.getChildren().addAll(new Label("Spieler A"), new Label("Spieler 2"), new Label("Spieler 3"), new Label("Spieler 4"));

		leftPane.setContent(leftVBox); // Set content for ScrollPane
		mainVBox.getChildren().add(leftPane);

		// =============================================================================================================
		// .setContent(label); // Set content for ScrollPane

		Scene scene = new Scene(mainVBox, WIDTH, HEIGHT);

		primaryStage.setTitle("Codesock's and j-bl's awesome slot machine");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override public void stop() {
	}

	public static void main(String[] args) {
		launch(args);
	}

}