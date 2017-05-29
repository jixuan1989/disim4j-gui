package cn.edu.thu.disim4j.elements;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TransitionController extends ElementController implements Initializable{

	@FXML
	Rectangle transitionRect;
	@FXML
	TextField nameText;
	@FXML
	TextArea functionText;

	@FXML
	Group group;


	@Override
	Group getGroup() {
		return group;
	}
	@Override
	double getCenterOffsetX() {
		return transitionRect.getWidth()/2;
	}

	@Override
	double getCenterOffsetY() {
		return transitionRect.getHeight()/2;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		transitionRect.setOnMouseClicked(circleOnMouseClickedEventHandler);
	}




	//单击鼠标的时候，将init token收起来，目前未启用
	EventHandler<MouseEvent> circleOnMouseClickedEventHandler = 
			new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			System.out.println("我被点击了");
			if(controller!=null&&controller.isLineMode()){//左边的基础元素没有controller
				controller.reportClicked(transitionRect);
			}else{
				functionText.setVisible(!functionText.isVisible());
			}
		}
	};


}
