package cn.edu.thu.disim4j;

	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	public static Stage mainStage;
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = (VBox)FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("DiSim4J v1.0");
			primaryStage.show();
			scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					if(event.equals(WindowEvent.WINDOW_CLOSE_REQUEST)){
						System.exit(0);
					}
				}
			});
			mainStage=primaryStage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
