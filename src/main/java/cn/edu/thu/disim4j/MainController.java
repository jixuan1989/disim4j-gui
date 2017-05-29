package cn.edu.thu.disim4j;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cn.edu.thu.disim4j.elements.PlaceController;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MainController implements Initializable, GlobalController{
	@FXML 
	Pane paintPane;
	@FXML
	ScrollPane scrolPane;
	@FXML
	Circle leftPlace;
	@FXML
	Rectangle leftRect;
	@FXML
	Line leftLine;
	@FXML
	Button compileButton;
	@FXML
	Button startButton;
	@FXML
	Button resetButton;
	@FXML
	Button addHButton;
	@FXML
	Button addVButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addOnePlace();
		addHButton.setOnAction( (EventHandler<ActionEvent>) e->{
			paintPane.setMinWidth(paintPane.getWidth()+500);
		});
		addVButton.setOnAction( (EventHandler<ActionEvent>) e->{
			paintPane.setMinHeight(paintPane.getHeight()+500);
		});
		leftPlace.setOnMouseClicked(leftCircleOnMouseClickedEventHandler);
	}
	public void  initCPN(){

	}
	public void  autoRun(){

	}
	public void  nextStep(){

	}
	public void  loadCPN(){

	}
	public void  pause(){

	}
	public void  loadCql(){

	}

	private void addOnePlace(){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("elements/place.fxml"));
		try {
			Group serverRoot = (Group)fxmlLoader.load();
			PlaceController serverController=(PlaceController)fxmlLoader.getController();
			serverController.addListener(this);
			paintPane.getChildren().add(serverRoot);
			double x=20;
			double y=30;
			serverRoot.relocate(x, y);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}
	@Override
	public void remove(Node node) {
		paintPane.getChildren().remove(node);
	}

	private EventHandler<MouseEvent> leftCircleOnMouseClickedEventHandler = 
	        new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent t) {
	        	Image image=new Image(getClass().getResourceAsStream("elements/place.png"),500,500,true,true);
	        	Main.mainStage.getScene().setCursor(new ImageCursor(image));
	        }
	};
}
