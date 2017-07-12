package game.net;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class ServerThread extends Thread{
	
	MyServer server;
	/**
	 * Create a thread that launch server
	 * @param server
	 */
	public ServerThread(MyServer server) {
		this.server=server;
	}
	
	@Override
	public void run() {
		server.lauchServer();
	}

	
}
