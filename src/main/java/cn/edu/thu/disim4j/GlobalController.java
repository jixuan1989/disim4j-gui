package cn.edu.thu.disim4j;

import cn.edu.thu.disim4j.elements.ElementController;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

public interface GlobalController {
	public void remove(Node node, ElementController controller);
	public void reportClicked(Shape node, ElementController controller);
	/**
	 * 
	 * @return 是否是在连线状态下
	 */
	public boolean isLineMode();
}
