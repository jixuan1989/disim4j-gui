package cn.edu.thu.disim4j;

	
import cn.edu.thu.disim4j.elements.PlaceController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	public static Stage mainStage;
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
			VBox root = (VBox)fxmlLoader.load();
			MainController controller=(MainController)fxmlLoader.getController();
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
			mainStage.addEventFilter(KeyEvent.KEY_PRESSED, controller.keyTypedHandler);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
