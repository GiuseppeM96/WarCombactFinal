package game.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import game.manager.GameMenu;

public class MyServer {

	ArrayList<ServerReciverMessage> connected;
	ServerSocket serverSocket;
	int port;
	int numPlayer;

	/**
	 * Constructor with one parameter
	 * 
	 * @param numPlayer
	 *            stand for the number of player that play this match
	 * @param port
	 * 
	 */
	public MyServer(int numPlayer, int port) {
		
		this.port = port;
		try {
			this.numPlayer = numPlayer;
			serverSocket = new ServerSocket(port);
			connected = new ArrayList<ServerReciverMessage>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * accept all players and give a code to each one and notify that game can
	 * start
	 */
	public void lauchServer() {
		for (int i = 0; i < numPlayer; i++)
			try {
				System.out.println("Wait ....");
				Socket socket = serverSocket.accept();
				System.out.println("New connection");
				ServerReciverMessage newClientConnected = new ServerReciverMessage(socket, this, i);
				connected.add(newClientConnected);
				newClientConnected.out.println(i);
				newClientConnected.out.flush();
				newClientConnected.start();
			} catch (IOException e) {

				e.printStackTrace();
			}
		System.out.println("all player are connected");
		for (int i = 0; i < numPlayer; i++)
			for (ServerReciverMessage serverReciverMessage : connected) {
				serverReciverMessage.out.println(i);
				serverReciverMessage.out.flush();
			}
		for (ServerReciverMessage serverReciverMessage : connected) {
			serverReciverMessage.out.println("go");
			serverReciverMessage.out.flush();
		}
	}

	/**
	 * does a broadcast of the message
	 * 
	 * @param message
	 *            contains the message that will be send
	 */
	public void sendNewMessage(String message) {
		for (ServerReciverMessage areConnected : connected) {
			areConnected.out.println(message);
			areConnected.out.flush();
		}

	}

	public boolean endMatch() {
		boolean find = false;
		for (ServerReciverMessage areConnected : connected) {
			if (!areConnected.scoreRecived)
				find = true;
		}
		return !find;
	}

	/**
	 * interrupts all thread that listen message from client and close the
	 * server socket
	 */
	public void shutdownServer() {

		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ServerSocket Not Close");
		}
		connected.clear();

	}
}
