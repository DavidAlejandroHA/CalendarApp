package dad.test;

import dad.calendarapp.controller.CalendarItem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App  extends Application{

	public static Stage primaryStage;

	private CalendarItem rootController;

	@Override
	public void start(Stage primaryStage) throws Exception {

		App.primaryStage = primaryStage;

		rootController = new CalendarItem();

		Scene scene = new Scene(rootController.getView(), 640, 480);

		//primaryStage.getIcons().add(new Image(getClass().getResource("/images/classroom-24x24.png").toExternalForm()));
		primaryStage.setTitle("App");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
