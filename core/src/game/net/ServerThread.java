package game.net;

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
