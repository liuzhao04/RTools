package application.dialog;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * 配置管理控制器
 * @author Administrator
 *
 */
public class DialogOkOrCancleController implements Initializable {

	@FXML
	private Button buttonOk;

	@FXML
	private Button buttonCancle;
	
	
	@FXML
	private TextArea textArea;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void init(String message, final ClickEvent okClickedEvent,String okText,String cancleText,final Stage stage) {
		buttonOk.setText(okText);
		buttonCancle.setText(cancleText);
		textArea.setText(message);
		
		buttonOk.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				okClickedEvent.onClick(DialogOkOrCancleController.this.buttonOk);
				stage.close();
			}
		});
		
		buttonCancle.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
	}

	public interface ClickEvent{
		public void onClick(Button button);
	}
}
