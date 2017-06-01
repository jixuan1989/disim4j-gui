package cn.edu.thu.disim4j;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import cn.edu.thu.disim4j.elements.ElementController;
import cn.edu.thu.disim4j.elements.MyCubicCurve;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class MainController implements Initializable, GlobalController{

	DrawType drawType=DrawType.None;
	Group none=new Group();//无意义的
	int counter=0;//每个元素都给一个唯一编号
	Map<Integer, ElementController> elements=new HashMap<>();//存储画板上所有元素
	Map<ElementController, Integer> reverseElements=new HashMap<>();
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
	@FXML
	Circle leftCommPlace;
	@FXML
	Rectangle leftCommRect;
	@FXML 
	Circle leftStorePlace;//TODO 尚未实现
	@FXML
	Rectangle leftTimeoutRect;//TODO 尚未实现
	
	//鼠标上的图案
	Group placeOnCursor;
	Group transitionOnCursor;
	Group commPlaceOnCursor;
	Group commTransitionOnCursor;
	Group storePlaceOnCursor;
	Group timeoutTransitionOnCursor;
	//TODO 新增元素时在这里加新的变量
	
	//drawType=Line 时连线模式
	boolean start=true;//连线开始还是结束的标记
	ElementController startNodeController;//连线开始位置
	ElementController endNodeController;//连线结束位置
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initNodesOnCursor();
		addHButton.setOnAction( (EventHandler<ActionEvent>) e->{
			paintPane.setMinWidth(paintPane.getWidth()+500);
		});
		addVButton.setOnAction( (EventHandler<ActionEvent>) e->{
			paintPane.setMinHeight(paintPane.getHeight()+500);
		});
		paintPane.setOnMouseEntered(mouseEnterOrLeaveGlobal);
		paintPane.setOnMouseExited(mouseEnterOrLeaveGlobal);
		paintPane.setOnMouseClicked(paintPaneOnMouseClickedEventHandler);
	}
	
	@Override
	public void remove(Node node, ElementController controller) {
		paintPane.getChildren().remove(node);
		Integer integer=reverseElements.get(controller);
		if(integer!=null){
			elements.remove(integer);
			reverseElements.remove(controller);
		}
		
	}


	
	//用户在主画板上点击了鼠标
	private EventHandler<MouseEvent> paintPaneOnMouseClickedEventHandler = 
			new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			if(paintPane.getOnMouseMoved()!=null){//==null时相当于用户并没有点选左边的基础元素（此时drawType==None）
				//line比较特殊
				if(!drawType.equals(DrawType.Line))
					addNewNodeOnPaintPane(t);
				System.out.println("北大简介");
				System.out.println(t.getTarget());
			}
		}

		
	};
	
	//在paintPane中移动鼠标事件
	private EventHandler<MouseEvent> mouseMoveGlobal = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			getNodeOnCursor().relocate(t.getX()-25, t.getY()-30);
		}
	};
	
	//鼠标进入或者离开paintPane的时候的事件
	private EventHandler<MouseEvent> mouseEnterOrLeaveGlobal = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			if(t.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
				getNodeOnCursor().setVisible(true);
			}else{
				getNodeOnCursor().setVisible(false);
			}
		}
	};
	
	//发现按下了esc 就将鼠标上的图形去掉。同时清除未完成的连线状态
	EventHandler<KeyEvent> keyTypedHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ESCAPE){
            	clearCursor();
            }
		}
	};
	private void clearCursor(){
		getNodeOnCursor().setVisible(false);
        paintPane.getChildren().remove(getNodeOnCursor());
        paintPane.setOnMouseMoved(null);
    	drawType=DrawType.None;
    	if(!start){
    		startNodeController.getGroup().setEffect(null);
    		startNodeController=null;
    		endNodeController=null;
    	}
    	paintPane.setCursor(Cursor.DEFAULT);
	}
	
	private void addOneElement(double x, double y, String fxml){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
		try {
			counter++;
			Group group = (Group)fxmlLoader.load();
			ElementController controller=(ElementController)fxmlLoader.getController();
			controller.addListener(this);
			paintPane.getChildren().add(group);
			elements.put(counter, controller);
			reverseElements.put(controller, counter);
			group.relocate(x, y);
			System.out.println(group.getTranslateX());
			System.out.println("input:"+x+","+y+"|now:"+group.getLayoutX()+","+group.getLayoutY());
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	

	//用户点击了左边的库所元素时
		private EventHandler<MouseEvent> leftNodeOnMouseClickedEventHandler = 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				//TODO 新增元素时扩展这里
				paintPane.setOnMouseMoved(null);
				paintPane.getChildren().remove(getNodeOnCursor());
				clearCursor();
				if(paintPane.getOnMouseMoved()==null){//确保重复点击无效
					if(t.getSource().equals(leftPlace)){//如果点击的是库所
						drawType=DrawType.Place;
					}else if(t.getSource().equals(leftRect)){
						drawType=DrawType.Transition;
					}else if(t.getSource().equals(leftLine)){
						drawType=DrawType.Line;
						paintPane.setCursor(Cursor.CROSSHAIR);
						System.out.println("点击line了");
						start=true;
					}else if(t.getSource().equals(leftCommPlace)){
						drawType=DrawType.CommPlace;
					}else if(t.getSource().equals(leftCommRect)){
						drawType=DrawType.CommTransition;
					}else if(t.getSource().equals(leftStorePlace)){
						drawType=DrawType.StorePlace;
					}else if(t.getSource().equals(leftTimeoutRect)){
						drawType=DrawType.TimeoutTransition;
					}
					paintPane.setOnMouseMoved(mouseMoveGlobal);
					paintPane.getChildren().add(getNodeOnCursor());
				}
			}
		};
	static enum DrawType{//如果新增其他基础元素，请扩展这里 TODO
		None, Place, Transition, Line, CommPlace, CommTransition, StorePlace, TimeoutTransition
	}
	
	private Node getNodeOnCursor(){//如果新增其他基础元素，请扩展这里 TODO
		//务必不要重新new新的对象
		switch (drawType) {
		case None:
			return none;
		case Place:
			return placeOnCursor;
		case Transition:
			return transitionOnCursor;
		case Line:
			return none;
		case CommPlace:
			return commPlaceOnCursor;
		case CommTransition:
			return commTransitionOnCursor;
		default:
			return none;
		}
	}
	private void initNodesOnCursor(){//如果新增其他基础元素，请扩展这里 TODO
		try {
			placeOnCursor = (Group)new FXMLLoader(getClass().getResource("elements/place.fxml")).load();
			placeOnCursor.setVisible(false);
			placeOnCursor.getChildren().get(3).setVisible(false);;//FIXME 如果修改了place.fxml 请注意修改这个index的值
			leftPlace.setOnMouseClicked(leftNodeOnMouseClickedEventHandler);
			//如果新增其他基础元素，请扩展这里 TODO
			transitionOnCursor= (Group)new FXMLLoader(getClass().getResource("elements/transition.fxml")).load();
			transitionOnCursor.setVisible(false);
			transitionOnCursor.getChildren().get(2).setVisible(false);;//FIXME 如果修改了transition.fxml 请注意修改这个index的值
			leftRect.setOnMouseClicked(leftNodeOnMouseClickedEventHandler);
			//Line比较特殊，只有一行代码
			leftLine.setOnMouseClicked(leftNodeOnMouseClickedEventHandler);
			//如果新增其他基础元素，请扩展这里 TODO
			commPlaceOnCursor= (Group)new FXMLLoader(getClass().getResource("elements/commPlace.fxml")).load();
			commPlaceOnCursor.setVisible(false);
			commPlaceOnCursor.getChildren().get(3).setVisible(false);//FIXME 如果修改了transition.fxml 请注意修改这个index的值
			leftCommPlace.setOnMouseClicked(leftNodeOnMouseClickedEventHandler);
			//如果新增其他基础元素，请扩展这里 TODO
			commTransitionOnCursor= (Group)new FXMLLoader(getClass().getResource("elements/commTransition.fxml")).load();
			commTransitionOnCursor.setVisible(false);
			commTransitionOnCursor.getChildren().get(2).setVisible(false);;//FIXME 如果修改了transition.fxml 请注意修改这个index的值
			leftCommRect.setOnMouseClicked(leftNodeOnMouseClickedEventHandler);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
	}
	private void addNewNodeOnPaintPane(MouseEvent t) {
		switch (drawType) {//如果新增其他基础元素，请扩展这里 TODO
		case Place:
			addOneElement(t.getX()-25, t.getY()-30, "elements/place.fxml");
			break;
		case Transition:
			addOneElement(t.getX()-45, t.getY()-25, "elements/transition.fxml");
			break;
		case Line:
			break;
		case CommPlace:
			addOneElement(t.getX()-25, t.getY()-30, "elements/commPlace.fxml");
			break;
		case CommTransition:
			addOneElement(t.getX()-25, t.getY()-30, "elements/commTransition.fxml");
			break;
				
		default:
			break;
		}
	}

	@Override
	public void reportClicked(Shape node, ElementController controller) {
		System.out.println(start);
		if(start){
			startNodeController=controller;
			endNodeController=null;
			start=false;
			paintPane.setCursor(Cursor.CLOSED_HAND);
			startNodeController.getGroup().setEffect(new DropShadow());
		}else{
			
			if(controller.getClass().equals(startNodeController.getClass())){
				return;
			}
			endNodeController=controller;
			start=true;
			paintPane.setCursor(Cursor.CROSSHAIR);
			double[] locations=getcurvelocation();
			MyCubicCurve curve=new MyCubicCurve(locations[0],locations[1],locations[2],locations[3]);
			paintPane.getChildren().add(curve);
			//关联该边到相关元素
			endNodeController.associateInArc(curve);
			startNodeController.associateOutArc(curve);
			startNodeController.getGroup().setEffect(null);
		}
	}
	/**
	 * 根据 start node 和end node获取贝塞尔曲线的位置
	 * startNode 和endNode是place和rectangle，所以需要找到他们的parent（也就是Group）才能找到正确的layoutX Y
	 * @return
	 */
	private double[] getcurvelocation() {
		double result[]=new double[4];		
		if(startNodeController.getLayoutX()<endNodeController.getLayoutX()){
			result[0]=startNodeController.getRightConnectionCoordinate().x;
			result[1]=startNodeController.getRightConnectionCoordinate().y;
			result[2]=endNodeController.getLeftConnectionCoordinate().x;
			result[3]=endNodeController.getLeftConnectionCoordinate().y;
		}else{
			result[0]=startNodeController.getLeftConnectionCoordinate().x;
			result[1]=startNodeController.getLeftConnectionCoordinate().y;
			result[2]=endNodeController.getRightConnectionCoordinate().x;
			result[3]=endNodeController.getRightConnectionCoordinate().y;
		}
		return result;
	}

	@Override
	public boolean isLineMode() {
		return drawType.equals(DrawType.Line);
	}
}
