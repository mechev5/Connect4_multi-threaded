import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


// MyTest uses an alternate version of my GameButton and GameLogic classes without JavaFX components.
// JavaFX components cause ExceptionInInitializerError and Toolkit errors.

class MyTest {

	// Functions to create new game board for testing.
	HashMap<String, GameButtonTest> buttons = new HashMap<String, GameButtonTest>();
	
	public String getCoordinates(int y) {
		if (y == 0) {
			return "5";
		} else if (y == 1) {
			return "4";
		} else if (y == 2) {
			return "3";
		} else if (y == 3) {
			return "2";
		} else if (y == 4) {
			return "1";
		}
		return "0";
	}
	
	public GameButtonTest[][] getFreshBoard() {
		GameButtonTest[][] gp = new GameButtonTest[7][6];
		buttons.clear();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				String coord = "" + i + getCoordinates(j);
				GameButtonTest temp = new GameButtonTest(coord);
				gp[i][j] = temp;
				buttons.put(coord, temp);
			}
		}
		return gp;
	}
	
	@Test
	void testVerticalWin() {
		for (int i = 0; i < 7; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			buttons.get(i + "0").placePiece();
			buttons.get(i + "1").placePiece();
			buttons.get(i + "2").placePiece();
			buttons.get(i + "3").placePiece();
			assertEquals(true, GameLogicTest.checkWin(gp), "testVerticalWin: Vertical test failed");
		}
		for (int i = 0; i < 7; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			buttons.get(i + "1").placePiece();
			buttons.get(i + "2").placePiece();
			buttons.get(i + "3").placePiece();
			buttons.get(i + "4").placePiece();
			assertEquals(true, GameLogicTest.checkWin(gp), "testVerticalWin: Vertical test failed");
		}
		for (int i = 0; i < 7; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			buttons.get(i + "2").placePiece();
			buttons.get(i + "3").placePiece();
			buttons.get(i + "4").placePiece();
			buttons.get(i + "5").placePiece();
			assertEquals(true, GameLogicTest.checkWin(gp), "testVerticalWin: Vertical test failed");
		}
		
	}
	@Test
	void testHorizontalWin() {
		for (int i = 0; i < 6; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			buttons.get("0" + i).placePiece();
			buttons.get("1" + i).placePiece();
			buttons.get("2" + i).placePiece();
			buttons.get("3" + i).placePiece();
			assertEquals(true, GameLogicTest.checkWin(gp), "testHorizontalWin: Horizontal test failed");
		}
		for (int i = 0; i < 6; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			buttons.get("1" + i).placePiece();
			buttons.get("2" + i).placePiece();
			buttons.get("3" + i).placePiece();
			buttons.get("4" + i).placePiece();
			assertEquals(true, GameLogicTest.checkWin(gp), "testHorizontalWin: Horizontal test failed");
		}
		for (int i = 0; i < 6; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			buttons.get("2" + i).placePiece();
			buttons.get("3" + i).placePiece();
			buttons.get("4" + i).placePiece();
			buttons.get("5" + i).placePiece();
			assertEquals(true, GameLogicTest.checkWin(gp), "testHorizontalWin: Horizontal test failed");
		}
		for (int i = 0; i < 6; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			buttons.get("3" + i).placePiece();
			buttons.get("4" + i).placePiece();
			buttons.get("5" + i).placePiece();
			buttons.get("6" + i).placePiece();
			assertEquals(true, GameLogicTest.checkWin(gp), "testHorizontalWin: Horizontal test failed");
		}
	}
	@Test
	void testUpRightWin() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				GameButtonTest[][] gp = getFreshBoard();
				buttons.get(i + "" + j).placePiece();
				buttons.get((i+1) + "" + (j+1)).placePiece();
				buttons.get((i+2) + "" + (j+2)).placePiece();
				buttons.get((i+3) + "" + (j+3)).placePiece();
				assertEquals(true, GameLogicTest.checkWin(gp), "testUpRightWin: Diagonal Up-Right failed");
			}
		}
	}
	@Test
	void testDownLeftWin() {
		for (int i = 3; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				GameButtonTest[][] gp = getFreshBoard();
				buttons.get(i + "" + j).placePiece();
				buttons.get((i-1) + "" + (j+1)).placePiece();
				buttons.get((i-2) + "" + (j+2)).placePiece();
				buttons.get((i-3) + "" + (j+3)).placePiece();
				assertEquals(true, GameLogicTest.checkWin(gp), "testUpRightWin: Diagonal Down-Left failed");
			}
		}
	}
	@Test
	void testTie() {
		GameButtonTest[][] gp = getFreshBoard();
		// Alternate between red and yellow placing pieces
		Boolean red = true;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				if (!red) {
					buttons.get(i + "" + j).setYellow(true);
				}
				buttons.get(i + "" + j).placePiece();
				red = !red;
			}
		}
		red = true;
		for (int i = 0; i < 7; i++) {
			for (int j = 3; j < 6; j++) {
				if (!red) {
					buttons.get(i + "" + j).setYellow(true);
				}
				buttons.get(i + "" + j).placePiece();
				red = !red;
			}
		}
		System.out.println(gp.length);
		// Assert that all buttons are active, i.e, no spaces are left to play.
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				assertEquals(true, buttons.get(i + "" + j).active);
			}
		}
		assertEquals(false, GameLogicTest.checkWin(gp), "Tie not drawn");
	}
	@Test
	// Test that the winning pieces are returned
	void testReturnCorrectPieces() {
		for (int i = 0; i < 7; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			String[] coords = new String[4];
			buttons.get(i + "0").placePiece();
			coords[3] = i + "0";
			buttons.get(i + "1").placePiece();
			coords[2] = i + "1";
			buttons.get(i + "2").placePiece();
			coords[1] = i + "2";
			buttons.get(i + "3").placePiece();
			coords[0] = i + "3";
			assertEquals(true, GameLogicTest.checkWin(gp), "testReturnCorrectPieces: Vertical test failed");
			assertArrayEquals(coords, GameLogicTest.getPieces(gp), "testReturnCorrectPieces: Incorrect pieces returned");
		}
		for (int i = 0; i < 7; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			String[] coords = new String[4];
			buttons.get(i + "1").placePiece();
			coords[3] = i + "1";
			buttons.get(i + "2").placePiece();
			coords[2] = i + "2";
			buttons.get(i + "3").placePiece();
			coords[1] = i + "3";
			buttons.get(i + "4").placePiece();
			coords[0] = i + "4";
			assertEquals(true, GameLogicTest.checkWin(gp), "testVerticalWin: Vertical test failed");
			assertArrayEquals(coords, GameLogicTest.getPieces(gp), "testReturnCorrectPieces: Incorrect pieces returned");
		}
		for (int i = 0; i < 7; i++) {
			GameButtonTest[][] gp = getFreshBoard();
			String[] coords = new String[4];
			buttons.get(i + "2").placePiece();
			coords[3] = i + "2";
			buttons.get(i + "3").placePiece();
			coords[2] = i + "3";
			buttons.get(i + "4").placePiece();
			coords[1] = i + "4";
			buttons.get(i + "5").placePiece();
			coords[0] = i + "5";
			assertEquals(true, GameLogicTest.checkWin(gp), "testVerticalWin: Vertical test failed");
			assertArrayEquals(coords, GameLogicTest.getPieces(gp), "testReturnCorrectPieces: Incorrect pieces returned");
		}
	}

}
