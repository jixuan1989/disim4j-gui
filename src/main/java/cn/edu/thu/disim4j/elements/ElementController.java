package cn.edu.thu.disim4j.elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import cn.edu.thu.disim4j.Coordinate;
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
	//关联的入边
	List<MyCubicCurve> inArcs=new ArrayList<>();
	//关联的出边
	List<MyCubicCurve> outArcs=new ArrayList<>();
	
	
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
				controller.remove(getGroup(), ElementController.this);
				inArcs.forEach(arc->controller.remove(arc, ElementController.this));
				outArcs.forEach(arc->controller.remove(arc, ElementController.this));
			}
		});
		contextMenu.getItems().addAll(item1);
	}

	/**
	 * 
	 * @return 返回root元素
	 */
	abstract public Group getGroup();
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
			System.out.println(t.getX());
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			//orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
			//orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
			orgTranslateX = getGroup().getTranslateX();
			orgTranslateY = getGroup().getTranslateY();
			inArcs.stream().filter(arc->arc.getParent()!=null).forEach(arc->{
				arc.end.simulateMousePressed(getGroup().getLayoutX()+getGroup().getTranslateX()+t.getX(), getGroup().getLayoutY()+getGroup().getTranslateY()+t.getY());
			});
			outArcs.stream().filter(arc->arc.getParent()!=null).forEach(arc->{
				arc.start.simulateMousePressed(getGroup().getLayoutX()+getGroup().getTranslateX()+t.getX(), getGroup().getLayoutY()+getGroup().getTranslateY()+t.getY());
			});
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
			inArcs.stream().filter(arc->arc.getParent()!=null).forEach(arc->{
				arc.end.simulateMouseDragged(getGroup().getLayoutX()+getGroup().getTranslateX()+t.getX(), getGroup().getLayoutY()+getGroup().getTranslateY()+t.getY());
			});
			outArcs.stream().filter(arc->arc.getParent()!=null).forEach(arc->{
				arc.start.simulateMouseDragged(getGroup().getLayoutX()+getGroup().getTranslateX()+t.getX(), getGroup().getLayoutY()+getGroup().getTranslateY()+t.getY());
			});

		}
	};
	public double getLayoutX(){
		return getGroup().getLayoutX()+getGroup().getTranslateX();
	}
	public double getLayoutY(){
		return getGroup().getLayoutY()+getGroup().getTranslateY();
	}
	/**
	 * 关联一条边
	 * @param curve
	 */
	public void associateInArc(MyCubicCurve curve){
		inArcs.add(curve);
	}
	/**
	 * 关联一条边
	 * @param curve
	 */
	public void associateOutArc(MyCubicCurve curve){
		outArcs.add(curve);
	}
	abstract public Coordinate getTopConnectionCoordinate();
	abstract public Coordinate getBottomConnectionCoordinate();
	abstract public Coordinate getLeftConnectionCoordinate();
	abstract public Coordinate getRightConnectionCoordinate();


}
