

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

public class myController implements Initializable {

	@FXML
	BorderPane root;
	
	@FXML
	HBox hb;
	
	@FXML
	VBox options;
	
	@FXML
	Text title, sPort;
	
	@FXML
	Button bTurnOnServer;
	
	
	@FXML
	TextField tfPort;
	
	@FXML
	ListView<String> lvInfo;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	public boolean tfFilter() {
		double value = 0;
		try {
			value = Double.valueOf(tfPort.getText());
		} catch (Exception e) {
			tfPort.setText("");
			return false;
		}
		return true;
	}
	
	
	public void turnOnServer(ActionEvent e) throws IOException {
		if (!tfFilter()) {
			return;
		}
		PortNumber pn = PortNumber.getInstance();
		pn.setPortNum(Integer.valueOf(tfPort.getText()));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/sFxml2.fxml"));
        Parent root2 = loader.load(); //load view into parent
        root.getScene().setRoot(root2);//update scene graph
	}

}
