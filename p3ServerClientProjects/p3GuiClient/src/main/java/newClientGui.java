

import java.util.HashMap;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class newClientGui extends Application{

	BorderPane root, gameRoot, endRoot;
	GridPane gameBoard;
	Text title, titleNum, tServerInfo, tPortNum, textIP, pName, sTurn, playTurn, winner, wait;
	TextField tfPortNum, tfIP;
	ListView<String> lvInfo, lvInfoCopy;
	ListView<Integer> moveList;
	HBox titleBox, gameHBox, textHBox, endHBox;
	VBox menuVbox1, menuVbox2, menuVbox2Copy, endVbox;
	Button start, playAgain, exit;
	Client clientConnection;
	Scene s1, s2, s3;
	Stage stage;
	static int portNum;
	String playerName, lastMove, currentPlayer, winnerString;
	HashMap<String, GameButton> buttons;
	GameButton[][] buttonGrid;
	Boolean clientRed, tie, replay;
	int placements;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.setTitle("Connect 4 Client");
		root = new BorderPane();
		title = new Text("Connect ");
		titleNum = new Text("4");
		titleNum.setFill(Color.RED);
		tServerInfo = new Text("SERVER INFORMATION");
		tPortNum = new Text("PORT NUMBER:");
		textIP = new Text("IP ADDRESS:");
		tfPortNum = new TextField();
		tfIP = new TextField("127.0.0.1");
		start = new Button("Start");
		lvInfo = new ListView<String>();
		lvInfoCopy = new ListView<String>();
		moveList = new ListView<Integer>();
		buttons = new HashMap<String, GameButton>();
		playerName = " ";
		buttonGrid = new GameButton[7][6];
		winner = new Text();
		clientRed = true;
		lastMove = "";
		winnerString = "Player ";
		currentPlayer = "Player 1";
		placements = 0;
		tie = false;
		replay = false;
		root.setStyle("-fx-background-image: url('/images/background1.jpg')");
		
		start.setOnAction(e->{connect(e); start.setDisable(true);});
		
		tfIP.setEditable(false);
		
		tServerInfo.setFont(new Font("Impact", 40));
		tServerInfo.setFill(Color.RED);
		tServerInfo.setUnderline(true);
		tServerInfo.setStroke(Color.BLACK);
		tPortNum.setFont(new Font("Impact", 24));
		tPortNum.setFill(Color.YELLOW);
		tPortNum.setStroke(Color.BLACK);
		tPortNum.setUnderline(true);
		textIP.setFont(new Font("Impact", 24));
		textIP.setFill(Color.WHITE);
		textIP.setStroke(Color.BLACK);
		textIP.setUnderline(true);
		start.setFont(new Font("Impact", 24));
		tfPortNum.setMaxWidth(300);
		tfIP.setMaxWidth(300);
		tfPortNum.setFont(new Font("Courier New", 16));
		tfIP.setFont(new Font("Courier New", 16));
		
		start.setMaxSize(200, 150);
		start.setStyle("-fx-background-color: lawngreen");
		start.setTextFill(Color.WHITE);
		menuVbox1 = new VBox(tServerInfo, tPortNum, tfPortNum, textIP, tfIP, start);
		//menuVbox1.setMargin(start, new Insets(15));
		//menuVbox1.setMargin(tServerInfo, new Insets(15));
		menuVbox1.setSpacing(20);
		menuVbox1.setPadding(new Insets(-120));
		menuVbox1.setMaxWidth(620);
		//menuVbox1.setStyle("-fx-background-color: blue");
		menuVbox2 = new VBox(lvInfo);
		menuVbox2.setAlignment(Pos.BOTTOM_RIGHT);
		menuVbox2.setMaxSize(600, 300);
		menuVbox2Copy = new VBox(lvInfoCopy);
		menuVbox2Copy.setAlignment(Pos.BOTTOM_RIGHT);
		menuVbox2Copy.setMaxSize(600, 300);
		menuVbox1.setAlignment(Pos.CENTER_LEFT);
		title.setFont(new Font("Stencil", 72));
		titleNum.setFont(new Font("Stencil", 72));
		titleBox = new HBox(title, titleNum);
		titleBox.setAlignment(Pos.TOP_CENTER);
		root.setTop(titleBox);
		root.setPadding(new Insets(8));
		root.setAlignment(title, Pos.CENTER);
		root.setCenter(menuVbox1);
		root.setBottom(menuVbox2);
		root.setAlignment(menuVbox2, Pos.BOTTOM_RIGHT);
		s1 = new Scene(root, 1280, 720);
		stage.setScene(s1);
		stage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	System.out.println("Client Closed");
                Platform.exit();
                System.exit(0);
            }
        });
	}
	// Filter out characters in textfield.
	public boolean tfFilter() {
		double value = 0;
		try {
			value = Double.valueOf(tfPortNum.getText());
		} catch (Exception e) {
			tfPortNum.setText("");
			return false;
		}
		return true;
	}
	// Highlight winning pieces.
	public void highlightWin() {
		System.out.println("highlightWin: highlighting pieces");
		String[] winningPieces = GameLogic.getPieces(buttonGrid);
		for (String s : winningPieces) {
			buttons.get(s).highlightPiece();
		}
		PauseTransition pause = new PauseTransition(Duration.seconds(4));
		pause.setOnFinished(e -> {
			endScreen();
		});
		pause.play();
	}
	// Parse the message string recieved from the Client. Update app based on that string.
	public void parseData(String data) {
		String[] dataArray = data.split(" ");
		for (int i = 0; i < dataArray.length; i++) {
			if (dataArray[i].contains("@Turn")) {
				if (dataArray[i+1].contains("True")) {
					gameBoard.setDisable(false);
					playTurn.setText(currentPlayer);
					playTurn.setFill(Color.GREEN);
				} else {
					gameBoard.setDisable(true);
					playTurn.setText(currentPlayer);
					playTurn.setFill(Color.RED);
				}
			}
			if (dataArray[i].contains("@Update")) {
				String update = dataArray[i+1];
				int j = 1;
				while (!dataArray[i+j].contains(".") && !dataArray[i+j].contains("!")) {
					update += " " + dataArray[i+j+1];
					j++;
				}
				if (update.contains("Game Tied")) {
					tie = true;
					endScreen();
					return;
				}
				if (update.contains("Player 1")) {
					currentPlayer = "Player 2";
				} else {
					currentPlayer = "Player 1";
				}
				playTurn.setText(currentPlayer);
				if (currentPlayer.contains(playerName)) {
					playTurn.setFill(Color.GREEN);
				} else {
					playTurn.setFill(Color.RED);
				}
				lvInfo.getItems().add(update);
			}
			if (dataArray[i].contains("@Move")) {
				String ij = dataArray[i+1];
				GameButton b = buttons.get(ij);
				b.setYellow(clientRed);
				placements++;
				b.placePiece();
			}
			if (dataArray[i].contains("@Win")) {
				if (dataArray[i+1].contains("True")) {
					winnerString = dataArray[i+2] + " " + dataArray[i+3];
					lvInfo.getItems().add(winnerString + " has won!");
					highlightWin();
				} else { 
					lvInfo.getItems().add("No winner yet!");
				}
			}
		}
	}
	// Recieve data from client. Update app based on the data. parseData method used to
	// update the board based on the data string.
	public void createClient(int port) {
		clientConnection = new Client(data->{
			Platform.runLater(()->{
				System.out.println("Data: " + data.toString());
				if (data instanceof String) {
					String info = data.toString();
					if (info.contains("Starting new game.")) {
						resetGame();
					} else if (info.contains("Starting Game")) {
						createGameScreen();
					} else if (info.contains("yellow")) {
						clientRed = false;
					} else if (info.contains("Additional Player Needed") && replay) {
						root.setBottom(menuVbox2Copy);
						root.setAlignment(menuVbox2Copy, Pos.BOTTOM_RIGHT);
						stage.setScene(s1);
					} else if (info.contains("@SetName")){
						String[] s = info.split("_");
						playerName = s[1];
						System.out.println("Player name: " + playerName);
					} else if (info.contains("@sendBack")) {
						parseData(info);
					} else {
						lvInfo.getItems().add(info);
						lvInfoCopy.getItems().add(info);
					}
				}
			});}, port);
		clientConnection.start();
	}
	
	public void connect(ActionEvent e) {
		if (!tfFilter()) {
			return;
		}
		portNum = Integer.valueOf(tfPortNum.getText());
		System.out.println("Port num is " + portNum);
		createClient(portNum);
	}
	
	// Gridpane's coordinate origin is at the top-left. Move to bottom-left by changing y value, which makes
	// readability easier when stating which move was made.
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
	
	public void createGameScreen() {
		System.out.println("createGameScreen called");
		gameRoot = new BorderPane();
		gameBoard = new GridPane();
		// Create Game Board
		for (int i = 0; i < 6; i++) {
			gameBoard.getRowConstraints().add(new RowConstraints());
			gameBoard.getColumnConstraints().add(new ColumnConstraints());
		}
		gameBoard.getColumnConstraints().add(new ColumnConstraints());
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				String coord = "" + i + getCoordinates(j);
				GameButton temp = new GameButton(coord);
				temp.setOnAction(e->{buttonListen(e, coord, temp);});
				temp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				if (!clientRed) {
					temp.setYellow(true);
				}
				gameBoard.add(temp, i, j);
				buttonGrid[i][j] = temp;
				buttons.put(coord, temp);
			}
		}
		gameBoard.setAlignment(Pos.CENTER);
		//gameBoard.setStyle("-fx-background-color: blue");
		
		//gameBoard.setMaxSize(650, 700);
		//lvInfo.setMaxSize(200, 700);
		lvInfo.setMaxHeight(438);
		gameHBox = new HBox(gameBoard, lvInfo);
		HBox.setHgrow(gameBoard, Priority.ALWAYS);
		HBox.setHgrow(lvInfo, Priority.ALWAYS);
		gameHBox.setMargin(gameBoard, new Insets(5));
		gameHBox.setMargin(lvInfo, new Insets(5));
		//gameHBox.setStyle("-fx-background-color: red");
		gameHBox.setAlignment(Pos.CENTER);
		gameHBox.setMaxSize(900, 700);
		
		pName = new Text(playerName);
		sTurn = new Text(" Current Turn: ");
		playTurn = new Text("Player");
		pName.setFont(new Font("Trebuchet MS", 22));
		sTurn.setFont(new Font("Trebuchet MS", 22));
		playTurn.setFont(new Font("Trebuchet MS", 22));
		
		HBox hboxName = new HBox(pName);
		HBox hboxName2 = new HBox(sTurn, playTurn);
		//textHBox = new HBox(pName, sTurn, playTurn);
		textHBox = new HBox(hboxName, hboxName2);
		textHBox.setAlignment(Pos.CENTER);
		textHBox.setSpacing(900);
		gameRoot.setCenter(gameHBox);
		gameRoot.setTop(textHBox);
		gameRoot.setPadding(new Insets(10));
		gameRoot.setStyle("-fx-background-color: #4BD5E7");
		s2 = new Scene(gameRoot, 1280, 720);
		stage.setScene(s2);
	}
	
	// Check that the move is valid.
	public boolean placementCheck(String coord) {
		GameButton b = buttons.get(coord);
		if (b.active) {
			lvInfo.getItems().add("Error: Piece already played! Try Again.");
			return false;
		}
		int y = Character.getNumericValue(coord.charAt(1));
		if (y > 0) {
			int intCoord = Integer.valueOf(coord);
			String lowerCoord = "";
			if (intCoord < 10) {
				lowerCoord = "0" + (intCoord - 1);
			} else {
				lowerCoord = "" + (intCoord - 1);
			}
			if (!buttons.get(lowerCoord).active) {
				lvInfo.getItems().add("Error: Move invalid! Try Again.");
				return false;
			}
		}
		return true;
	}
	
	// Listen for a GameButton to be pressed. Place the piece if valid.
	public void buttonListen(ActionEvent e, String coord, GameButton b) {
		System.out.println("You hit button: " + coord);
		if (!placementCheck(coord)) {
			return;
		}
		placements++;
		b.placePiece();
		gameBoard.setDisable(true);
		clientConnection.makeMove(coord);
		if (GameLogic.checkWin(buttonGrid)) {
			System.out.println("Winner!");
			clientConnection.gameDone();
		} else if (placements >= 42) {
			System.out.println("Tie");
			clientConnection.gameTie();
		}
	}
	
	public void endScreen() {
		System.out.println("endScreen called");
		endRoot = new BorderPane();
		if (!tie) {
			winner.setText(winnerString + " has won!!");
		} else {
			winner.setText("Tie Game.");
		}
		
		if (winnerString.contains(playerName)) {
			System.out.println("Winner is " + playerName);
			winner.setFill(Color.GREEN);
		} else {
			System.out.println(playerName + " did not win.");
			winner.setFill(Color.RED);
		}
		winner.setFont(new Font("Impact", 80));
		playAgain = new Button("Play Again");
		playAgain.setStyle("-fx-background-color: lawngreen;" + "-fx-background-radius: 15px;");
		playAgain.setTextFill(Color.WHITE);
		exit = new Button("Exit");
		exit.setStyle("-fx-background-color: red;" + "-fx-background-radius: 15px;");
		exit.setTextFill(Color.WHITE);
		playAgain.setMaxSize(300, 150);
		exit.setMaxSize(300, 150);
		playAgain.setFont(new Font("Impact", 22));
		exit.setFont(new Font("Impact", 22));
		endHBox = new HBox(playAgain, exit);
		endHBox.setAlignment(Pos.CENTER);
		endHBox.setMaxSize(1280, 400);
		endHBox.setHgrow(playAgain, Priority.ALWAYS);
		endHBox.setHgrow(exit, Priority.ALWAYS);
		endHBox.setSpacing(20);
		exit.setOnAction(e->{clientConnection.exit();System.exit(0);});
		playAgain.setOnAction(e->{replay = true;wait.setVisible(true);playAgain.setDisable(true); clientConnection.newGame();});
		endVbox = new VBox(winner, endHBox);
		endVbox.setAlignment(Pos.CENTER);
		endVbox.setSpacing(20);
		endVbox.setMaxSize(1280, 720);
		endRoot.setCenter(endVbox);
		//endVbox.setStyle("-fx-background-color: red");
		endVbox.setVgrow(endHBox, Priority.ALWAYS);
		//endHBox.setStyle("-fx-background-color: blue");
		endRoot.setPadding(new Insets(20));
		
		wait = new Text("*Waiting for player 2");
		endRoot.setBottom(wait);
		wait.setVisible(false);
		s3 = new Scene(endRoot, 1280, 720);
		stage.setScene(s3);
	}
	// Reset the game for another round.
	public void resetGame() {
		System.out.println("resetGame: restarting game.");
		placements = 0;
		tie = false;
		for (GameButton b : buttons.values()) {
			b.resetButton();
			if (!clientRed) {
				b.setYellow(true);
			} else {
				b.setYellow(false);
			}
		}
		stage.setScene(s2);
	}
}
