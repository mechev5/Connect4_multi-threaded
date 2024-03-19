import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameButton extends Button{
	boolean active, highlighted;
	String sCoords;
	ImageView defaultImageV, redDef, yellowDef, redHigh, yellowHigh;
	Boolean isRed;
	
	GameButton(String c) {
		isRed = true;
		active = false;
		highlighted = false;
		this.sCoords = c;
		defaultImageV = new ImageView(new Image("/images/but.png"));
		defaultImageV.setPreserveRatio(true);
		this.setGraphic(defaultImageV);
		redDef = new ImageView("/images/redDef.png");
		redHigh = new ImageView("/images/redHigh.png");
		yellowDef = new ImageView("/images/yellowDef.png");
		yellowHigh = new ImageView("/images/yellowHigh.png");
		this.setStyle("-fx-background-color: aqua");
	}
	// Set the game piece to yellow.
	public void setYellow(Boolean b) {
		isRed = !b;
	}
	// Place down a piece. If piece is already placed, then no action is taken.
	public void placePiece() {
		if (active) {
			return;
		}
		if (isRed) {
			this.setGraphic(redDef);
		} else {
			this.setGraphic(yellowDef);
		}
		active = true;
	}
	// Highlight the piece.
	public void highlightPiece() {
		if (isRed) {
			this.setGraphic(redHigh);
		} else {
			this.setGraphic(yellowHigh);
		}
	}
	// Reset the piece to be used again.
	public void resetButton() {
		active = false;
		highlighted = false;
		this.setGraphic(defaultImageV);
	}
	
	
}
