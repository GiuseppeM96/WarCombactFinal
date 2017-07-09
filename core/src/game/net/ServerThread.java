package game.net;

public class ServerThread extends Thread{
	
	MyServer server;
	public ServerThread(MyServer server) {
		this.server=server;
	}
	@Override
	public void run() {
		server.lauchServer();
	}
}
