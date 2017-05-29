package cn.edu.thu.disim4j.elements;

import java.net.URL;
import java.util.ResourceBundle;

import cn.edu.thu.disim4j.GlobalController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.WindowEvent;

public class PlaceController implements Initializable{
	
	@FXML
	Circle placeCircle;
	@FXML
	TextField nameText;
	@FXML
	TextArea initText;
	@FXML
	TextField typeText;
	@FXML
	Group group;
	//circle
	final ContextMenu contextMenu = new ContextMenu();
	
	GlobalController controller;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setPlaceCircleMenu();
		group.setCursor(Cursor.HAND);
		group.setOnMousePressed(groupOnMousePressedEventHandler);
		group.setOnMouseDragged(groupOnMouseDraggedEventHandler);
		//placeCircle.setOnMouseClicked(circleOnMouseClickedEventHandler);
		placeCircle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				System.out.println("aaaS");
				contextMenu.show(placeCircle, Side.LEFT, placeCircle.getRadius()+25, 0);
			}
		});
		
	}
	
	public void addListener(GlobalController controller){
		this.controller=controller;
	}
	private void setPlaceCircleMenu(){
		MenuItem item1 = new MenuItem("删除");
		item1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        controller.remove(group);
		    }
		});
		contextMenu.getItems().addAll(item1);
	}
	
	double orgSceneX, orgSceneY;
	double orgTranslateX, orgTranslateY;
	EventHandler<MouseEvent> groupOnMousePressedEventHandler = 
	        new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent t) {
	            orgSceneX = t.getSceneX();
	            orgSceneY = t.getSceneY();
	            //orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
	            //orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
	            orgTranslateX = group.getTranslateX();
	            orgTranslateY = group.getTranslateY();
	        }
	    };
	    
	    EventHandler<MouseEvent> groupOnMouseDraggedEventHandler = 
	        new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent t) {
	            double offsetX = t.getSceneX() - orgSceneX;
	            double offsetY = t.getSceneY() - orgSceneY;
	            double newTranslateX = orgTranslateX + offsetX;
	            double newTranslateY = orgTranslateY + offsetY;
	            //((Circle)(t.getSource())).setTranslateX(newTranslateX);
	            //((Circle)(t.getSource())).setTranslateY(newTranslateY);
	            group.setTranslateX(newTranslateX);
	            group.setTranslateY(newTranslateY);
	        }
	    };
	    EventHandler<MouseEvent> circleOnMouseClickedEventHandler = 
		        new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent t) {
		            initText.setVisible(!initText.isVisible());
		        }
		};
	    
}
