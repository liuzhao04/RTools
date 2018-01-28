package com.aotain.rtools.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;

import application.dialog.DialogOkController;
import application.dialog.DialogOkOrCancleController;
import application.dialog.DialogOkOrCancleController.ClickEvent;
import application.value.ValueSceneFactory;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 界面工具箱
 * 
 * @author liuz@aotian.com
 * @date 2017年12月28日 下午2:21:44
 */
public class FrameUtils {

	/**
	 * 获取屏幕大小
	 * 
	 * @return
	 */
	public static Dimension getScreenDimension() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * 将src大小的组件，居中放置在target上的location点(左上角坐标)
	 * 
	 * @param src
	 * @param target
	 * @return
	 */
	public static Point calcLoaction(Dimension src, Dimension target) {
		Point point = new Point();
		point.x = 0;
		point.y = 0;
		if (src == null || target == null) {
			return point;
		}
		int tmpX = (int) ((target.width - src.width) / 2);
		int tmpY = (int) ((target.height - src.height) / 2);
		if (tmpX < 0 || tmpY < 0) {
			return point;
		}
		point.x = tmpX;
		point.y = tmpY;
		return point;
	}

	/**
	 * 弹出框
	 * 
	 * @param title
	 * @param message
	 */
	public static void alert(String title, String message, String buttonText) {
		final Stage window = new Stage();
		window.setTitle(title);
		// modality要使用Modality.APPLICATION_MODEL
		window.initModality(Modality.APPLICATION_MODAL);
		window.setResizable(false);
		Parent root = null;
		try {
			URL location = ValueSceneFactory.class.getResource("/application/dialog/DialogOk.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			root = (Pane) fxmlLoader.load(location.openStream());
			DialogOkController control = (DialogOkController) fxmlLoader.getController();
			control.init(message,buttonText,window);
			Scene scene = new Scene(root);
			window.setScene(scene);
			// 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
			window.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void alertOkWarn(String message) {
		alert("系统提示",message,"确定");
	}
	
	public static void alertOkError(String message) {
		alert("异常提示",message,"确定");
	}
	
	public static void alertOkWarn(String message,String button) {
		alert("系统提示",message,button);
	}
	
	public static void alertOkError(String message,String button) {
		alert("异常提示",message,button);
	}
	
	/**
	 * 弹出框
	 * 
	 * @param title
	 * @param message
	 */
	public static void dialog(String title,String message, final ClickEvent okClickedEvent,String okText,String cancleText) {
		final Stage window = new Stage();
		window.setTitle(title);
		// modality要使用Modality.APPLICATION_MODEL
		window.initModality(Modality.APPLICATION_MODAL);
		window.setResizable(false);
		Parent root = null;
		try {
			URL location = ValueSceneFactory.class.getResource("/application/dialog/DialogOkOrCancle.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			root = (Pane) fxmlLoader.load(location.openStream());
			DialogOkOrCancleController control = (DialogOkOrCancleController) fxmlLoader.getController();
			control.init(message,okClickedEvent,okText,cancleText,window);
			Scene scene = new Scene(root);
			window.setScene(scene);
			// 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
			window.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void dialogOkCancle(String message,ClickEvent ce) {
		dialog("系统提示",message,ce,"确定","取消");
	}
	
	public static void showWindow(String title, Parent root) {
		final Stage window = new Stage();
		window.setTitle(title);
		// modality要使用Modality.APPLICATION_MODEL
		window.initModality(Modality.APPLICATION_MODAL);
		Scene scene = new Scene(root);
		window.setScene(scene);
		// 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
		window.showAndWait();
	}
}
