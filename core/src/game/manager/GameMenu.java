package game.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import game.net.MyServer;
import game.net.NetGameScreen;
import game.net.ScorePlayer;
import game.net.ServerThread;
import game.object.StaticObject;
import game.pools.ConstantField;
import game.pools.GameConfig;
import game.pools.ImagePool;
import game.pools.MusicPool;
import game.screens.BlackHouseScreen;
import game.screens.CastleScreen;
import game.screens.ChooseAIscreen;
import game.screens.FreeGameScreen;
import game.screens.GameManagerScreen;
import game.screens.GameModMenu;
import game.screens.HelpMenu;
import game.screens.IntroScreen;
import game.screens.PauseMenu;
import game.screens.PoitionScreen;
import game.screens.ScoreMenu;
import game.screens.ScoreNetScreen;
import game.screens.SettingMultiplayerScreen;
import game.screens.SettingsMenu;
import game.screens.StartMenuScreen;
import game.screens.StartMultiplayerScreen;
import game.screens.WaitScreen;

public class GameMenu extends Game {

	static public String className;
	public MyServer server;
	public boolean startGameNet;
	public static User userInfo;
	static HelpMenu helpMenu;
	static SettingsMenu settingsMenu;
	static StartMenuScreen startMenuScreen;
	static GameModMenu gameModMenu;
	static PauseMenu pauseMenu;
	public static GameManagerScreen gameLevel;
	static GameManagerScreen gameLevelTwo;
	static GameManagerScreen gameLevelThree;
	static BlackHouseScreen takePoison;
	static CastleScreen goToTheKing;
	static PoitionScreen potionExplotion;
	static FreeGameScreen freeModGame;
	static ScoreMenu scoreScreen;
	static ScoreNetScreen scoreNetScreen;
	static IntroScreen introScreen;
	static ChooseAIscreen chooseAIscreen;
	static NetGameScreen netGame;
	ServerThread connection;
	static StartMultiplayerScreen multiplayerMenu;
	static SettingMultiplayerScreen settingMultiplayerScreen;
	static WaitScreen waitScreen;
	public World world;
	public ArrayList<ScorePlayer> scorePlayers;
	public static boolean loadGame;
	public static int currentLevel;
	public boolean myServerLunched;

	public boolean start;
	public String serverAddress;
	public static boolean free;
	boolean multiplayer;
	public int port;

	@Override
	public void create() {
		currentLevel = 1;
		loadGame = false;
		myServerLunched = false;
		startGameNet = false;
		start = true;
		port = 12345;
		serverAddress = "127.0.0.1";
		free = false;
		multiplayer = false;
		userInfo = new User("");
		scorePlayers = new ArrayList<ScorePlayer>();
		potionExplotion = new PoitionScreen(this);
		chooseAIscreen = new ChooseAIscreen(this);
		helpMenu = new HelpMenu(this);
		settingsMenu = new SettingsMenu(this);
		startMenuScreen = new StartMenuScreen(this);
		gameModMenu = new GameModMenu(this);
		pauseMenu = new PauseMenu(this);
		world = new World(0, 1, new Vector2(320, 240), className);
		gameLevel = new GameManagerScreen(world, this, new Vector2(240, 340));
		goToTheKing = new CastleScreen(this, 1);
		takePoison = new BlackHouseScreen(this);
		waitScreen = new WaitScreen(this);
		MusicPool.musicMenu.setLooping(true);
		if (SettingsMenu.isMusicEnable)
			MusicPool.musicMenu.play();
		setScreen(startMenuScreen);
	}

	/**
	 * change screen depending on parameter state
	 * 
	 * @param state
	 *            indicates application's state
	 */
	public void swap(int state) {
		ImagePool.camera.zoom = 1.0f;
		switch (state) {
		case 0:
			if (start) {
				if (!multiplayer)
					restartGame();
				else {
					if (server != null)
						server.shutdownServer();
					scorePlayers.clear();
				}
				multiplayer = false;
				myServerLunched = false;
				startGameNet = false;
				setScreen(startMenuScreen);
			} else {
				if (SettingsMenu.isMusicEnable)
					MusicPool.musicMenu.play();
				world.enemiesOne.stopThread = true;
				setScreen(pauseMenu);
			}
			break;
		case 1:
			setScreen(settingsMenu);
			break;
		case 2:
			setScreen(helpMenu);
			break;
		case 3:
			Gdx.input.setInputProcessor(gameModMenu.stage);
			setScreen(gameModMenu);
			break;
		case 4:
			try {
				scoreScreen = new ScoreMenu(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setScreen(scoreScreen);
			break;
		case 5:
			start = false;
			MusicPool.musicMenu.stop();
			if (free) {
				world.enemiesOne.start();
				setScreen(freeModGame);
			} else {
				world.enemiesOne.start();
				setScreen(gameLevel);
			}
			break;
		case 6:
			setScreen(chooseAIscreen);
			break;
		case 7:
			multiplayerMenu = new StartMultiplayerScreen(this);
			setScreen(multiplayerMenu);
			break;
		case 8:
			myServerLunched = true;
			settingMultiplayerScreen = new SettingMultiplayerScreen(this, true);
			setScreen(settingMultiplayerScreen);
			break;
		case 9:
			settingMultiplayerScreen = new SettingMultiplayerScreen(this, false);
			setScreen(settingMultiplayerScreen);
			break;
		case 10:
			if (myServerLunched) {
				if (server == null)
					System.out.println("server non creato");
				connection = new ServerThread(server);
				connection.start();
			}
			netGame = new NetGameScreen(serverAddress, port, this);
			multiplayer = true;
			setScreen(waitScreen);
			break;
		case 11:
			MusicPool.musicMenu.stop();
			netGame.matchTimer.start();
			setScreen(netGame);
			break;
		case 12:

			scoreNetScreen = new ScoreNetScreen(this);
			setScreen(scoreNetScreen);
			break;
		default:
			break;
		}
	}

	/**
	 * set level screen
	 * 
	 * @param level
	 *            indicates game level
	 */
	public void changeLevel(int level) {
		switch (level) {
		case 1:
			clearWorld();
			world = new World(0, 1, new Vector2(240, 340), className);
			gameLevel = new GameManagerScreen(world, this, world.player.getPosition());
			gameLevel.worldGame.enemiesOne.start();
			free = false;
			setScreen(gameLevel);
			break;
		case 2:
			clearWorld();
			Vector2 playerPosition = world.player.getPosition();
			int tmpScore = world.score;
			world = new World(tmpScore, 2, playerPosition, className);
			gameLevel = new GameManagerScreen(world, this,
					new Vector2(gameLevel.gameCam.position.x, gameLevel.gameCam.position.y));
			gameLevel.worldGame.enemiesOne.start();
			free = false;
			setScreen(gameLevel);
			break;
		case 3:
			clearWorld();
			Vector2 position = world.player.getPosition();
			int oldScore = world.score;
			world = new World(oldScore, 3, position, className);
			gameLevel = new GameManagerScreen(world, this,
					new Vector2(gameLevel.gameCam.position.x, gameLevel.gameCam.position.y));
			gameLevel.worldGame.enemiesOne.start();
			free = false;
			setScreen(gameLevel);
			break;
		case 4:
			clearWorld();
			if (!loadGame) {
				free = true;
				world = new World(0, 4, new Vector2(320, 240), className);
				freeModGame = new FreeGameScreen(this, world);
			} else
				loadGame();
			MusicPool.musicMenu.stop();
			freeModGame.worldGame.enemiesOne.start();
			setScreen(freeModGame);
			break;
		default:
			return;

		}
	}

	public void clearWorld() {
		world.clear();

	}

	/**
	 * configure new level
	 */
	public void levelUp() {

		world.enemiesOne.stopThread = true;
		if (SettingsMenu.isMusicEnable)
			MusicPool.musicMenu.play();
		loadGame = false;
		if (!free) {
			if (currentLevel == 1) {
				goToTheKing.level = 1;
				setScreen(goToTheKing);
			} else if (currentLevel == 2) {
				takePoison.level = currentLevel + 1;
				setScreen(takePoison);
			} else {
				goToTheKing.collided = false;
				goToTheKing.level = 3;
				setScreen(goToTheKing);
			}
			currentLevel++;
		}
	}

	/**
	 * save world's state in file
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {

		String path;
		if (free)
			path = "src/Free/" + userInfo.userName + ".txt";
		else
			path = "src/Story/" + userInfo.userName + ".txt";
		File fileMap = new File(path);
		if (fileMap.exists())
			fileMap.createNewFile();
		fileMap.setWritable(true);
		FileWriter tmp;
		try {
			tmp = new FileWriter(fileMap);
			BufferedWriter buffer = new BufferedWriter(tmp);
			PrintWriter printout = new PrintWriter(buffer);
			printout.println(className);
			int mapx = (int) world.gameMap.getPosition().x;
			int mapy = (int) world.gameMap.getPosition().y;
			ArrayList<StaticObject> objects = world.getListObject();
			for (StaticObject s : objects) {
				if (s.getType() != null)
					printout.println(
							codType(s.getType()) + ";" + (int) s.getPosition().x + ";" + (int) s.getPosition().y + ";");
			}
			printout.println("i;" + world.score + ";" + world.player.lifePoints + ";");
			printout.println("h;" + world.player.shotGunShots + ";" + world.player.machineGunShots + ";");
			printout.println("l;" + currentLevel + ";" + world.foundLetter + ";");
			printout.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private String codType(String type) {
		switch (type) {

		case "Mappa 1":
			return "1";
		case "Mappa 2":
			return "2";
		case "Mappa 3":
			return "0";
		case "Capanna":
			return "3";
		case "Castello":
			return "4";
		case "Casinò":
			return "5";
		case "Capanna Grande":
			return "6";
		case "Cespuglio":
			return "7";
		case "Albero":
			return "8";
		case "Mitra":
			return "9";
		case "Fucile":
			return "10";
		case "Guarigione":
			return "11";
		case "Nemico":
			return "12";
		case "Pozzo":
			return "24";
		case "A":
			return "13";
		case "E":
			return "14";
		case "G":
			return "15";
		case "H":
			return "16";
		case "I":
			return "17";
		case "L":
			return "18";
		case "N":
			return "19";
		case "O":
			return "20";
		case "P":
			return "21";
		case "S":
			return "22";
		case "V":
			return "23";
		case "Well":
			return "24";
		case "Player":
			return "25";
		default:
			return null;
		}
	}

	/**
	 * configure a new match
	 */
	public void restartGame() {
		clearWorld();
		currentLevel = 1;
		start = true;
		free = false;
		int tmpScore = world.score;
		world = new World(tmpScore, 1, new Vector2(GameConfig.SCREEN_WIDTH / 2, GameConfig.SCREEN_HEIGHT / 2),
				className);
		world.score = 0;
		world.player.lifePoints = ConstantField.PLAYER_LIFE_POINTS;
		gameLevel = new GameManagerScreen(world, this, world.player.getPosition());
	}

	/**
	 * set the least screen of history
	 */
	public void gameCompleted() {
		setScreen(potionExplotion);
	}

	/**
	 * Load match
	 */
	public void loadGame() {

		clearWorld();

		world = new World(0, 1, new Vector2(50, 50), className);
		resetMatch();
		if (!free)
			gameLevel = new GameManagerScreen(world, this, new Vector2(world.player.getPosition()));
		else
			freeModGame = new FreeGameScreen(this, world);
	}

	/**
	 * set appropriate intro screen depending on parameter level
	 * 
	 * @param level
	 */
	public void intro(int level) {
		introScreen = new IntroScreen(level, this);
		setScreen(introScreen);
	}

	private void resetMatch() {
		currentLevel = world.level;
	}

	public ArrayList<ScorePlayer> getScoreList() {
		return scorePlayers;
	}

}