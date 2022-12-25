package dad.calendarapp;

import dad.calendarapp.controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CalendarApp extends Application {

	public static Stage primaryStage;
	//public Scene escena;
	
	private Controller controller = new Controller();
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		CalendarApp.primaryStage = primaryStage;
		primaryStage.setTitle("CalendarApp");
		primaryStage.setScene(new Scene(controller.getView()));
		primaryStage.getIcons().add(new Image(CalendarApp.class.getResourceAsStream("/images/calendar-16x16.png")));
		primaryStage.show();
		
		// añadir un eventHandler para aumentar de año al presionar una flecha
		/*escena.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
			switch (e.getCode()){
	    	case RIGHT:
	    		controller.operarAnio(1);
	    		break;
	    	case LEFT:
	    		controller.operarAnio(-1);
	    		break;
			default:
				break;
	    	}
		});*/
	}

	public static void main(String[] args) {
		launch(args);
	}

}
