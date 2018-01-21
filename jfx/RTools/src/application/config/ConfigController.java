package application.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.aotain.rtools.model.RedisConfig;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void init(List<RedisConfig> rlist) {
		this.rlist = rlist;

		ObservableList<String> strList = FXCollections.observableArrayList(toListData());
		clusterList.setItems(strList);
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
