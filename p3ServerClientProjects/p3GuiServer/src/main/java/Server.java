import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.util.Duration;


public class Server {

	int count = 1;	
	int portNum;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	boolean readyOne, readyTwo, dropped;
	CFourInfo serverCFI, c1, c2;
	
	
//	Server(Consumer<Serializable> call){
//		
//		callback = call;
//		server = new TheServer();
//		portNum = 5555;
//		server.start();
//	}
	Server(Consumer<Serializable> call, int p){
	
		callback = call;
		server = new TheServer();
		portNum = p;
		serverCFI = new CFourInfo();
		c1 = new CFourInfo();
		c2 = new CFourInfo();
		dropped = false;
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			serverCFI = new CFourInfo();
			try(ServerSocket mysocket = new ServerSocket(portNum);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("Client has connected to server: " + "client #" + count);
				clients.add(c);
				count++;
				if (count >= 3) {
					serverCFI.has2Players = true;
				}
				c.start();
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;
			}
			public void updateClients(String s) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(s);
					}
					catch(Exception e) {}
				}
			}
			// Give each client new information
			public void updateClients(CFourInfo cfi) {
				c1.play = cfi.play;
				c2.play = cfi.play;
				c1.turn = !c1.turn;
				c2.turn = !c2.turn;
				c1.moveMade = true;
				c2.moveMade = true;
				c1.gameDone = cfi.gameDone;
				c2.gameDone = cfi.gameDone;
				c1.winner = cfi.winner;
				c2.winner = cfi.winner;
				if (!cfi.update.contains("Tied")) {
					String update = cfi.name + " selected (" + cfi.play.charAt(0) + ", " + cfi.play.charAt(1) + ").";
					c1.update = update;
					c2.update = update;
					callback.accept(update);
				} else {
					c1.update = cfi.update;
					c2.update = cfi.update;
					callback.accept(cfi.update);
				}
				if (cfi.gameDone) {
					c1.turn = false;
					c2.turn = false;
				}
				try {
					clients.get(0).out.reset();
					clients.get(1).out.reset();
					clients.get(0).out.writeObject(c1);
					clients.get(1).out.writeObject(c2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			public void sendCleanCFI() {
				System.out.println("sendCleanCFI");
				c1.name = "Player 1";
				c2.name = "Player 2";
				c1.play = "";
				c2.play = "";
				c1.has2Players = true;
				c2.has2Players = true;
				c1.turn = true;
				c2.turn = false;
				c1.update = "null";
				c2.update = "null";
				try {
					clients.get(0).out.reset();
					clients.get(1).out.reset();
					clients.get(0).out.writeObject(c1);
					clients.get(1).out.writeObject(c2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			public void countdown() {
				PauseTransition pause = new PauseTransition(Duration.seconds(1));
				pause.setOnFinished(e -> {
					updateClients("5...");
				});
				pause.play();
				PauseTransition pause1 = new PauseTransition(Duration.seconds(2));
				pause1.setOnFinished(e -> {
					updateClients("4...");
				});
				pause1.play();
				PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
				pause2.setOnFinished(e -> {
					updateClients("3...");
				});
				pause2.play();
				PauseTransition pause3 = new PauseTransition(Duration.seconds(4));
				pause3.setOnFinished(e -> {
					updateClients("2...");
				});
				pause3.play();
				PauseTransition pause4 = new PauseTransition(Duration.seconds(5));
				pause4.setOnFinished(e -> {
					updateClients("1...");
				});
				pause4.play();
				PauseTransition pause5 = new PauseTransition(Duration.seconds(7));
				pause5.setOnFinished(e -> {
					updateClients("Starting Game");
					callback.accept("Starting Game");
					sendCleanCFI();
				});
				pause5.play();
				
			}
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				updateClients("Player #" + (count) + " has joined the game.");
				if (serverCFI.has2Players) {
					updateClients("2 Players ready");
					try {
						clients.get(1).out.writeObject("yellow");
						clients.get(0).out.writeObject("@SetName_Player 1");
			    		clients.get(1).out.writeObject("@SetName_Player 2");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					callback.accept("Enough players for game");
					countdown();
				}
					
				// Listen for client to send CFourInfo
				 while(true) {
					    try {
					    	CFourInfo clientCFI = (CFourInfo) in.readObject();
					    	if (clientCFI.gameDone) {
					    		callback.accept("Game is over");
					    	}
					    	if (clientCFI.reset) {
					    		if (clientCFI.name.contains("Player 1") && clientCFI.update.contains("connected")) {
					    			readyOne = true;
					    			callback.accept("Player 1 ready to play again.");
					    		} 
					    		if (clientCFI.name.contentEquals("Player 2") && clientCFI.update.contains("connected")) {
					    			callback.accept("Player 2 ready to play again.");
					    			readyTwo = true;
					    		}
					    	}
					    	if (readyOne && readyTwo) {
					    		System.out.println("Server: Telling client to restart.");
					    		callback.accept("Starting new game.");
					    		updateClients("Starting new game.");
					    		c1.gameDone = false;
					    		c2.gameDone = false;
					    		c1.moveMade = false;
					    		c2.moveMade = false;
					    		c1.reset = false;
					    		c2.reset = false;
					    		readyOne = false;
					    		readyTwo = false;
					    		sendCleanCFI();
					    	} else {
					    		updateClients(clientCFI);
						    	serverCFI = clientCFI;
					    	}
					    	// Update all of the clients with most recent cfi
					    	serverCFI = clientCFI;
					    	}
					    catch(Exception e) {
					    	count--;
					    	if (count < 3) {
								serverCFI.has2Players = false;
								dropped = true;
								updateClients("Additional Player Needed");
							}
					    	callback.accept("Client dropped");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
}


	
	

	
