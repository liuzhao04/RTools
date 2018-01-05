package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.aotain.rtools.common.RedisTypeConstant;
import com.aotain.rtools.common.RedisUtils;
import com.aotain.rtools.model.RedisConfig;

import application.value.ValueSceneFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MainController implements Initializable {

	// 集群选择框
	@FXML
	private ChoiceBox<String> selCluster = null;

	private List<RedisConfig> rlist = null;

	private RedisUtils rutils = null;

	@FXML
	private TextField jtfKeysFilter = null;

	@FXML
	private ListView<String> listKeys = null;

	@FXML
	private TextField jtfKey = null;

	@FXML
	private TextField jtfType = null;
	
	@FXML
	private BorderPane valuePane = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rlist = new ArrayList<RedisConfig>();
		String host1 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port1 = "7000,7000,7000,7000,7000,7000";
		RedisConfig rc1 = new RedisConfig(host1, port1);
		rc1.setId(1);
		rc1.setName("研发集群");
		String host2 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port2 = "8000,8000,8000,8000,8000,8000";
		RedisConfig rc2 = new RedisConfig(host2, port2);
		rc2.setId(2);
		rc2.setName("测试集群");
		rlist.add(rc1);
		rlist.add(rc2);
		rutils = new RedisUtils(rc1.getIpStrs(), rc1.getPortStrs());
		initKeysFilter();
		resetClusterChoice();
		selCluster.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				RedisConfig rc = getRedisConfigByIndex(newValue.intValue());
				rutils.close();
				jtfKeysFilter.clear();
				rutils = new RedisUtils(rc.getIpStrs(), rc.getPortStrs());
				List<String> keys = rutils.fuzzyKeys(null);
				ObservableList<String> strList = FXCollections.observableArrayList(keys);
				listKeys.setItems(strList);
			}
		});
		listKeys.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changeKey(oldValue, newValue);
			}
		});
	}

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
		jtfKey.setText(newKey);
		String type = rutils.type(newKey);
		jtfType.setText(type);

		showValueByKeyType(newKey, type);
	}

	/**
	 * 根据key的不同，在界面上展示不一样的结果
	 * 
	 * @param key redis key值
	 * @param type redis key的类型
	 */
	private void showValueByKeyType(String key, String type) {
		switch (type) {
		case RedisTypeConstant.STRING:
			Pane pane = ValueSceneFactory.createStringPanel(rutils,key);
			valuePane.setCenter(pane);
			break;
		case RedisTypeConstant.HASH:
		case RedisTypeConstant.LIST:
		case RedisTypeConstant.SET:
		case RedisTypeConstant.ZSET:
		default:
			// TODO: 提示不支持的数据类型
			pane = ValueSceneFactory.createUnSuportedPanel();
			valuePane.setCenter(pane);
			break;
		}
	}
	
	private void resetClusterChoice() {
		selCluster.setItems(rlistToModel());
		selCluster.getSelectionModel().clearAndSelect(0);
	}

	private RedisConfig getRedisConfigByIndex(int index) {
		return rlist.get(index);
	}

	private ObservableList<String> rlistToModel() {
		List<String> list = new ArrayList<String>();
		for (RedisConfig ru : rlist) {
			list.add(ru.getName());
		}
		return FXCollections.observableArrayList(list);
	}

	private void initKeysFilter() {
		List<String> keys = rutils.fuzzyKeys(null);
		ObservableList<String> strList = FXCollections.observableArrayList(keys);
		listKeys.setItems(strList);
		jtfKeysFilter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				List<String> keys = rutils.fuzzyKeys(StringUtils.trim(newValue));
				ObservableList<String> strList = FXCollections.observableArrayList(keys);
				listKeys.setItems(strList);
			}
		});
	}

}
