import java.io.Serializable;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ListScreen implements Serializable {
	BorderPane root;
	ListView<String> list;
	Server serverConnection;
	Scene gameScene;
	
	public ListScreen(Scene s) {
		list = new ListView<String>();
		this.gameScene = s;
		PortNumber pn = PortNumber.getInstance();
		createServer(pn.getPortNum());
		start();
	}
	
	public void start() {
		root = new BorderPane();
		root.setCenter(list);
		gameScene.setRoot(root);
	}
	
	public void createServer(int port) {
		System.out.println("Port number is " + port);
		serverConnection = new Server(data -> {
			Platform.runLater(()->{});
		}, port);
		//list.getItems().add("Server created. Port: " + port);
	}
}
