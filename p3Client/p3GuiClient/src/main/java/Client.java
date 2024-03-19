import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

import javafx.scene.control.ListView;



public class Client extends Thread{

	
	Socket socketClient;
	int port;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	CFourInfo cfi;
	boolean connected;
	
	private Consumer<Serializable> callback;
	
	Client(Consumer<Serializable> call){
	
		callback = call;
	}
	Client(Consumer<Serializable> call, int p){
		
		callback = call;
		port = p;
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",port);
		System.out.println("Socket made");
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {
			System.out.println("no");
			e.printStackTrace();
		}
		connected = true;
		
		// reading in data
		while(true) {
			 
			try {
			Object data = in.readObject();
			// Create a string with all updates that need to be made to the applicaiton.
			if (data.toString().contains("CFourInfo")) {
				CFourInfo t = (CFourInfo) data;
				cfi = t;
				String sendBack = "@sendBack ";
				// Whose turn is it anyway??
				if (t.turn) {
					sendBack += "@Turn True ";
				} else {
					sendBack += "@Turn false ";
				}
				// Who am i 0-0
				sendBack += "@Name " + t.name + " ";
				// What happened last turn ;p
				if (t.moveMade && t.turn) {
					sendBack += "@Move " + t.play + " ";
				}
				if (!t.update.contains("null")) {
					sendBack += "@Update " + t.update + " ";
				}
				// Any one win (0-0)7
				if (t.gameDone) {
					sendBack += "@Win True ";
					sendBack += cfi.winner;
				} else {
					sendBack += "@Win false";
				}
				callback.accept(sendBack);
			} else if (data.toString().contains("Starting new game.")) {
				System.out.println("Client: Telling app to restart");
				callback.accept("Starting new game.");
			} else {
				callback.accept(data.toString());
			}
			System.out.println("Data read");
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(CFourInfo data) {
		
		try {
			out.reset();
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Tell server what move has just been made
	public void makeMove(String coord) {
		System.out.println("Making the move on " + coord + ".");
		cfi.play = coord;
		cfi.turn = false;
		cfi.moveMade = true;
		send(cfi);
	}
	
	// Tell server game has been won
	public void gameDone() {
		System.out.println("Client: Sending game complete");
		cfi.gameDone = true;
		cfi.winner = cfi.name;
		cfi.update = cfi.name + " has won the game!!";
		cfi.moveMade = false;
		send(cfi);
	}
	
	// Tell server a tie has occured
	public void gameTie() {
		System.out.println("Client: Sending game Tie");
		cfi.gameDone = true;
		cfi.winner = "";
		cfi.update = "Game Tied.";
		cfi.moveMade = false;
		send(cfi);
	}

	// Tell server client wants to start a new game
	public void newGame() {
		cfi.reset = true;
		cfi.winner = "";
		cfi.gameDone = false;
		cfi.update = "connected";
		send(cfi);
	}
	
	// Tell server when client closes out applicaiton
	public void exit() {
		cfi.reset = true;
		cfi.winner = "";
		cfi.gameDone = false;
		cfi.update = "dead";
		send(cfi);
	}

}
