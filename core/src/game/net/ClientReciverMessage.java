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
	NetGameScreen clientScreen;
	int i = 0;
	boolean otherPlayerShot;
	boolean stopThread;
	boolean canClose;

	/**
	 * Create a Thread that listen message from server
	 * 
	 * @param s
	 *            socket where server send message
	 * @param cl
	 *            Game Screen where we apply changing
	 */
	public ClientReciverMessage(Socket s, NetGameScreen clientScreen) {
		socket = s;
		otherPlayerShot = false;
		stopThread = false;
		canClose = false;

		this.clientScreen = clientScreen;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!stopThread) {
			if (!clientScreen.finish) {
				try {

					String rcv = in.readLine();
					if (clientScreen.wait) {
						clientScreen.worldGame.currentPlayer.code = convert(rcv);
						clientScreen.wait = false;
						float x, y;
						x = clientScreen.worldGame.spawnPoints.get(
								clientScreen.worldGame.currentPlayer.code % clientScreen.worldGame.spawnPoints.size())
								.getPosition().x;
						y = clientScreen.worldGame.spawnPoints.get(
								clientScreen.worldGame.currentPlayer.code % clientScreen.worldGame.spawnPoints.size())
								.getPosition().y;
						clientScreen.worldGame.currentPlayer.setPosition(new Vector2(x, y));
					} else if (!rcv.contains(";")) {

						if (rcv.equals("finish")) {
							clientScreen.finish = true;
						} else if (rcv.equals("go"))
							clientScreen.gameMenu.startGameNet = true;
						else {
							int newCode = convert(rcv);
							if (newCode != clientScreen.worldGame.currentPlayer.code) {
								NetCharacter newPlayer = new NetCharacter();
								newPlayer.code = newCode;
								float x, y;
								x = clientScreen.worldGame.spawnPoints
										.get(newCode % clientScreen.worldGame.spawnPoints.size()).getPosition().x;
								y = clientScreen.worldGame.spawnPoints
										.get(newCode % clientScreen.worldGame.spawnPoints.size()).getPosition().y;
								newPlayer.setPosition(new Vector2(x, y));
								clientScreen.worldGame.otherPlayers.add(newPlayer);
							}
						}
					} else {
						NetMessage m = new NetMessage(rcv);
						if (m.action == 0) {
							for (NetCharacter c : clientScreen.worldGame.otherPlayers) {
								if (m.code == c.code) {
									c.setVelocity(m.x);
									c.move(m.dir, m.y);
									c.stateAnimationTime += m.y;
									break;
								}
							}
						} else if (m.action == 1) {
							boolean check = false;
							for (StaticObject sb : clientScreen.worldGame.objects) {
								if (sb.getPosition().x == m.x && sb.getPosition().y == m.y) {
									if (m.code == 0 && sb instanceof AddLifePoints) {
										check = true;
									} else if (m.code == 1 && sb instanceof AddShotGunShots) {
										check = true;
									} else if (m.code == 2 && sb instanceof AddMachineGunShots) {
										check = true;
									}
									if (check) {
										clientScreen.removeObjects(sb);
										break;
									}
								}
							}
						} else if (m.action == 2) {

							if (m.code != clientScreen.worldGame.currentPlayer.code)
								for (NetCharacter players : clientScreen.worldGame.otherPlayers) {
									if (m.code == players.code) {
										players.shoting = true;
										break;
									}
								}
						} else if (m.action == 3) {
							if (m.code != clientScreen.worldGame.currentPlayer.code)
								for (NetCharacter players : clientScreen.worldGame.otherPlayers) {
									if (m.code == players.code) {
										players.changeWeapon();
										break;
									}
								}
						} else if (m.action == 4) {
							if (m.code != clientScreen.worldGame.currentPlayer.code)
								for (NetCharacter players : clientScreen.worldGame.otherPlayers) {
									if (m.code == players.code) {
										players.died = true;
										break;
									}
								}
						} else if (m.action == 5) {
							if (m.y != clientScreen.worldGame.currentPlayer.code)
								clientScreen.gameMenu.scorePlayers.add(new ScorePlayer(m.name, m.x));
						} else if (m.action == 6) {
							for (NetCharacter nc : clientScreen.worldGame.otherPlayers)
								if (nc.code == m.code) {
									clientScreen.worldGame.otherPlayers.remove(nc);
									break;
								}
						}
					}

				} catch (IOException e) {

					System.out.println("Server Disconnected");
					if (clientScreen.gameMenu.server != null)
						clientScreen.gameMenu.server.shutdownServer();
					clientScreen.gameMenu.swap(0);
					break;
				}
			} else
				break;

		}
		try {
			socket.close();
			System.out.println("Client socket closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Convert a String to int
	 * 
	 * @param rcv
	 *            String that we want to convert
	 * @return string converted
	 */
	private int convert(String rcv) {
		char[] tmp = rcv.toCharArray();
		int result = 0;
		for (int i = 0; i < tmp.length; i++) {
			result *= 10;
			result += tmp[i] - '0';
		}
		return result;
	}
}
