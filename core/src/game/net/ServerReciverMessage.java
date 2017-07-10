package game.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerReciverMessage extends Thread{
	
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	MyServer server;
	int code;
	
	/**
	 * Create a thread that listen a message from a single client
	 * @param s indicates socket where message travel
	 * @param serv server that will send received message to other client
	 * @param i
	 */
	public ServerReciverMessage(Socket s,MyServer serv,int i) {
		socket=s;
		server=serv;
		code=i;
		try {
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());
			//out.println(i);
			//out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(true){
			try {
				String message=in.readLine();
				server.sendNewMessage(message);
			} catch (IOException e) {
				System.out.println("Connesione out");
				server.connected.remove(this);
				server.sendNewMessage(6+";"+code+";"+0+";"+0+";"+0+";");
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
