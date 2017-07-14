package game.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;

import game.object.AddLifePoints;
import game.object.AddMachineGunShots;
import game.object.AddShotGunShots;
import game.object.Bash;
import game.object.BigHut;
import game.object.BlackHouse;
import game.object.Castle;
import game.object.Character;
import game.object.Enemy;
import game.object.Hut;
import game.object.Letter;
import game.object.Map;
import game.object.ShotEnemy;
import game.object.ShotPlayer;
import game.object.StaticObject;
import game.object.Tree;
import game.object.Well;
import game.pools.GameConfig;
import game.pools.ImagePool;
import game.threads.EnemyThread;
import game.personalAI.*;

public class World {

	static public Class<? extends Enemy> classe;
	public String mission;
	public int found;
	static public ArrayList<StaticObject> objects;
	static public ArrayList<Enemy> enemies;
	static public Character player;
	static public ArrayList<ShotPlayer> shotsPlayer;
	static public ArrayList<ShotEnemy> shotsEnemy;
	static public int level;
	public boolean levelCompleted;
	static Well well;
	Map gameMap;
	public EnemyThread enemiesOne;
	public String className;
	public static int score;

	public static boolean playerShot;
	public static boolean enemyAdded;

	/**
	 * Create a new world
	 * 
	 * @param i
	 *            indicates level world
	 * @param playerPosition
	 *            indicates where we want player located
	 * @param className
	 *            class that contain the AI that we want
	 */
	public World(int i, Vector2 playerPosition, String className) {

		objects = new ArrayList<StaticObject>();
		shotsPlayer = new ArrayList<ShotPlayer>();
		enemies = new ArrayList<Enemy>();
		player = new Character();
		shotsEnemy = new ArrayList<ShotEnemy>();
		levelCompleted = false;
		gameMap = new Map(level);
		score = 0;
		playerShot = false;
		enemyAdded = false;
		this.className = className;
		if (className == "Enemy" || className == null || className == "game.object.Enemy")
			try {
				classe = (Class<? extends Enemy>) Class.forName("game.object.Enemy");
				className = "game.object.Enemy";
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else {
			try {
				classe = (Class<? extends Enemy>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// I check that the class extends Enemy
			if (!checkThatObjectIsAnEnemy()) {
				try {
					classe = (Class<? extends Enemy>) Class.forName("game.object.Enemy");
					className = "game.object.Enemy";
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		found = 1;
		switch (i) {
		case 1:
			mission = "HELP";
			break;
		case 2:
			mission = "POISON";
			break;
		case 3:
			mission = "SAVEVILLAGE";
			break;
		default:
			break;
		}
		level = i;
		initWorld(playerPosition);
		enemiesOne = new EnemyThread(player.getPosition());
	}

	/**
	 * Check if the class loaded is a class that extends enemy
	 * 
	 * @return boolean
	 */
	private boolean checkThatObjectIsAnEnemy() {
		if (classe.isInterface())
			return false;
		try {
			if (classe.getMethod("getMoveDirection", Vector2.class) != null && classe.getMethod("shotAI") != null
					&& classe.getMethod("getType") != null)
				return true;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Map getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map gameMap) {
		this.gameMap = gameMap;
	}

	public void movePlayerUp(float dt) {
		player.move(0, dt);
	}

	public void movePlayerDown(float dt) {
		player.move(2, dt);
	}

	public void movePlayerRight(float dt) {
		player.move(1, dt);
	}

	public void movePlayerLeft(float dt) {
		player.move(3, dt);
	}

	/**
	 * initialize world reading map from file
	 */
	public void initWorld(Vector2 playerPosition) {

		player.setPosition(playerPosition);
		File worldFile = new File(getLevelFile(level));
		try {
			loadObjectFromFile(worldFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Read file and populate world
	 * 
	 * @param fileMap
	 *            file that contains the information of world's object
	 * @throws IOException
	 */
	private void loadObjectFromFile(File fileMap) throws IOException {
		// FileReader reader =new FileReader(fileMap);
		BufferedReader buffer = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileMap.getPath())));
		if (GameMenu.loadGame) {
			String line = buffer.readLine();

			try {
				classe = (Class<? extends Enemy>) Class.forName(line);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GameMenu.className = line;

		}
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
			if (type.equals("i")) {
				score = convert(codx);
				player.lifePoints = convert(cody);
			} else if (type.equals("l")) {
				level = convert(codx);
				found = convert(cody);
			} else {
				StaticObject tmp = createNewObject(type, codx, cody);
				if (tmp instanceof Map)
					gameMap = (Map) tmp;
				else if (tmp instanceof Enemy) {
					// ((Enemy)tmp).setAI(50);
					enemies.add((Enemy) tmp);
				} else if (tmp instanceof Well)
					well = (Well) tmp;
				else if (tmp instanceof Character) {
					player = (Character) tmp;
				} else
					objects.add(tmp);

			}
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
	 * convert string to int
	 * 
	 * @param cod
	 *            string that will be converted
	 * @return int
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
		case "12":
			try {
				return classe.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			return new Character();
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param level
	 *            indicates level that we want load
	 * @return Path of the file map depending on level
	 */
	public String getLevelFile(int level) {
		if (!GameMenu.loadGame) {
			switch (level) {
			case 1:
				return "LevelOne.txt";
			case 2:
				return "LevelTwo.txt";
			case 3:
				return "LevelThree.txt";
			case 4:
				return "FreeLevel.txt";
			default:
				return null;
			}
		} else {
			if (GameMenu.free) {
				return "Free/" + GameMenu.userInfo.userName + ".txt";
			}
			return "Story/" + GameMenu.userInfo.userName + ".txt";

		}
	}

	/**
	 * 
	 * @return A list that contains all object in the world
	 */
	public ArrayList<StaticObject> getListObject() {
		ArrayList<StaticObject> allObjects = new ArrayList<StaticObject>();
		allObjects.add(gameMap);
		if (well != null)
			allObjects.add(well);
		allObjects.addAll(objects);
		allObjects.addAll(enemies);
		allObjects.add(player);
		allObjects.addAll(shotsPlayer);
		allObjects.addAll(shotsEnemy);
		return allObjects;
	}

	/**
	 * check if the parameter o collide with each other object in the world
	 * 
	 * @param o
	 *            StaticObject who we wants know if it collides
	 * @return StaticObject that collied with o
	 */
	public static StaticObject checkCollisionObject(StaticObject o) {
		ArrayList<StaticObject> tmpObjects = new ArrayList<StaticObject>(objects);
		int i = 0;
		for (StaticObject s : tmpObjects) {
			if (o.collide(s))
				return objects.get(i);
			i++;
		}
		if (!(o instanceof Enemy))
			if (o.collide(well))
				return well;
		tmpObjects.clear();
		if (o instanceof ShotPlayer || o instanceof Character) {
			tmpObjects = new ArrayList<StaticObject>(enemies);
			i = 0;
			for (StaticObject e : tmpObjects) {
				if (o.collide(e))
					return enemies.get(i);
				i++;
			}
			tmpObjects.clear();
		}
		if (o instanceof Enemy) {
			tmpObjects = new ArrayList<StaticObject>(shotsPlayer);
			i = 0;
			for (StaticObject e : tmpObjects) {
				if (o.collide(e))
					return shotsPlayer.get(i);
				i++;
			}
			tmpObjects.clear();
			if (o.collide(player))
				return player;
		}
		if (o instanceof ShotEnemy)
			if (o.collide(player))
				return player;
		return null;
	}

	/**
	 * Add new shot of parameter currentPlayer
	 * 
	 * @param currentPlayer
	 *            NetCharacter that have shot
	 */
	public void playerHasShot() {
		ArrayList<ShotPlayer> newShots = new ArrayList<ShotPlayer>();
		if (!player.hasNotShots()) {
			for (int i = 0; i < player.getCurrentWeapon().getNumShots(); i++) {
				ShotPlayer tmp = new ShotPlayer(i - player.getCurrentWeapon().getNumShots() + 1,
						player.getCurrentWeapon());
				newShots.add(tmp);

			}
			shotsPlayer.addAll(newShots);
			player.updateNumShots();
		}
	}

	/**
	 * kills enemy
	 * 
	 * @param collisionObject
	 * @param tmp
	 */
	public void damageEnemy(StaticObject collisionObject, ShotPlayer tmp) {
		((Enemy) collisionObject).alive = false;
	}

	/**
	 * evolves shots and check their collision
	 */
	public void updateShots() {
		ArrayList<ShotPlayer> shotPlayerDied = new ArrayList<ShotPlayer>();
		ArrayList<ShotEnemy> shotEnemyDied = new ArrayList<ShotEnemy>();
		for (ShotPlayer tmp : shotsPlayer) {
			if (tmp.getTarget() >= 50 || !tmp.visible
					|| !(tmp.getPosition().x >= 0 && tmp.getPosition().y >= 0
							&& tmp.getPosition().x < GameConfig.MAP_SIZE.x - ImagePool.shot.getWidth()
							&& tmp.getPosition().y < GameConfig.MAP_SIZE.y - ImagePool.shot.getHeight())) {
				shotPlayerDied.add(tmp);
			} else {
				if (tmp.getTarget() >= 0) {
					tmp.visible = true;
					StaticObject collisionObject = checkCollisionObject(tmp);
					if (collisionObject != null)
						tmp.visible = false;
					if (collisionObject instanceof Enemy) {
						damageEnemy(collisionObject, tmp);
						score += 100;
					}
				}
				tmp.setTarget(tmp.getTarget() + 1);
				tmp.move(tmp.getCodDirection(), Gdx.graphics.getDeltaTime());
			}
		}
		shotsPlayer.removeAll(shotPlayerDied);
		int cont = 0;
		for (ShotEnemy tmp : shotsEnemy) {
			if (tmp.getTarget() >= 50 || !tmp.visible) {
				shotEnemyDied.add(tmp);
			} else {
				if (tmp.getTarget() >= 0) {
					tmp.visible = true;
					StaticObject collisionObject = checkCollisionObject(tmp);
					if (collisionObject != null)
						tmp.visible = false;
					if (collisionObject instanceof Character) {
						player.lifePoints -= 30;
					}
				}
				if (tmp.visible) {
					tmp.setTarget(tmp.getTarget() + 1);
					tmp.move(tmp.getCodDirection(), Gdx.graphics.getDeltaTime());
				}
			}
		}
		shotsEnemy.removeAll(shotEnemyDied);
	}

	public void stopEnemy() {

		enemiesOne.stop();
	}

	/**
	 * Create new thread to restart enemy
	 */
	public void resumeEnemy() {
		enemiesOne = new EnemyThread(player.getPosition());
	}

	/**
	 * check if player is alive
	 * 
	 * @return
	 */
	public boolean playerIsAlive() {
		if (player.lifePoints <= 0)
			return false;
		return true;
	}

	/**
	 * empties all list of object in the world and stop thread
	 */
	public synchronized void clear() {
		score = 0;
		enemiesOne.stopThread = true;
		objects.clear();
		well = null;
		enemies.removeAll(enemies);
		shotsEnemy.clear();
		shotsPlayer.clear();
	}

	/**
	 * function that create new enemy and add to the enemy list
	 */
	public void generateEnemy() {
		Enemy e = new Enemy();
		Vector2 position = new Vector2(well.getPosition());
		e.setPosition(position);
		e.setDirection(2);
		enemiesOne.stopThread = true;
		while (enemiesOne.isAlive()) {
		}
		enemies.add(e);
		enemiesOne = new EnemyThread(player.getPosition());
		enemiesOne.start();
	}

}
