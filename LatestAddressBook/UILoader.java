package addressbook.loader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import addressbook.controller.UIController;


public class UILoader extends Application
{

	public static void main(String[] args) {
		launch(args);

	}

	public void start(Stage stage)
	{
		try
		{

			Parent root = (Parent) FXMLLoader.load(getClass().getResource("../../userinterface.fxml"));	
			Scene scene = new Scene(root);
			stage.setTitle("AddressBook");
			stage.setScene(scene);
			stage.show();
			scene.getStylesheets().add("/MyCss.css");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}