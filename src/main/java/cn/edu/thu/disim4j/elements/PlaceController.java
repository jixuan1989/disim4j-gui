package cn.edu.thu.disim4j.elements;

import java.net.URL;
import java.util.ResourceBundle;

import cn.edu.thu.disim4j.Coordinate;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class PlaceController extends ElementController implements Initializable{

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


	@Override
	public Group getGroup() {
		return group;
	}
	@Override
	double getCenterOffsetX() {
		return placeCircle.getRadius()+25;
	}

	@Override
	double getCenterOffsetY() {
		return placeCircle.getRadius()+10;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		placeCircle.setOnMouseClicked(circleOnMouseClickedEventHandler);

	}


	//单击鼠标的时候，将init token收起来
	EventHandler<MouseEvent> circleOnMouseClickedEventHandler = 
			new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			System.out.println("被点击了"+t.getX());
			if(controller!=null&&controller.isLineMode()){//左边的基础元素没有controller
				controller.reportClicked(placeCircle, PlaceController.this);
			}else{
				initText.setVisible(!initText.isVisible());
			}
		}
	};


	@Override
	public Coordinate getTopConnectionCoordinate() {
		return new Coordinate(group.getLayoutX()+group.getTranslateX()+placeCircle.getRadius(),group.getLayoutY()+group.getTranslateY() );
	}
	@Override
	public Coordinate getBottomConnectionCoordinate() {
		return new Coordinate(group.getLayoutX()+group.getTranslateX()+placeCircle.getRadius(),group.getLayoutY()+group.getTranslateY()+2*placeCircle.getRadius());
	}
	@Override
	public Coordinate getLeftConnectionCoordinate() {
		return new Coordinate(group.getLayoutX()+group.getTranslateX(),group.getLayoutY()+group.getTranslateY()+placeCircle.getRadius() );
	}
	@Override
	public Coordinate getRightConnectionCoordinate() {
		return new Coordinate(group.getLayoutX()+group.getTranslateX()+2*placeCircle.getRadius(),group.getLayoutY()+group.getTranslateY()+placeCircle.getRadius() );
	}


}
