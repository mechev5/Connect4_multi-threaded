

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class newServerGui extends Application{
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Connect 4 Server");
		try {
            // Read file fxml and draw interface.
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/FXML/sFxml.fxml"));
         Scene s1 = new Scene(root, 700,700);
         s1.getStylesheets().add("/styles/style1.css");
            primaryStage.setScene(s1);
            primaryStage.show();
         
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	System.out.println("Server Closed");
                Platform.exit();
                System.exit(0);
            }
        });
	}

	
}
