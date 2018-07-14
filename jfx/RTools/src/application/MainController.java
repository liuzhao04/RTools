package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.aotain.rtools.common.IRedisOP;
import com.aotain.rtools.common.RedisOpFactory;
import com.aotain.rtools.common.RedisTypeConstant;
import com.aotain.rtools.model.RedisConfig;
import com.aotain.rtools.utils.ConfigHelper;
import com.aotain.rtools.utils.FrameUtils;

import application.config.ConfigController;
import application.value.ValueSceneFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MainController implements Initializable {

	// 集群选择框
	@FXML
	private ChoiceBox<String> selCluster = null;

	private List<RedisConfig> rlist = null;

	private IRedisOP redisOP = null;

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

	@FXML
	private MenuItem freshKeyMenu;

	@FXML
	private HBox bodyPaneWest;

	@FXML
	private Button btnMgrCluster;
	
	private int rlistHashCode = -1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rlist = ConfigHelper.read();
		if (rlist == null || rlist.isEmpty()) {
			rlist = getDefaultConfigs();
		}
		selCluster.setItems(rlistToModel()); // 初始化下拉框

		RedisConfig rc = ConfigHelper.getDefaultValue(rlist);
		resetCluster(rc);
		initKeysFilter();
		selCluster.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				RedisConfig rc = getRedisConfigByIndex(newValue.intValue());
				if(redisOP != null){
					redisOP.destory();
				}
				jtfKeysFilter.clear();
				resetCluster(rc);
				List<String> keys = redisOP.keys(null);
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

		freshKeyMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String curVal = jtfKeysFilter.getText();
				List<String> keys = redisOP.keys(StringUtils.trim(curVal));
				ObservableList<String> strList = FXCollections.observableArrayList(keys);
				listKeys.setItems(strList);
			}
		});

		// 保存集群配置
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				ConfigHelper.save(rlist);
			}

		});

		btnMgrCluster.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Parent root;
				try {
					URL location = ValueSceneFactory.class.getResource("/application/config/ConfigScene.fxml");
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(location);
					fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
					root = (Pane) fxmlLoader.load(location.openStream());
					ConfigController control = (ConfigController) fxmlLoader.getController();
					rlistHashCode = MainController.this.hashCode(rlist);
					control.init(rlist);
					FrameUtils.showWindow("集群配置管理", root,new CloseMgrClusterHandler(control));
				} catch (IOException e) {
					FrameUtils.alertOkError("集群配置管理加载失败：" + e.getMessage(),"确定");
					e.printStackTrace();
				}
			}
		});
	}
	
	private int hashCode(List<RedisConfig> rlist) {
		int code = 0;
		for(RedisConfig rc : rlist){
			code += rc.calcHashCode();
		}
		return code;
	}
	
	private boolean isConfigListChanged() {
		return rlistHashCode != hashCode(this.rlist);  
	}
	
	/**
	 * 集群管理界面关闭事件
	 * 
	 * @author liuz@aotian.com
	 * @date 2018年7月13日 下午2:00:59
	 */
	private class CloseMgrClusterHandler implements EventHandler<javafx.stage.WindowEvent>  {
		
		private ConfigController controller = null;

		public CloseMgrClusterHandler(ConfigController control) {
			this.controller = control;
		}

		@Override
		public void handle(javafx.stage.WindowEvent event) {
			if(controller != null){
				if(isConfigListChanged()){
					FrameUtils.alertOkWarn("配置已修改，手动重启后生效！");
				}
			}
		}
		
	}

	private void resetCluster(RedisConfig rc) {
		selCluster.getSelectionModel().clearAndSelect(ConfigHelper.getIndex(rlist, rc));
		bodyPaneWest.setDisable(true);
		valuePane.setDisable(true);

		try {
			redisOP = RedisOpFactory.createIRedisOP(rc.getIpStrs(), rc.getPortStrs());
			if (redisOP.isConnectRight()) {
				bodyPaneWest.setDisable(false);
				valuePane.setDisable(false);
			} else {
				FrameUtils.alertOkError("集群连接失败："+rc.getCname());
				redisOP = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			FrameUtils.alertOkError("集群连接失败："+rc.getCname()+", "+e.getMessage());
			redisOP = null;
		}
	}

	private List<RedisConfig> getDefaultConfigs() {
		String host0 = "192.168.31.20";
		String port0 = "8888";
		RedisConfig rc0 = new RedisConfig(host0, port0);
		rc0.setId(1);
		rc0.setCname("UbuntuRedis");
//		rlist.add(rc0);
		String host1 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port1 = "7000,7000,7000,7000,7000,7000";
		RedisConfig rc1 = new RedisConfig(host1, port1);
		rc1.setId(2);
		rc1.setCname("研发集群");
		String host2 = "192.168.5.65,192.168.5.66,192.168.5.67,192.168.5.68,192.168.5.69,192.168.5.71";
		String port2 = "8000,8000,8000,8000,8000,8000";
		RedisConfig rc2 = new RedisConfig(host2, port2);
		rc2.setId(3);
		rc2.setCname("测试集群");
		rlist.add(rc1);
		rlist.add(rc2);
		ConfigHelper.save(rlist);
		return rlist;
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
		String type = redisOP.type(newKey);
		jtfType.setText(type);

		showValueByKeyType(newKey, type);
	}

	/**
	 * 根据key的不同，在界面上展示不一样的结果
	 * 
	 * @param key
	 *            redis key值
	 * @param type
	 *            redis key的类型
	 */
	private void showValueByKeyType(String key, String type) {
		switch (type) {
		case RedisTypeConstant.STRING:
			Pane pane = ValueSceneFactory.createStringPanel(redisOP, key);
			valuePane.setCenter(pane);
			break;
		case RedisTypeConstant.HASH:
			pane = ValueSceneFactory.createHashPanel(redisOP, key);
			valuePane.setCenter(pane);
			break;
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

	private RedisConfig getRedisConfigByIndex(int index) {
		return rlist.get(index);
	}

	private ObservableList<String> rlistToModel() {
		List<String> list = new ArrayList<String>();
		for (RedisConfig ru : rlist) {
			list.add(ru.getCname());
		}
		return FXCollections.observableArrayList(list);
	}

	private void initKeysFilter() {
		if(redisOP == null){
			return;
		}
		List<String> keys = redisOP.keys(null);
		ObservableList<String> strList = FXCollections.observableArrayList(keys);
		listKeys.setItems(strList);
		jtfKeysFilter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				List<String> keys = redisOP.keys(StringUtils.trim(newValue));
				ObservableList<String> strList = FXCollections.observableArrayList(keys);
				listKeys.setItems(strList);
			}
		});
	}

}
