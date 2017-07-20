package game.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import game.object.AddLifePoints;
import game.object.AddMachineGunShots;
import game.object.AddShotGunShots;
import game.object.Bash;
import game.object.BigHut;
import game.object.BlackHouse;
import game.object.Castle;
import game.object.Hut;
import game.object.Letter;
import game.object.Map;
import game.object.Shot;
import game.object.StaticObject;
import game.object.Tree;
import game.object.Well;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class NetWorld {
	public ArrayList<StaticObject> objects;
	public NetCharacter currentPlayer;
	public ArrayList<Well> spawnPoints;
	public ArrayList<Shot> shots;
	ArrayList<Shot> newShots;
	public ArrayList<NetCharacter> otherPlayers;
	Map gameMap;
	public static int score;
	public static int diedTimes;
	public static boolean currentPlayerShot;

	/**
	 * Constructor
	 * 
	 * @param code
	 *            indicates code of current player
	 */
	public NetWorld(int code) {

		currentPlayer = new NetCharacter();
		otherPlayers = new ArrayList<NetCharacter>();
		objects = new ArrayList<StaticObject>();
		spawnPoints = new ArrayList<Well>();
		shots = new ArrayList<Shot>();
		newShots = new ArrayList<Shot>();
		currentPlayer.code = code;

		score = 0;
		diedTimes = 0;
		currentPlayerShot = false;

		initWorld();
	}

	public Map getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map gameMap) {
		this.gameMap = gameMap;
	}

	public void movecurrentPlayerUp(float dt) {
		currentPlayer.move(0, dt);
	}

	public void movecurrentPlayerDown(float dt) {
		currentPlayer.move(2, dt);
	}

	public void movecurrentPlayerRight(float dt) {
		currentPlayer.move(1, dt);
	}

	public void movecurrentPlayerLeft(float dt) {
		currentPlayer.move(3, dt);
	}

	/**
	 * initialize world reading map from file
	 */
	public void initWorld() {

		currentPlayer.setPosition(new Vector2(currentPlayer.code * 100, currentPlayer.code * 100));
		otherPlayers = new ArrayList<NetCharacter>();
		// File worldFile=new File("src/GameComplete.txt");
		File worldFile = new File("src/NetMap.txt");
		try {
			loadObjectFromFile(worldFile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addcurrentPlayer(ArrayList<NetCharacter> lst) {
		otherPlayers.addAll(lst);
	}

	/**
	 * Read file and populate world
	 * 
	 * @param fileMap
	 *            file that contains the information of world's object
	 * @throws IOException
	 */
	private void loadObjectFromFile(File fileMap) throws IOException {
		FileReader reader =new FileReader(fileMap);
		BufferedReader buffer = new BufferedReader(reader);
		String line = buffer.readLine();
		while (line != null) {
			String type = new String(), codx = new String(), cody = new String();
			char[] arrayLine = new char[line.length()];
			line.getChars(0, line.length(), arrayLine, 0);
			int i = 0;
			for (; arrayLine[i] != ';'; i++) {
				type += arrayLine[i];
			}
			i++;
			for (; arrayLine[i] != ';'; i++) {
				codx += arrayLine[i];
			}
			i++;
			for (; arrayLine[i] != ';'; i++) {
				cody += arrayLine[i];
			}
			StaticObject tmp = createNewObject(type, codx, cody);
			if (tmp instanceof Map)
				gameMap = (Map) tmp;
			else if (tmp instanceof Well)
				spawnPoints.add((Well) tmp);
			else
				objects.add(tmp);

			line = buffer.readLine();
		}
		buffer.close();
	}

	/**
	 * Creates an objects that have this code type and set its position
	 * 
	 * @param codtype
	 *            Object code type
	 * @param codx
	 *            code of x position
	 * @param cody
	 *            code of y position
	 * @return A new instance of the class with that code located in position
	 *         codx,cody
	 */
	private StaticObject createNewObject(String codtype, String codx, String cody) {
		StaticObject tmp = getObject(codtype);
		tmp.setPosition(new Vector2(convert(codx), convert(cody)));
		return tmp;
	}

	/**
	 * Convert a string to int
	 * 
	 * @param cod
	 *            string who we wants convert
	 * @return string converted to int
	 */
	private int convert(String cod) {
		char[] tmp = cod.toCharArray();
		int result = 0;
		for (int i = 0; i < tmp.length; i++) {
			result *= 10;
			result += tmp[i] - '0';
		}
		return result;
	}

	/**
	 * 
	 * @param codType
	 *            code of the object that we need
	 * @return A new instance of the class that have that code
	 */
	private StaticObject getObject(String codType) {

		switch (codType) {
		case "0":
			return new Map(3);
		case "1":
			return new Map(1);
		case "2":
			return new Map(2);
		case "3":
			return new Hut();
		case "4":
			return new Castle();
		case "5":
			return new BlackHouse();
		case "6":
			return new BigHut();
		case "7":
			return new Bash();
		case "8":
			return new Tree();
		case "9":
			return new AddMachineGunShots();
		case "10":
			return new AddShotGunShots();
		case "11":
			return new AddLifePoints();
		case "13":
			return new Letter('a');
		case "14":
			return new Letter('e');
		case "15":
			return new Letter('g');
		case "16":
			return new Letter('h');
		case "17":
			return new Letter('i');
		case "18":
			return new Letter('l');
		case "19":
			return new Letter('n');
		case "20":
			return new Letter('o');
		case "21":
			return new Letter('p');
		case "22":
			return new Letter('s');
		case "23":
			return new Letter('v');
		case "24":
			return new Well();
		case "25":
			return new NetCharacter();
		default:
			return null;
		}
	}

	/**
	 * check if the parameter o collide with each other object in the world
	 * 
	 * @param o
	 *            StaticObject who we wants know if it collides
	 * @return StaticObject that collied with o
	 */
	public StaticObject checkCollisionObject(StaticObject o) {
		int i = 0;
		for (StaticObject s : objects) {
			if (o.collide(s))
				return objects.get(i);
			i++;
		}
		i = 0;
		if (o instanceof Shot) {
			for (NetCharacter c : otherPlayers) {
				if (o.collide(c))
					return otherPlayers.get(i);
				i++;
			}
			if (o.collide(currentPlayer))
				return currentPlayer;
		}
		i = 0;
		if (o instanceof NetCharacter) {
			for (Shot s : shots) {
				if (o.collide(s))
					return shots.get(i);
				i++;
			}
		}
		return null;
	}

	/**
	 * Add new shot of parameter currentPlayer
	 * 
	 * @param currentPlayer
	 *            NetCharacter that have shot
	 */
	public void playerHasShot(NetCharacter currentPlayer) {
		if (!currentPlayer.hasNotShots()) {
			for (int i = 0; i < currentPlayer.getCurrentWeapon().getNumShots(); i++) {
				System.out.println(currentPlayer.code);
				Shot tmp = new Shot(i - currentPlayer.getCurrentWeapon().getNumShots() + 1,
						currentPlayer.getCurrentWeapon(), currentPlayer.code, new Vector2(currentPlayer.getPosition()),
						new Vector2(currentPlayer.getDirection()));
				System.out.println(tmp.codeOwner);
				newShots.add(tmp);
			}
			shots.addAll(newShots);
			newShots.clear();
			currentPlayer.updateNumShots();
		}
	}

	/**
	 * It evolves shots and check their collision
	 */
	public void updateShots() {
		ArrayList<Shot> shotDied = new ArrayList<Shot>();
		for (Shot tmp : shots) {
			if (tmp.getTarget() >= 50 || !tmp.visible
					|| !(tmp.getPosition().x >= 0 && tmp.getPosition().y >= 0
							&& tmp.getPosition().x < GameConfig.MAP_SIZE.x - ImagePool.shot.getWidth()
							&& tmp.getPosition().y < GameConfig.MAP_SIZE.y - ImagePool.shot.getHeight())) {
				shotDied.add(tmp);
			} else {
				if (tmp.getTarget() >= 0) {
					tmp.visible = true;
					StaticObject collisionObject = checkCollisionObject(tmp);

					if (collisionObject instanceof NetCharacter) {
						if (((NetCharacter) collisionObject).code == currentPlayer.code) {
							if (tmp.codeOwner != currentPlayer.code) {
								currentPlayer.lifePoints -= 30;
								tmp.visible = false;
							}
						} else if (tmp.codeOwner == currentPlayer.code) {
							score += 10;
							tmp.visible = false;
						}
					} else if (collisionObject != null)
						tmp.visible = false;
				}
				tmp.setTarget(tmp.getTarget() + 1);
				tmp.move(tmp.getCodDirection(), Gdx.graphics.getDeltaTime());
			}
		}
		shots.removeAll(shotDied);
	}

	/**
	 * check if the current player is alive
	 * 
	 * @return True if player is alive False in the other case
	 */
	public boolean currentPlayerIsAlive() {
		if (currentPlayer.lifePoints <= 0)
			return false;
		return true;
	}

}
