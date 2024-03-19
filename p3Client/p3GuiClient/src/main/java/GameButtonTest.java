// Alternate version of GameButton class that works the same way only stripped of JavaFX components to allow for testing.
// All necessary data members and methods from Application version are present.
public class GameButtonTest {
	boolean active, highlighted;
	String sCoords;
	Boolean isRed;
	
	GameButtonTest(String c) {
		isRed = true;
		active = false;
		highlighted = false;
		sCoords = c;
	}
	public void setYellow(Boolean b) {
		isRed = !b;
	}
	
	public void placePiece() {
		if (active) {
			return;
		}
		active = true;
	}
	
	public void highlightPiece() {
	}
	
	public void resetButton() {
		active = false;
		highlighted = false;
	}
	
	
}