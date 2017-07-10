package game.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import game.manager.GameMenu;


public class MyServer {
	
	ArrayList<ServerReciverMessage> connected;
	ServerSocket ss;
	int port;
	int numPlayer;
	/**
	 * Constructor with one parameter
	 * @param numPlayer stand for the number of player that play this match
	 * 
	 */
	public MyServer(int numPlayer) {
		port=12345;
		try {
			this.numPlayer=numPlayer;
			ss=new ServerSocket(port);
			connected=new ArrayList<ServerReciverMessage>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * accept all players and give a code to each one and notify that game can start 
	 */
	public void lauchServer(){
		for(int i=0;i<numPlayer;i++)
			try {
				System.out.println("Wait ....");
				Socket s=ss.accept();
				System.out.println("New connection");
				ServerReciverMessage newClientConnected=new ServerReciverMessage(s,this,i);
				connected.add(newClientConnected);
				newClientConnected.out.println(i);
				newClientConnected.out.flush();
				newClientConnected.start();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		System.out.println("all player are connected");
		for(int i=0;i<numPlayer;i++)
			for(ServerReciverMessage s:connected){
				s.out.println(i);
				s.out.flush();
		}
		for(ServerReciverMessage s:connected){
			s.out.println("go");
			s.out.flush();
		}
	}
	
	/**
	 * does a broadcast of the message
	 * @param message contains the message that will be send 
	 */
	public void sendNewMessage(String message) {
		for(ServerReciverMessage c:connected){
				c.out.println(message);
				c.out.flush();
		}
		
	}
	/**
	 * interrupts all thread that listen message from client and close the server socket
	 */
	public void shutdownServer(){
		for(ServerReciverMessage srm:connected){
			srm.interrupt();
		}
		try {
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

