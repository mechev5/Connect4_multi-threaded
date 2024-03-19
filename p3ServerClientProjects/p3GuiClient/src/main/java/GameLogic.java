
public class GameLogic {
	// Read the 2D array of GameButtons for win.
	public static boolean checkWin(GameButton[][] buttons) {
		// Check for horizontal win
		for (int i = 0; i < 4; i++) {
			for (int j = 0 ; j < 6; j++) {
				if (buttons[i][j].active) {
					Boolean isRed = buttons[i][j].isRed;
					if (buttons[i+1][j].active && buttons[i+1][j].isRed == isRed
							&& buttons[i+2][j].active && buttons[i+2][j].isRed == isRed
							&& buttons[i+3][j].active && buttons[i+3][j].isRed == isRed) {
						return true;
					}
				}
			}
		}
		// Check for vertical win
		for (int i = 0; i < 7; i++) {
			for (int j = 0 ; j < 3; j++) {
				if (buttons[i][j].active) {
					Boolean isRed = buttons[i][j].isRed;
					if (buttons[i][j+1].active && buttons[i][j+1].isRed == isRed
							&& buttons[i][j+2].active && buttons[i][j+2].isRed == isRed
							&& buttons[i][j+3].active && buttons[i][j+3].isRed == isRed) {
						return true;
					}
				}
			}
		}
		// Check for diagonal win up-right
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].active) {
					Boolean isRed = buttons[i][j].isRed;
					if (buttons[i+1][j+1].active && buttons[i+1][j+1].isRed == isRed
							&& buttons[i+2][j+2].active && buttons[i+2][j+2].isRed == isRed
							&& buttons[i+3][j+3].active && buttons[i+3][j+3].isRed == isRed) {
						return true;
					}
				}
			}
		}
		// Check for diagonal win down-left
		for (int i = 3; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].active) {
					Boolean isRed = buttons[i][j].isRed;
					if (buttons[i-1][j+1].active && buttons[i-1][j+1].isRed == isRed
							&& buttons[i-2][j+2].active && buttons[i-2][j+2].isRed == isRed
							&& buttons[i-3][j+3].active && buttons[i-3][j+3].isRed == isRed) {
						return true;
					}
				}
			}
		}
		return false;
	}
	// Read the 2D array of GameButtons and return the coordinates of the winning buttons in an array.
	public static String[] getPieces(GameButton[][] buttons) {
		String[] ret = new String[4];
		// Check for horizontal win
		for (int i = 0; i < 4; i++) {
			for (int j = 0 ; j < 6; j++) {
				if (buttons[i][j].active) {
					Boolean isRed = buttons[i][j].isRed;
					if (buttons[i+1][j].active && buttons[i+1][j].isRed == isRed
							&& buttons[i+2][j].active && buttons[i+2][j].isRed == isRed
							&& buttons[i+3][j].active && buttons[i+3][j].isRed == isRed) {
						ret[0] = buttons[i][j].sCoords;
						ret[1] = buttons[i+1][j].sCoords;
						ret[2] = buttons[i+2][j].sCoords;
						ret[3] = buttons[i+3][j].sCoords;
						return ret;
					}
				}
			}
		}
		// Check for vertical win
		for (int i = 0; i < 7; i++) {
			for (int j = 0 ; j < 3; j++) {
				if (buttons[i][j].active) {
					Boolean isRed = buttons[i][j].isRed;
					if (buttons[i][j+1].active && buttons[i][j+1].isRed == isRed
							&& buttons[i][j+2].active && buttons[i][j+2].isRed == isRed
							&& buttons[i][j+3].active && buttons[i][j+3].isRed == isRed) {
						ret[0] = buttons[i][j].sCoords;
						ret[1] = buttons[i][j+1].sCoords;
						ret[2] = buttons[i][j+2].sCoords;
						ret[3] = buttons[i][j+3].sCoords;
						return ret;
					}
				}
			}
		}
		// Check for diagonal win up-right
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 3; j++) {
						if (buttons[i][j].active) {
							Boolean isRed = buttons[i][j].isRed;
							if (buttons[i+1][j+1].active && buttons[i+1][j+1].isRed == isRed
									&& buttons[i+2][j+2].active && buttons[i+2][j+2].isRed == isRed
									&& buttons[i+3][j+3].active && buttons[i+3][j+3].isRed == isRed) {
								ret[0] = buttons[i][j].sCoords;
								ret[1] = buttons[i+1][j+1].sCoords;
								ret[2] = buttons[i+2][j+2].sCoords;
								ret[3] = buttons[i+3][j+3].sCoords;
								return ret;
							}
						}
					}
				}
				// Check for diagonal win down-left
				for (int i = 3; i < 7; i++) {
					for (int j = 0; j < 3; j++) {
						if (buttons[i][j].active) {
							Boolean isRed = buttons[i][j].isRed;
							if (buttons[i-1][j+1].active && buttons[i-1][j+1].isRed == isRed
									&& buttons[i-2][j+2].active && buttons[i-2][j+2].isRed == isRed
									&& buttons[i-3][j+3].active && buttons[i-3][j+3].isRed == isRed) {
								ret[0] = buttons[i][j].sCoords;
								ret[1] = buttons[i-1][j+1].sCoords;
								ret[2] = buttons[i-2][j+2].sCoords;
								ret[3] = buttons[i-3][j+3].sCoords;
								return ret;
							}
						}
					}
				}
		return ret;
	}
}
