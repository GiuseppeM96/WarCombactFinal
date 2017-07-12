package game.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.badlogic.gdx.math.Vector2;

import game.manager.GameMenu;
import game.object.AddLifePoints;
import game.object.AddMachineGunShots;
import game.object.AddShotGunShots;
import game.object.StaticObject;

public class ClientReciverMessage extends Thread {
	Socket socket;
	BufferedReader in;
	NetGameScreen c;
	int i=0;
	boolean otherPlayerShot;
	boolean stopThread;
	boolean canClose;

	/**
	 * Create a Thread that listen message from server
	 * @param s socket where server send message
	 * @param cl Game Screen where we apply changing 
	 */
	public ClientReciverMessage(Socket s,NetGameScreen cl) {
		socket=s;
		otherPlayerShot=false;
		stopThread=false;
		canClose=false;

		c=cl;
		try {
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				String rcv=in.readLine();
				if(c.wait){
					c.worldGame.currentPlayer.code=convert(rcv);
					System.out.println(c.worldGame.currentPlayer.code);
					c.wait=false;
					float x,y;
					x=c.worldGame.spawnPoints.get(c.worldGame.currentPlayer.code%c.worldGame.spawnPoints.size()).getPosition().x;
					y=c.worldGame.spawnPoints.get(c.worldGame.currentPlayer.code%c.worldGame.spawnPoints.size()).getPosition().y;
					c.worldGame.currentPlayer.setPosition(new Vector2(x,y));
				}
				else if(!rcv.contains(";")){
					
					if(rcv.equals("finish")){
						c.finish=true;
					}
					else if(rcv.equals("go"))
						c.gameMenu.startGameNet=true;
					else{
						int newCode=convert(rcv);
						if(newCode!=c.worldGame.currentPlayer.code){
							NetCharacter newPlayer=new NetCharacter();
							newPlayer.code=newCode;
							System.out.println(newCode);
							float x,y;
							x=c.worldGame.spawnPoints.get(newCode%c.worldGame.spawnPoints.size()).getPosition().x;
							y=c.worldGame.spawnPoints.get(newCode%c.worldGame.spawnPoints.size()).getPosition().y;
							newPlayer.setPosition(new Vector2(x,y));
							c.worldGame.otherPlayers.add(newPlayer);
						}
					}
				}
				else {
					NetMessage m=new NetMessage(rcv);
					if(m.action==0){
						for(NetCharacter c: c.worldGame.otherPlayers){
							if(m.code==c.code){
								c.setVelocity(m.x);
								c.move(m.dir, m.y);
								c.stateAnimationTime+=m.y;
								break;
							}
						}
					}
					else if(m.action == 1){
						boolean check=false;
						for(StaticObject sb: c.worldGame.objects){
							if(sb.getPosition().x==m.x &&sb.getPosition().y==m.y){
								if(m.code == 0 && sb instanceof AddLifePoints){
									check=true;
								}
								else if(m.code == 1 && sb instanceof AddShotGunShots){
									check=true;
								}
								else if(m.code == 2 && sb instanceof AddMachineGunShots){
									check=true;
								}
								if(check){
									c.removeObjects(sb);
									break;
								}
							}
						}
					}
					else if(m.action==2){
						 
						if(m.code!=c.worldGame.currentPlayer.code)
							for(NetCharacter players : c.worldGame.otherPlayers){
								if(m.code==players.code){
									players.shoting=true;
									break;
								}
							}
					}
					else if(m.action==3){
						if(m.code!=c.worldGame.currentPlayer.code)
							for(NetCharacter players : c.worldGame.otherPlayers){
								if(m.code==players.code){
									players.changeWeapon();
									break;
								}
							}
					}
					else if(m.action==4){
						if(m.code!=c.worldGame.currentPlayer.code)
							for(NetCharacter players : c.worldGame.otherPlayers){
								if(m.code==players.code){
									players.died=true;
									break;
								}
							}
					}
					else if(m.action==5){
						if(m.y != c.worldGame.currentPlayer.code)
							c.gameMenu.scorePlayers.add(new ScorePlayer(m.name,m.x));
					}
					else if(m.action==6){
						for(NetCharacter nc:c.worldGame.otherPlayers)
							if(nc.code==m.code){
								c.worldGame.otherPlayers.remove(nc);
								break;
							}		
					}
				}
			} catch (IOException e) {
				
				System.out.println("Server Disconnected");
				if(c.gameMenu.server!=null)
					c.gameMenu.server.shutdownServer();
				c.gameMenu.swap(0);
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
/**
 * Convert a String to int
 * @param rcv String that we want to convert
 * @return string converted
 */
	private int convert(String rcv) {
		char[] tmp =rcv.toCharArray();
		int result=0;
		for (int i = 0; i < tmp.length; i++) {
			result*=10;
			result+=tmp[i]-'0';
		}
		return result;
	}
}
