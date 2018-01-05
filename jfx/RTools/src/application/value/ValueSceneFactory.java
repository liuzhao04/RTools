package application.value;

import com.aotain.rtools.common.RedisUtils;
import com.sun.javafx.sg.BackgroundFill;

import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Redis Value展示界面创建工厂
 * 
 * @author liuz@aotian.com
 * @date 2018年1月4日 下午6:39:19
 */
public class ValueSceneFactory {
	//
	private final static String STL_UNPORTED_DATA_TYPE_PANE = "-fx-control-inner-background:#ffffff;-fx-font-family: Consolas;-fx-font-size:16pt; -fx-text-fill: #ff0000; ";

	public static Pane createStringPanel(RedisUtils rutils, String key) {
		HBox hbox = new HBox();
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		hbox.getChildren().add(textArea);
		HBox.setHgrow(textArea, Priority.ALWAYS);
		textArea.setText(rutils.get(key));
		return hbox;
	}

	public static Pane createUnSuportedPanel() {
		HBox hbox = new HBox();
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		hbox.getChildren().add(textArea);
		HBox.setHgrow(textArea, Priority.ALWAYS);
		hbox.setStyle(STL_UNPORTED_DATA_TYPE_PANE);
		textArea.setText("An unsupported data type !");
		return hbox;
	}
}
