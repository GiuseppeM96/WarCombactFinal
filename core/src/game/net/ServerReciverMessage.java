package game.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerReciverMessage extends Thread {

	Socket socket;
	BufferedReader in;
	PrintWriter out;
	MyServer server;
	int code;
	public boolean scoreRecived;
	public boolean stopTread;

	/**
	 * Create a thread that listen a message from a single client
	 * 
	 * @param s
	 *            indicates socket where message travel
	 * @param serv
	 *            server that will send received message to other client
	 * @param i
	 */
	public ServerReciverMessage(Socket s, MyServer server, int i) {
		socket = s;
		this.server = server;
		code = i;
		scoreRecived = false;
		stopTread = false;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			// out.println(i);
			// out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!stopTread) {
			try {
				String message = in.readLine();
				if (message != null) {
					server.sendNewMessage(message);
					if (message.substring(0, 2).equals("5;"))
						scoreRecived = true;
				} else
					break;

			} catch (IOException e) {
				System.out.println("Connesione out");
				server.connected.remove(this);
				server.sendNewMessage(6 + ";" + code + ";" + 0 + ";" + 0 + ";" + 0 + ";");
				break;
			}

		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
