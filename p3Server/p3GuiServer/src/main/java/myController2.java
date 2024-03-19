import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class myController2 implements Initializable {

	@FXML
	BorderPane root;
	
	@FXML
	ListView<String> lvInfo;
	
	Server serverConnection;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		PortNumber pn = PortNumber.getInstance();
		createServer(pn.getPortNum());
	}
	
	public void createServer(int port) {
		//System.out.println("Port number is " + port);
		serverConnection = new Server(data -> {
			Platform.runLater(()->{
				lvInfo.getItems().add(data.toString());
			});
		}, port);
		System.out.println("Port number is " + port);
	}
}
