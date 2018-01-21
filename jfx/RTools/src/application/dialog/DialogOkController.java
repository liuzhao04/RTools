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
public class DialogOkController implements Initializable {

	@FXML
	private Button buttonOk;
	@FXML
	private TextArea textArea;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void init(String message,String buttonText,final Stage stage) {
		buttonOk.setText(buttonText);
		textArea.setText(message);
		
		buttonOk.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
	}

}
