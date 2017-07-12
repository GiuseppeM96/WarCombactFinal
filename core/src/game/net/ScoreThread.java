package game.net;

public class ScoreThread extends Thread {

	MyServer server;
	
	public ScoreThread(MyServer server) {
		this.server=server;
	}
	@Override
	public void run() {
		while(!server.endMatch()){}
		server.sendNewMessage("finish");
	}
}
