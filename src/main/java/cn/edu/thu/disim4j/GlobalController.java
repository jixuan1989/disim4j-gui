package cn.edu.thu.disim4j;

import javafx.scene.Node;
import javafx.scene.shape.Shape;

public interface GlobalController {
	public void remove(Node node);
	public void reportClicked(Shape node);
	/**
	 * 
	 * @return 是否是在连线状态下
	 */
	public boolean isLineMode();
}
