import java.io.Serializable;

public class CFourInfo implements Serializable {
		private static final long serialVersionUID = 1L;
		String name;
		String play, winner, update;
		boolean gameDone, has2Players, turn, moveMade, reset;
		int count = 0;
		
		CFourInfo() {
			name = "";
			play = "";
			winner = "";
			gameDone = false;
			has2Players = false;
			turn = false;
			moveMade = false;
			reset = false;
			update = "null";
		}
		
	}// end of CFourInfo
