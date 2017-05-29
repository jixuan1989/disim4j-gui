package cn.edu.thu.disim4j.elements;

import java.net.URL;
import java.util.ResourceBundle;

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
	Group getGroup() {
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
			System.out.println("被点击了");
			if(controller!=null&&controller.isLineMode()){//左边的基础元素没有controller
				controller.reportClicked(placeCircle);
			}else{
				initText.setVisible(!initText.isVisible());
			}
		}
	};


}
