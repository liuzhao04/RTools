package application.value;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aotain.rtools.common.IRedisOP;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;

/**
 * HashValue控制器
 * 
 * @author liuz@aotian.com
 * @date 2018年1月8日 下午4:26:33
 */
public class HashValueController implements Initializable {

	@FXML
	private ToggleGroup tgroup = null;

	@FXML
	private TextField tfHkeyFilter;

	@FXML
	private ListView<String> tfHkeyList;

	@FXML
	private TextArea taHValue;

	@FXML
	private RadioButton rbJsonFormat;
	@FXML
	private RadioButton rbTextFormat;

	@FXML
	private MenuItem freshHKey;
	
	@FXML
	private MenuItem freshHContent;
	
	private IRedisOP rutils;
	private String key;

	private List<String> hkeys = null;

	public void myInit() {
		resetView();
	}

	public void setRutils(IRedisOP rutils) {
		this.rutils = rutils;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle rb) {

	}

	private void resetView() {
		hkeys = rutils.hkeys(this.key);
		tfHkeyList.setItems(FXCollections.observableArrayList(hkeys));
		tfHkeyFilter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				List<String> keys = filterKeys(StringUtils.trim(newValue));
				ObservableList<String> strList = FXCollections.observableArrayList(keys);
				tfHkeyList.setItems(strList);
			}
		});
		tfHkeyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changeKey(oldValue, newValue);
			}
		});
		tgroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (tgroup.getSelectedToggle() != null) {
					RadioButton rb = (RadioButton) tgroup.getSelectedToggle();
					String type = rb.getText();
					showAsType(type);
				}
			}
		});
		taHValue.setWrapText(true);
		freshHKey.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				hkeys = rutils.hkeys(key);
				tfHkeyList.setItems(FXCollections.observableArrayList(hkeys));
				List<String> keys = filterKeys(StringUtils.trim(tfHkeyFilter.getText()));
				ObservableList<String> strList = FXCollections.observableArrayList(keys);
				tfHkeyList.setItems(strList);
			}
		});
		
		freshHContent.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String hkey = tfHkeyList.getSelectionModel().getSelectedItem();
				changeKey(hkey, hkey);
			}
		});
	}

	private void showAsType(String type) {
		if ((!StringUtils.isBlank(value)) && type.equals("json")) {
			JSONObject obj = JSON.parseObject(value);
			String data = JSON.toJSONString(obj, true);
			taHValue.setText(data);
		} else {
			taHValue.setText(value);
		}
	}

	private String value = null;

	/**
	 * 选择KEY时
	 * 
	 * @param oldKey
	 * @param newKey
	 */
	private void changeKey(String oldKey, String newKey) {
		if (StringUtils.isBlank(newKey)) {
			return;
		}
		String content = rutils.hget(key, newKey);
		value = content;

		try {
			JSON.parseObject(value);
			rbJsonFormat.setDisable(false);
			rbJsonFormat.setSelected(true);
		} catch (Exception e) {
			rbJsonFormat.setSelected(false);
			rbJsonFormat.setDisable(true);
			rbTextFormat.setSelected(true);
		}
		if (tgroup.getSelectedToggle() != null) {
			RadioButton rb = (RadioButton) tgroup.getSelectedToggle();
			String type = rb.getText();
			showAsType(type);
		}
	}

	private List<String> filterKeys(String key) {
		if (StringUtils.isBlank(key)) {
			return hkeys;
		}
		key = key.trim();
		List<String> nlist = new ArrayList<String>();
		for (String hkey : hkeys) {
			if (hkey.indexOf(key) != -1) {
				nlist.add(hkey);
			}
		}
		return nlist;
	}
}
