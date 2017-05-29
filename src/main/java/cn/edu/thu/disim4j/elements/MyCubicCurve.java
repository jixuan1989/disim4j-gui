package cn.edu.thu.disim4j.elements;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;

/**
 * see https://stackoverflow.com/questions/26702519/javafx-line-curve-with-arrow-head
 *  and https://stackoverflow.com/questions/13056795/cubiccurve-javafx
 * @author hxd
 *
 */
public class MyCubicCurve extends Group{

	public MyCubicCurve(double sx, double sy, double ex, double ey) {
		super();
		curve = createStartingCurve(sx, sy, ex, ey);
		controlLine1 = new BoundLine(curve.controlX1Property(), curve.controlY1Property(), curve.startXProperty(), curve.startYProperty());
		controlLine2 = new BoundLine(curve.controlX2Property(), curve.controlY2Property(), curve.endXProperty(),   curve.endYProperty());

		start    = new Anchor(Color.PALEGREEN, curve.startXProperty(),    curve.startYProperty());
		control1 = new Anchor(Color.GOLD,      curve.controlX1Property(), curve.controlY1Property());
		control2 = new Anchor(Color.GOLDENROD, curve.controlX2Property(), curve.controlY2Property());
		end      = new Anchor(Color.TOMATO,    curve.endXProperty(),      curve.endYProperty());
		controlLine1.setVisible(false);
		control2.setVisible(false);
		start.setVisible(false);
		end.setVisible(false);
		control1.setVisible(false);
		controlLine2.setVisible(false);
		curve.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				controlLine1.setVisible(!controlLine1.isVisible());
				controlLine2.setVisible(!controlLine2.isVisible());
				start.setVisible(!start.isVisible());
				end.setVisible(!end.isVisible());
				control1.setVisible(!control1.isVisible());
				control2.setVisible(!control2.isVisible());
			}
		});
	    double[] arrowShape = new double[] { 0,0,5,10,-5,10 };
	    //arrows.add( new Arrow( curve, 0f, arrowShape));
	    arrows.add( new Arrow( curve, 1f, arrowShape));
	    this.getChildren().addAll(controlLine1, controlLine2, curve, start, control1, control2, end,arrows.get(0));
		
	}
	CubicCurve curve;

	Line controlLine1 ;
	Line controlLine2 ;
	Anchor start   ;
	Anchor control1;
	Anchor control2 ;
	Anchor end  ;

	private CubicCurve createStartingCurve(double sx, double sy, double ex, double ey) {
		CubicCurve curve = new CubicCurve();
		curve.setStartX(sx);
		curve.setStartY(sy);
		curve.setControlX1(sx+50);
		curve.setControlY1(sy-50);
		curve.setControlX2(ex-50);
		curve.setControlY2(ey+50);
		curve.setEndX(ex);
		curve.setEndY(ey);
		curve.setStroke(Color.BLACK);
		curve.setStrokeWidth(2);
		curve.setStrokeLineCap(StrokeLineCap.ROUND);
		curve.setFill(null);

		return curve;
	}


	class BoundLine extends Line {
		BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
			startXProperty().bind(startX);
			startYProperty().bind(startY);
			endXProperty().bind(endX);
			endYProperty().bind(endY);
			setStrokeWidth(2);
			setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
			setStrokeLineCap(StrokeLineCap.BUTT);
			getStrokeDashArray().setAll(10.0, 5.0);
		}
	}

	// a draggable anchor displayed around a point.
	  class Anchor extends Circle { 
	    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
	      super(x.get(), y.get(), 10);
	      setFill(color.deriveColor(1, 1, 1, 0.5));
	      setStroke(color);
	      setStrokeWidth(2);
	      setStrokeType(StrokeType.OUTSIDE);

	      x.bind(centerXProperty());
	      y.bind(centerYProperty());
	      enableDrag();
	    }

	    // make a node movable by dragging it around with the mouse.
	    private void enableDrag() {
	      final Delta dragDelta = new Delta();
	      setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	          // record a delta distance for the drag and drop operation.
	          dragDelta.x = getCenterX() - mouseEvent.getX();
	          dragDelta.y = getCenterY() - mouseEvent.getY();
	          getScene().setCursor(Cursor.MOVE);
	        }
	      });
	      setOnMouseReleased(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	          getScene().setCursor(Cursor.HAND);
	        }
	      });
	      setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	          double newX = mouseEvent.getX() + dragDelta.x;
	          if (newX > 0 && newX < getScene().getWidth()) {
	            setCenterX(newX);
	          }  
	          double newY = mouseEvent.getY() + dragDelta.y;
	          if (newY > 0 && newY < getScene().getHeight()) {
	            setCenterY(newY);
	          }

	          // update arrow positions
	          for( Arrow arrow: arrows) {
	              arrow.update();
	          }
	        }
	      });
	      setOnMouseEntered(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	          if (!mouseEvent.isPrimaryButtonDown()) {
	            getScene().setCursor(Cursor.HAND);
	          }
	        }
	      });
	      setOnMouseExited(new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent mouseEvent) {
	          if (!mouseEvent.isPrimaryButtonDown()) {
	            getScene().setCursor(Cursor.DEFAULT);
	          }
	        }
	      });
	    }

	    // records relative x and y co-ordinates.
	    private class Delta { double x, y; }
	  }  
	
	List<Arrow> arrows = new ArrayList<Arrow>();

    public static class Arrow extends Polygon {

        public double rotate;
        public float t;
        CubicCurve curve;
        Rotate rz;

        public Arrow( CubicCurve curve, float t) {
            super();
            this.curve = curve;
            this.t = t;
            init();
        }

        public Arrow( CubicCurve curve, float t, double... arg0) {
            super(arg0);
            this.curve = curve;
            this.t = t;
            init();
        }

        private void init() {

            setFill(Color.web("#ff0900"));

            rz = new Rotate();
            {
                rz.setAxis(Rotate.Z_AXIS);
            }
            getTransforms().addAll(rz);

            update();
        }

        public void update() {
            double size = Math.max(curve.getBoundsInLocal().getWidth(), curve.getBoundsInLocal().getHeight());
            double scale = size / 4d;

            Point2D ori = eval(curve, t);
            Point2D tan = evalDt(curve, t).normalize().multiply(scale);

            setTranslateX(ori.getX());
            setTranslateY(ori.getY());

            double angle = Math.atan2( tan.getY(), tan.getX());

            angle = Math.toDegrees(angle);

            // arrow origin is top => apply offset
            double offset = -90;
            if( t > 0.5)
                offset = +90;

            rz.setAngle(angle + offset);

        }

          /**
           * Evaluate the cubic curve at a parameter 0<=t<=1, returns a Point2D
           * @param c the CubicCurve 
           * @param t param between 0 and 1
           * @return a Point2D 
           */
          private Point2D eval(CubicCurve c, float t){
              Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+
                      3*t*Math.pow(1-t,2)*c.getControlX1()+
                      3*(1-t)*t*t*c.getControlX2()+
                      Math.pow(t, 3)*c.getEndX(),
                      Math.pow(1-t,3)*c.getStartY()+
                      3*t*Math.pow(1-t, 2)*c.getControlY1()+
                      3*(1-t)*t*t*c.getControlY2()+
                      Math.pow(t, 3)*c.getEndY());
              return p;
          }

          /**
           * Evaluate the tangent of the cubic curve at a parameter 0<=t<=1, returns a Point2D
           * @param c the CubicCurve 
           * @param t param between 0 and 1
           * @return a Point2D 
           */
          private Point2D evalDt(CubicCurve c, float t){
              Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+
                      3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlX1()+
                      3*((1-t)*2*t-t*t)*c.getControlX2()+
                      3*Math.pow(t, 2)*c.getEndX(),
                      -3*Math.pow(1-t,2)*c.getStartY()+
                      3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlY1()+
                      3*((1-t)*2*t-t*t)*c.getControlY2()+
                      3*Math.pow(t, 2)*c.getEndY());
              return p;
          }
    }
}
