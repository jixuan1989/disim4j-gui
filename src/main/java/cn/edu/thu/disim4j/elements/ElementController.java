package cn.edu.thu.disim4j.elements;

import java.net.URL;
import java.util.ResourceBundle;

import cn.edu.thu.disim4j.GlobalController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

public abstract class ElementController {
	GlobalController controller;
	public void addListener(GlobalController controller){
		this.controller=controller;
	}

	//circle
	final ContextMenu contextMenu = new ContextMenu();
	public void initialize(URL location, ResourceBundle resources) {
		initPlaceCircleMenu();
		getGroup().setCursor(Cursor.HAND);
		getGroup().setOnMousePressed(groupOnMousePressedEventHandler);
		getGroup().setOnMouseDragged(groupOnMouseDraggedEventHandler);
		//placeCircle.setOnMouseClicked(circleOnMouseClickedEventHandler);
		//添加右键菜单
		getGroup().setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				contextMenu.show(getGroup(), Side.LEFT, getCenterOffsetX(), getCenterOffsetY());
			}
		});
	}

	private void initPlaceCircleMenu(){
		MenuItem item1 = new MenuItem("删除");
		item1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				controller.remove(getGroup());
			}
		});
		contextMenu.getItems().addAll(item1);
	}

	/**
	 * 
	 * @return 返回root元素
	 */
	abstract Group getGroup();
	/**
	 * 
	 * @return 返回group左上角到中心的X偏移值
	 */
	abstract double getCenterOffsetX();
	/**
	 * 
	 * @return 返回group左上角到中心的Y偏移值
	 */
	abstract double getCenterOffsetY();
	
	double orgSceneX, orgSceneY;
	double orgTranslateX, orgTranslateY;
	//鼠标按下的时候，开始记录当前位置
	EventHandler<MouseEvent> groupOnMousePressedEventHandler = 
			new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			//orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
			//orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
			orgTranslateX = getGroup().getTranslateX();
			orgTranslateY = getGroup().getTranslateY();
			t.consume();
		}
	};
	//拖动鼠标的时候，让图形跟着走
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
			getGroup().setTranslateX(newTranslateX);
			getGroup().setTranslateY(newTranslateY);
		}
	};
}
