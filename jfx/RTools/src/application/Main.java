package application;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static String getAppName(){
		 return ResourceBundle.getBundle("app").getString("app-name");
	}
	private static String getAppVersion(){
		return ResourceBundle.getBundle("app").getString("app-version");
	}
	private static String getAppNameAndVersion(){
		return getAppName()+" - "+getAppVersion();
	}
	public static String getIconPath(){
		return ResourceBundle.getBundle("app").getString("app-image");
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// Read file fxml and draw interface.
			Parent root = FXMLLoader.load(getClass().getResource("/application/MainScene.fxml"));

			primaryStage.setTitle(getAppNameAndVersion());
			primaryStage.setScene(new Scene(root));
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(Main.getIconPath())));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
