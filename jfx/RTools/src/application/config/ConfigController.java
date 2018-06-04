package application.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.aotain.rtools.model.RedisConfig;
import com.aotain.rtools.utils.ConfigHelper;
import com.aotain.rtools.utils.FrameUtils;

import application.dialog.DialogOkOrCancleController.ClickEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

/**
 * 配置管理控制器
 * 
 * @author Administrator
 *
 */
public class ConfigController implements Initializable {

	@FXML
	private ListView<String> clusterList;
	@FXML
	private ListView<String> clusterNodeList;
	@FXML
	private TextField clusterName;
	@FXML
	private TextField clusterNodeIp;
	@FXML
	private TextField clusterNodePort;
	@FXML
	private Button addButton;
	@FXML
	private Button delButton;
	@FXML
	private Button defaultClusterButton;
	@FXML
	private Button resetManagerViewButton;
	@FXML
	private Button lockButton;
	@FXML
	private Button nodeAddButton;
	@FXML
	private Label label_1;
	@FXML
	private Label label_2;
	@FXML
	private Label label_3;

	private List<RedisConfig> rlist;			// redis集群配置
	private RedisConfig selectedItem = null;	// 当前被选中的集群
	
	private boolean isLock = true;				// 集群管理锁：默认锁住(不可修改新增)
	
	private RedisConfig editCache = null;		// 编辑区的缓存
	private boolean isEditMode = false;			// 编辑模式标志：true表示正在编辑，否则为新建模式
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void init(List<RedisConfig> rlist) {
		this.rlist = rlist;
		if (this.rlist == null) {
			this.rlist = new ArrayList<RedisConfig>();
		}
		lockManagerView();
		ObservableList<String> strList = FXCollections.observableArrayList(toListData());
		clusterList.setItems(strList);

		clusterList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(newValue.intValue() < 0){
					return;
				}
				// 1. 获取选中数据
				selectedItem = ConfigController.this.rlist.get(newValue.intValue());
				// 重置管理界面
				resetManagerView();
				// 2. 更新界面
				updateSelectedItemInfo();
				// 3. 刷新缓存对象
				editCache = selectedItem;
				isEditMode = true;
			}
		});
		
		clusterNodeList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(newValue.intValue() < 0){
					return;
				}
				// 1. 获取选中数据
				 ObservableList<String>  list = clusterNodeList.getItems();
				 String selItemStr = list.get(newValue.intValue());
				 String[] selItems = selItemStr.split(":");
				// 2. 更新界面
				 clusterNodeIp.setText(selItems[0]);
				 clusterNodePort.setText(selItems[1]);
			}
		});

		delButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (selectedItem == null) {
					FrameUtils.alertOkWarn("请选择要删除的集群！");
					return;
				}
				// 通知主界面删除Item(重新加载下拉菜单就行)
				FrameUtils.dialogOkCancle("确定删除集群："+selectedItem.getCname(), new ClickEvent() {
					
					@Override
					public void onClick(Button button) {
						ConfigController.this.rlist.remove(selectedItem);
						ConfigHelper.save(ConfigController.this.rlist);
						clusterList.getItems().remove(clusterList.getSelectionModel().getSelectedIndex());
						// TODO:重新加载下拉菜单就行
						
					}
				});

			}
		});
		
		// 新增或者修改一个集群信息
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				RedisConfig rc = obtainRedisConfig();
				String error = null;
				// 基础格式校验
				if((error = validateBasic(rc)) != null){
					FrameUtils.alertOkError(error);
					return;
				}
				// 编辑模式，用当前模式与缓存中的进行对比
				if(isEditMode){
					rc.setId(editCache.getId());
					rc.setDefaultSelected(editCache.isDefaultSelected());
					if(!editCache.isChanged(rc)){
						FrameUtils.alertOkError("当前集群配置无修改");
						return;
					}
					// 提交修改参数
					editCache.setCname(rc.getCname());
					editCache.setIpStrs(rc.getIpStrs());
					editCache.setPortStrs(rc.getPortStrs());
					int index = ConfigHelper.getIndex(ConfigController.this.rlist, editCache);
					clusterList.getItems().set(index, (index+1)+"-"+rc.getCname());
					FrameUtils.alertOkWarn("修改成功");
				}else{
					editCache = rc;
					ConfigController.this.rlist.add(editCache);
					// 刷新ClusterList
					int size = clusterList.getItems().size();
					clusterList.getItems().add((size+1)+"-"+rc.getCname());
					FrameUtils.alertOkWarn("新增成功");
				}
			}
			
		});
		
		// 新增或者修改一个节点的信息
		nodeAddButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				int index = clusterNodeList.getSelectionModel().getSelectedIndex();
				List<String> items = clusterNodeList.getItems();
				String selectedItem = null;
				if(index >= 0){
					selectedItem = items.get(index);
				}
				String ip = clusterNodeIp.getText();
				String port = clusterNodePort.getText();
				String error = null;
				if((error = validateIp(ip)) != null){
					FrameUtils.alertOkError(error);
					return;
				}
				if((error = validatePort(port)) != null){
					FrameUtils.alertOkError(error);
					return;
				}
				
				String destStr = ip.trim()+":"+port.trim();
				if(selectedItem != null && selectedItem.equals(destStr)){
					FrameUtils.alertOkWarn("无修改");
					return;
				}
				
				if(items!=null && items.contains(destStr)){
					FrameUtils.alertOkError("节点已存在："+destStr);
					return;
				}
				
				if(selectedItem == null) {
					// 新增节点
					if(clusterNodeList.getItems() ==null){
						clusterNodeList.setItems(FXCollections.observableArrayList(destStr));
					}else{
						clusterNodeList.getItems().add(destStr);
					}
					// 缓存对象更新IP值
					editCache.addIpAndPort(ip.trim(), port.trim());
					
				}else{
					// 修改节点
					clusterNodeList.getItems().set(index,destStr);
					// 更新缓存对象
					String[] ipAndPort =  obtainIpAndPort();
					editCache.setIpStrs(ipAndPort[0]);
					editCache.setPortStrs(ipAndPort[1]);
				}
			}
			
		});
		
		lockButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(isLock) {
					unlockManagerView();
				}else{
					lockManagerView();
				}
			}
		});
		
		resetManagerViewButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetManagerView();
			}
		});
		
		defaultClusterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setDefaultCluster();
			}
		});
	}
	
	private String validateBasic(RedisConfig rc) {
		if(StringUtils.isBlank(rc.getCname())){
			return "集群名称为空";
		}
		if(StringUtils.isBlank(rc.getIpStrs())){
			return "IP为空";
		}
		if(StringUtils.isBlank(rc.getPortStrs())){
			return "端口为空";
		}
		return null;
	}
	
	private String validateIp(String ip) {
		if (StringUtils.isBlank(ip)) {
			return "IP为空";
		}
		String ip_ = ip.trim();
		if (!ip_.matches("^\\d{1,3}\\.\\d{1,3}.\\d{1,3}.\\d{1,3}$")) {
			return "非法IP：" + ip_;
		}
		return null;
	}

	private String validatePort(String port) {
		if (StringUtils.isBlank(port)) {
			return "端口为空";
		}
		String port_ = port;
		if (!port_.matches("^\\d+$")) {
			return "非法端口：" + port_;
		}
		return null;
	}

	private void updateSelectedItemInfo() {
		if (selectedItem == null) {
			return;
		}
		String cName = selectedItem.getCname();
		clusterName.setText(cName);
		ObservableList<String> strList = FXCollections.observableArrayList(toNodeList());
		clusterNodeList.setItems(strList);
	}

	private List<String> toNodeList() {
		String redisIpStrs = selectedItem.getIpStrs();
		String redisPortStrs = selectedItem.getPortStrs();
		String[] redisIps = redisIpStrs.split(",");
		String[] redisPorts = redisPortStrs.split(",");
		List<String> nodeList = new ArrayList<String>();
		for(int i = 0 ;i < redisIps.length; i++){
			nodeList.add(redisIps[i]+":"+redisPorts[i]);
		}
		return nodeList;
	}

	private List<String> toListData() {
		List<String> list = new ArrayList<String>();
		int i = 1;
		String selStr = null;
		boolean hasUsed = false;
		for (RedisConfig rc : this.rlist) {
			if(!hasUsed && rc.isDefaultSelected()){
				selStr = "(default)";
				hasUsed = true;
			}else{
				selStr = null;
			}
			list.add(i + " - " + rc.getCname() + (selStr==null?"":selStr));
			i++;
		}
		return list;
	}
	
	private void resetManagerView() {
		// 重置管理界面
		clusterNodeIp.setText("");
		clusterNodePort.setText("");
		if(clusterNodeList.getItems() != null){
			clusterNodeList.getItems().clear();
		}
		clusterNodeList.getSelectionModel().clearSelection();
		clusterNodeList.setItems(null); 
		clusterNodeList.setItems(clusterNodeList.getItems());
		clusterName.setText("");
		// 重置缓存数据
		editCache = obtainRedisConfig(); 
		isEditMode = false;
	}
	
	private String[] obtainIpAndPort(){
		StringBuilder ipSb = new StringBuilder();
		StringBuilder portSb = new StringBuilder();	
		if(clusterNodeList.getItems() != null){
			for(String item : clusterNodeList.getItems()){
				String[] it =  item.split(":");
				if(ipSb.length() > 0){
					ipSb.append(',');
					portSb.append(',');
				}
				ipSb.append(it[0]);
				portSb.append(it[1]);
			}
		}
		return new String[]{ipSb.toString(),portSb.toString()};
	}
	
	/**
	 * 获取界面上的数据,构造一个配置对象
	 * @return
	 */
	private RedisConfig obtainRedisConfig(){
		String[] ipAndPort = obtainIpAndPort();
		RedisConfig rc = new RedisConfig(ipAndPort[0],ipAndPort[1]);
		rc.setCname(clusterName.getText());
		rc.setId(ConfigHelper.getIndex(ConfigController.this.rlist, rc));
		rc.setDefaultSelected(false);
		return rc;
	}

	private void lockManagerView(){
		// 锁定管理界面
		nodeAddButton.setDisable(true);
		clusterNodeList.setDisable(true);
		clusterNodeIp.setText("");
		clusterNodePort.setText("");
		clusterName.setText("");
		clusterNodeIp.setDisable(true);
		clusterNodePort.setDisable(true);
		addButton.setDisable(true);
		resetManagerViewButton.setDisable(true);
		lockButton.setText("解锁");
		clusterName.setDisable(true);
		label_1.setDisable(true);
		label_2.setDisable(true);
		label_3.setDisable(true);
		isLock = true;
		clusterNodeList.getSelectionModel().clearSelection();
		clusterNodeList.setItems(null); 
		clusterNodeList.setItems(clusterNodeList.getItems());
		// 重置缓存数据
		editCache = obtainRedisConfig(); 
		isEditMode = false;
	}
	
	private void unlockManagerView(){
		//  解锁管理界面
		nodeAddButton.setDisable(false);
		clusterNodeList.setDisable(false);
		clusterNodeIp.setDisable(false);
		clusterNodePort.setDisable(false);
		addButton.setDisable(false);
		resetManagerViewButton.setDisable(false);
		lockButton.setText("锁定");
		clusterName.setDisable(false);
		label_1.setDisable(false);
		label_2.setDisable(false);
		label_3.setDisable(false);
		isLock = false;
	}
	
	private void setDefaultCluster(){
		// 设置默认集群
		for(RedisConfig rc :  ConfigController.this.rlist){
			if(selectedItem.equals(rc)){
				rc.setDefaultSelected(true);
			}else{
				rc.setDefaultSelected(false);
			}
		}
		clusterList.getSelectionModel().clearSelection();
		clusterList.setItems(null); 
		clusterList.setItems(FXCollections.observableArrayList(toListData()));
	}
}
