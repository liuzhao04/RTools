package application.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.ListView;

/**
 * 配置管理控制器
 * 
 * @author Administrator
 *
 */
public class ConfigController implements Initializable {

	@FXML
	private ListView<String> clusterList;

	private List<RedisConfig> rlist;

	private RedisConfig selectedItem = null;

	@FXML
	private Button addButton;

	@FXML
	private Button delButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void init(List<RedisConfig> rlist) {
		this.rlist = rlist;
		if (this.rlist == null) {
			this.rlist = new ArrayList<RedisConfig>();
		}

		ObservableList<String> strList = FXCollections.observableArrayList(toListData());
		clusterList.setItems(strList);

		clusterList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// 1. 更新选中数据
				selectedItem = ConfigController.this.rlist.get(newValue.intValue());
				// 2. 更新界面
				updateSelectedItemInfo();
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
						// TODO:重新加载下拉菜单就行
						ConfigController.this.rlist.remove(selectedItem);
						ConfigHelper.save(ConfigController.this.rlist);
					}
				});

			}
		});
		
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
			}
			
		});

	}

	private void updateSelectedItemInfo() {
		if (selectedItem == null) {
			return;
		}
		System.out.println(selectedItem.toString());
	}

	private List<String> toListData() {
		List<String> list = new ArrayList<String>();
		int i = 1;
		for (RedisConfig rc : this.rlist) {
			list.add(i + " - " + rc.getCname());
			i++;
		}
		return list;
	}

}
