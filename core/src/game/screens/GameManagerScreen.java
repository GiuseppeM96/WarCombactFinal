package game.screens;

import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import javax.swing.text.Position;
import org.ietf.jgss.GSSManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.manager.World;
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
import game.pools.ConstantField;
import game.pools.GameConfig;
import game.pools.ImagePool;
import game.pools.MusicPool;

public class GameManagerScreen implements Screen, ControllerListener {
	Music music;
	public World worldGame;
	SpriteBatch worldBatch;
	public OrthographicCamera gameCam;
	Viewport viewport;
	GameMenu gameMenu;
	float statePlayerTime = 0.f;
	BitmapFont score;
	BitmapFont lifePerCent;
	boolean intro;
	float shotAnimationTime = 0.f;
	float diedAnimationTime = 0.f;
	int introDuration = 200;
	Controllers controller;// = new Controllers();
	static boolean gameIsInPause = false;
	int currentAxis = 0;
	float valueMov = 0;
	PovDirection povDirection;
	boolean canDraw;
	boolean canRemove;

	/**
	 * create a screen game
	 * 
	 * @param world
	 *            indicates the world of this game screen
	 * @param game
	 *            indicates game application
	 * @param camPosition
	 *            indicates where camera will be located
	 */
	public GameManagerScreen(World world, GameMenu game, Vector2 camPosition) {
		super();
		canDraw = true;
		canRemove = false;
		intro = true;
		worldGame = world;
		gameMenu = game;
		worldBatch = new SpriteBatch();
		score = new BitmapFont();
		score.setColor(Color.WHITE);
		gameCam = new OrthographicCamera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
		gameCam.position.x = camPosition.x;
		gameCam.position.y = camPosition.y;
		viewport = new ScreenViewport(gameCam);
		GameConfig.controller.addListener(this);
		updateCam();
	}

	/**
	 * Handle input and update world
	 * 
	 * @param dt
	 *            time interval
	 */
	private void update(float dt) {
		if (!worldGame.player.died) {
			if (Gdx.input.isKeyPressed(Input.Keys.UP) || povDirection == PovDirection.north) {
				moveAndCheckCollision(0, dt);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || povDirection == PovDirection.east) {
				moveAndCheckCollision(1, dt);
			} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || povDirection == PovDirection.west) {
				moveAndCheckCollision(3, dt);
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || povDirection == PovDirection.south) {
				moveAndCheckCollision(2, dt);
			} else {
				statePlayerTime = 0;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || gameIsInPause) {
				ImagePool.camera.zoom = 1.0f;
				gameIsInPause = false;
				gameMenu.start = false;
				gameMenu.swap(0);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || worldGame.player.controllerHasShoted) {
				worldGame.player.shoting = true;
				// worldGame.player.ControllerHasShoted=false;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.X) || worldGame.player.controllerHasChangedWeapon) {
				if (SettingsMenu.isAudioEnable)
					MusicPool.reloadSound.play();
				World.player.changeWeapon();
				worldGame.player.controllerHasChangedWeapon = false;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.Z) || worldGame.player.controllerHasChangedVelocity) {
				World.player.changeSpeed(ConstantField.PLAYER_SUPER_VELOCITY);
			} else {
				World.player.changeSpeed(ConstantField.PLAYER_STD_VELOCITY);
			}
			if (World.player.shoting) {
				if (shotAnimationTime > 0.3 && !World.playerShot) {
					worldGame.playerHasShot();
					if (SettingsMenu.isAudioEnable)
						MusicPool.shotGunSound.play();
					World.playerShot = true;
				}
				if (shotAnimationTime > 0.6) {
					World.player.shoting = false;
					shotAnimationTime = 0.f;
					World.playerShot = false;
				}
			}
			if (World.player.shoting)
				shotAnimationTime += dt;
			if (!worldGame.playerIsAlive()) {
				worldGame.player.died = true;
				worldGame.enemiesOne.stopThread = true;
			}
			Tree.animationTime += dt;
			updateShots();
			updateCam();
		} else {

			diedAnimationTime += dt;
			if (diedAnimationTime > 0.9) {
				// se livello 1 2 3 restart level
				if (worldGame.level == 4) {
					// carica menu gameOver con il punteggio
				}
				gameMenu.start = true;
				worldGame.player.died = false;
				worldGame.player.lifePoints = 1000;
				gameMenu.swap(4);
			}
		}
	}

	/**
	 * Call the shot update function of World
	 */
	private void updateShots() {
		while (!canRemove) {
		}
		canDraw = false;
		worldGame.updateShots();
		canDraw = true;

	}

	/**
	 * Move currentPlayer and check if it collide with other objects
	 * 
	 * @param i
	 *            indicates direction code
	 * @param dt
	 *            time interval
	 */
	private void moveAndCheckCollision(int i, float dt) {
		StaticObject currentObject;
		if (i == 0)
			worldGame.movePlayerUp(dt);
		else if (i == 1)
			worldGame.movePlayerRight(dt);
		else if (i == 2)
			worldGame.movePlayerDown(dt);
		else if (i == 3)
			worldGame.movePlayerLeft(dt);
		currentObject = World.checkCollisionObject(worldGame.player);
		if (currentObject == null && (worldGame.player.position.x >= 10
				&& worldGame.player.position.x <= GameConfig.MAP_SIZE.x - 70 && worldGame.player.position.y >= 10
				&& worldGame.player.position.y <= GameConfig.MAP_SIZE.y - 10)) {
			statePlayerTime += dt;
			if (SettingsMenu.isAudioEnable)
				MusicPool.walkingSound.play();
		} else if (currentObject instanceof Letter) {
			if (SettingsMenu.isMusicEnable)
				MusicPool.pickLetter.play();
			worldGame.found++;
			worldGame.objects.remove(currentObject);
		}

		else if (currentObject instanceof AddLifePoints) {
			if (SettingsMenu.isAudioEnable)
				MusicPool.addLifePoints.play();
			worldGame.player.addLife();
			if (worldGame.player.lifePoints > 1000)
				worldGame.player.lifePoints = 1000;
			statePlayerTime += dt;
			worldGame.objects.remove(currentObject);
		} else if (currentObject instanceof AddMachineGunShots) {
			if (SettingsMenu.isAudioEnable)
				MusicPool.addShotSound.play();
			worldGame.player.addShots("MachineGun");
			statePlayerTime += dt;
			worldGame.objects.remove(currentObject);
		} else if (currentObject instanceof AddShotGunShots) {
			if (SettingsMenu.isAudioEnable)
				MusicPool.addShotSound.play();
			worldGame.player.addShots("ShotGun");
			statePlayerTime += dt;
			worldGame.objects.remove(currentObject);
		} else if (currentObject instanceof Enemy) {
			World.player.lifePoints -= 1;
		} else if (levelIsCompeted(currentObject)) {
			Gdx.input.setInputProcessor(null);
			gameMenu.levelUp();
		} else if (i == 0)
			worldGame.movePlayerDown(dt);
		else if (i == 1)
			worldGame.movePlayerLeft(dt);
		else if (i == 2)
			worldGame.movePlayerUp(dt);
		else if (i == 3)
			worldGame.movePlayerRight(dt);
		if (worldGame.found >= 0)// worldGame.mission.length())
			worldGame.levelCompleted = true;
	}

	/**
	 * check if game level is completed
	 * 
	 * @param currentObject
	 *            object that collide with palyer
	 * @return
	 */
	private boolean levelIsCompeted(StaticObject currentObject) {
		if (worldGame.levelCompleted && currentObject instanceof Castle
				&& (worldGame.level == 1 || worldGame.level == 3))
			return true;
		if (worldGame.levelCompleted && currentObject instanceof BlackHouse && worldGame.level == 2)
			return true;
		return false;
	}

	/**
	 * Set cam position at player position
	 */
	public void updateCam() {
		int xPlayer = (int) (worldGame.player.getPosition().x + 23);
		int yPlayer = (int) (worldGame.player.getPosition().y + 25);
		if (!(xPlayer - gameCam.viewportWidth / 2 < 0 || xPlayer + gameCam.viewportWidth / 2 > 2816))
			gameCam.position.x = xPlayer;
		if (!(yPlayer - gameCam.viewportHeight / 2 < 0 || yPlayer + gameCam.viewportHeight / 2 > 2212))
			gameCam.position.y = yPlayer;
		gameCam.update();
	}

	/**
	 * Draw all objects in the world
	 */
	private void drawWorld() {
		if (canDraw) {
			canRemove = false;
			ArrayList<StaticObject> objects;
			objects = worldGame.getListObject();
			Texture tmp = null;
			int cont = 0;
			for (StaticObject s : objects) {
				boolean trov = false;
				if (s instanceof Map)
					if (((Map) s).type == 1)
						tmp = ImagePool.mapOne;
					else if (((Map) s).type == 2)
						tmp = ImagePool.mapTwo;
					else
						tmp = ImagePool.mapThree;
				if (s instanceof AddLifePoints)
					tmp = ImagePool.addLife;
				else if (s instanceof AddShotGunShots)
					tmp = ImagePool.shotGun;
				else if (s instanceof AddMachineGunShots)
					tmp = ImagePool.machineGun;
				else if (s instanceof Bash)
					tmp = ImagePool.bash;
				else if (s instanceof BigHut)
					tmp = ImagePool.bigHut;
				else if (s instanceof BlackHouse)
					tmp = ImagePool.casino;
				else if (s instanceof Castle)
					tmp = ImagePool.castle;
				else if (s instanceof ShotPlayer) {
					if (((ShotPlayer) s).getTarget() >= 0)
						if (((ShotPlayer) s).getDirection().x == 0) {
							if (worldGame.level == 3)

								tmp = ImagePool.winterVerticalShot;
							else
								tmp = ImagePool.verticalShot;
						} else {
							if (worldGame.level == 3)
								tmp = ImagePool.winterShot;
							else
								tmp = ImagePool.shot;
						}
					else
						trov = true;
				} else if (s instanceof ShotEnemy) {
					if (((ShotEnemy) s).getTarget() >= 0)
						if (((ShotEnemy) s).getDirection().x == 0) {
							if (worldGame.level == 3)

								tmp = ImagePool.winterVerticalShot;
							else
								tmp = ImagePool.verticalShot;
						} else {
							if (worldGame.level == 3)
								tmp = ImagePool.winterShot;
							else
								tmp = ImagePool.shot;
						}
					else
						trov = true;
				} else if (s instanceof Letter) {
					tmp = getLetterImage((Letter) s);
				} else if (s instanceof Enemy) {
					try {
						switch ((int) World.classe.getMethod("getMoveDirection", Vector2.class).invoke(s,
								World.player.getPosition())) {// (((Enemy)
																// s).getMoveDirection(worldGame.player.getPosition()))
																// {
						case 0:
							if (((Enemy) s).shoting)
								worldBatch.draw(
										ImagePool.shotEnemyAnimationUp.getKeyFrame(((Enemy) s).shotAnimationTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							else
								worldBatch.draw(
										ImagePool.enemyAnimationUp.getKeyFrame(((Enemy) s).stateEnemyTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							break;
						case 1:
							if (((Enemy) s).shoting)
								worldBatch.draw(ImagePool.shotEnemyAnimationRight
										.getKeyFrame(((Enemy) s).shotAnimationTime, true), s.getPosition().x,
										(int) s.getPosition().y);
							else
								worldBatch.draw(
										ImagePool.enemyAnimationRight.getKeyFrame(((Enemy) s).stateEnemyTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							break;
						case 2:
							if (((Enemy) s).shoting)
								worldBatch.draw(ImagePool.shotEnemyAnimationDown
										.getKeyFrame(((Enemy) s).shotAnimationTime, true), s.getPosition().x,
										(int) s.getPosition().y);
							else
								worldBatch.draw(
										ImagePool.enemyAnimationDown.getKeyFrame(((Enemy) s).stateEnemyTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							break;
						case 3:
							if (((Enemy) s).shoting)
								worldBatch.draw(ImagePool.shotEnemyAnimationLeft
										.getKeyFrame(((Enemy) s).shotAnimationTime, true), s.getPosition().x,
										(int) s.getPosition().y);
							else
								worldBatch.draw(
										ImagePool.enemyAnimationLeft.getKeyFrame(((Enemy) s).stateEnemyTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							break;
						default:
							break;
						}
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					trov = true;
				} else if (s instanceof Hut)
					tmp = ImagePool.hut;
				else if (s instanceof Tree) {
					tmp = ImagePool.tree;
					worldBatch.draw(ImagePool.treeAnimation.getKeyFrame(((Tree) s).animationTime, true),
							s.getPosition().x, (int) s.getPosition().y);
					trov = true;
				} else if (s instanceof Character) {
					boolean shot = ((Character) s).shoting;
					if (!((Character) s).died)
						switch (((Character) s).getFrame()) {
						case 0:
							if (shot)
								worldBatch.draw(ImagePool.shotPlayerAnimationUp.getKeyFrame(shotAnimationTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							else
								worldBatch.draw(ImagePool.playerAnimationUp.getKeyFrame(statePlayerTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							break;
						case 1:
							if (shot)
								worldBatch.draw(ImagePool.shotPlayerAnimationRight.getKeyFrame(shotAnimationTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							else
								worldBatch.draw(ImagePool.playerAnimationRight.getKeyFrame(statePlayerTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							break;
						case 2:
							if (shot)
								worldBatch.draw(ImagePool.shotPlayerAnimationDown.getKeyFrame(shotAnimationTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							else
								worldBatch.draw(ImagePool.playerAnimationDown.getKeyFrame(statePlayerTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							break;
						case 3:
							if (shot)
								worldBatch.draw(ImagePool.shotPlayerAnimationLeft.getKeyFrame(shotAnimationTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							else
								worldBatch.draw(ImagePool.playerAnimationLeft.getKeyFrame(statePlayerTime, true),
										s.getPosition().x, (int) s.getPosition().y);
							break;
						default:
							break;
						}
					else {
						worldBatch.draw(ImagePool.playerAnimationDied.getKeyFrame(diedAnimationTime, true),
								s.getPosition().x, s.getPosition().y);

					}
					trov = true;
				}
				if (!trov) {
					if (tmp == ImagePool.mapOne)
						cont++;
					worldBatch.draw(tmp, (int) s.getPosition().x, (int) s.getPosition().y);

				}
			}
		}
		canRemove = true;
	}

	/**
	 * find Texture that stand for parameter s
	 * 
	 * @param s
	 * @return Texture depending value of s
	 */
	private Texture getLetterImage(Letter s) {
		switch (s.getValue()) {
		case 'a':
			return ImagePool.A;
		case 'e':
			return ImagePool.E;
		case 'g':
			return ImagePool.G;
		case 'h':
			return ImagePool.H;
		case 'i':
			return ImagePool.I;
		case 'l':
			return ImagePool.L;
		case 'n':
			return ImagePool.N;
		case 'o':
			return ImagePool.O;
		case 'p':
			return ImagePool.P;
		case 's':
			return ImagePool.S;
		case 'v':
			return ImagePool.V;
		}
		return null;
	}

	public void wait(int time) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() < start + time)
			;
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		worldBatch.setProjectionMatrix(gameCam.combined);
		worldBatch.begin();

		drawWorld();
		drawNavigationMap(worldGame.level);
		drawInfoBar();
		update(delta);

		worldBatch.end();

	}

	/**
	 * Draws the information about player life, number of munitions and score
	 */
	private void drawInfoBar() {
		float x = gameCam.position.x;
		float y = gameCam.position.y;
		Texture currentWeapon = getPlayerWeapon();
		worldBatch.draw(ImagePool.bar, x - viewport.getWorldWidth() / 2,
				y + 200 * viewport.getWorldHeight() / GameConfig.SCREEN_HEIGHT,
				ImagePool.bar.getWidth() * gameCam.viewportWidth / GameConfig.MAP_SIZE.x, ImagePool.bar.getHeight());
		worldBatch.draw(currentWeapon, x - 50 * viewport.getWorldWidth() / GameConfig.SCREEN_WIDTH,
				y + 200 * viewport.getWorldHeight() / GameConfig.SCREEN_HEIGHT);
		worldBatch.draw(ImagePool.navigationMap, x - viewport.getWorldWidth() / 2, y - viewport.getWorldHeight() / 2,
				ImagePool.navigationMap.getWidth() * gameCam.viewportWidth / GameConfig.SCREEN_WIDTH,
				ImagePool.navigationMap.getHeight() * gameCam.viewportHeight / GameConfig.SCREEN_HEIGHT);
		int numShots = World.player.getNumTotShot();
		int numShotsAvailable = World.player.getNumShot();
		score.draw(worldBatch, "PUNTEGGIO   " + worldGame.score,
				x + 150 * viewport.getWorldWidth() / GameConfig.SCREEN_WIDTH,
				y + 220 * viewport.getWorldHeight() / GameConfig.SCREEN_HEIGHT);
		score.draw(worldBatch, "       " + numShotsAvailable + "  /  " + numShots,
				x - 50 * viewport.getWorldWidth() / GameConfig.SCREEN_WIDTH + currentWeapon.getWidth(),
				y + 220 * viewport.getWorldHeight() / GameConfig.SCREEN_HEIGHT);
		if (World.player.lifePoints > 0) {
			worldBatch.draw(ImagePool.life100, x - 245 * viewport.getWorldWidth() / GameConfig.SCREEN_WIDTH,
					y + 200 * viewport.getWorldHeight() / GameConfig.SCREEN_HEIGHT,
					(190 * World.player.lifePoints) / 1000, 33);
		}

	}

	/**
	 * Draw small map that represents all game map
	 */
	private void drawNavigationMap(int level) {
		float x = gameCam.position.x;
		float y = gameCam.position.y;
		float reduceXPlayer = worldGame.player.getPosition().x;
		float reduceYPlayer = worldGame.player.getPosition().y;
		reduceXPlayer = ((reduceXPlayer * ImagePool.navigationMap.getWidth() * gameCam.viewportWidth
				/ GameConfig.SCREEN_WIDTH) / GameConfig.MAP_SIZE.x) + x - viewport.getWorldWidth() / 2;
		reduceYPlayer = ((reduceYPlayer * ImagePool.navigationMap.getHeight() * gameCam.viewportHeight
				/ GameConfig.SCREEN_HEIGHT) / GameConfig.MAP_SIZE.y) + y - viewport.getWorldHeight() / 2;
		worldBatch.draw(ImagePool.navigationPlayer, reduceXPlayer - ImagePool.navigationPlayer.getWidth() / 2,
				reduceYPlayer - ImagePool.navigationPlayer.getHeight() / 2,
				ImagePool.navigationPlayer.getWidth() * gameCam.viewportWidth / GameConfig.SCREEN_WIDTH,
				ImagePool.navigationPlayer.getHeight() * gameCam.viewportHeight / GameConfig.SCREEN_HEIGHT);
		for (Enemy e : worldGame.enemies) {
			float reduceXEnemy = e.getPosition().x;
			float reduceYEnemy = e.getPosition().y;
			reduceXEnemy = ((reduceXEnemy * ImagePool.navigationMap.getWidth() * gameCam.viewportWidth
					/ GameConfig.SCREEN_WIDTH) / GameConfig.MAP_SIZE.x) + x - viewport.getWorldWidth() / 2;
			reduceYEnemy = ((reduceYEnemy * ImagePool.navigationMap.getHeight() * gameCam.viewportHeight
					/ GameConfig.SCREEN_HEIGHT) / GameConfig.MAP_SIZE.y) + y - viewport.getWorldHeight() / 2;
			worldBatch.draw(ImagePool.navigationEnemy, reduceXEnemy - ImagePool.navigationEnemy.getWidth() / 2,
					reduceYEnemy - ImagePool.navigationEnemy.getHeight() / 2,
					ImagePool.navigationEnemy.getWidth() * gameCam.viewportWidth / GameConfig.SCREEN_WIDTH,
					ImagePool.navigationEnemy.getHeight() * gameCam.viewportHeight / GameConfig.SCREEN_HEIGHT);
		}

		float xFlag;
		float yFlag;
		if (level == 1 || level == 3) {
			xFlag = 2455;
			yFlag = 76;
		} else {
			xFlag = 125;
			yFlag = 1271;
		}
		xFlag = ((xFlag * ImagePool.navigationMap.getWidth() * gameCam.viewportWidth / GameConfig.SCREEN_WIDTH)
				/ GameConfig.MAP_SIZE.x) + x - viewport.getWorldWidth() / 2;
		yFlag = ((yFlag * ImagePool.navigationMap.getHeight() * gameCam.viewportHeight / GameConfig.SCREEN_HEIGHT)
				/ GameConfig.MAP_SIZE.y) + y - viewport.getWorldHeight() / 2;
		worldBatch.draw(ImagePool.flag, xFlag - ImagePool.flag.getWidth() / 2,
				yFlag - ImagePool.navigationEnemy.getHeight() / 2 + 3,
				ImagePool.flag.getWidth() * gameCam.viewportWidth / GameConfig.SCREEN_WIDTH,
				ImagePool.flag.getHeight() * gameCam.viewportHeight / GameConfig.SCREEN_HEIGHT);
	}

	/**
	 * 
	 * @return Texture that stand for players's current weapon
	 */
	Texture getPlayerWeapon() {
		if (World.player.weaponType.equals("ShotGun"))
			return ImagePool.shotGun;
		else
			return ImagePool.machineGun;
	}

	@Override
	public void resize(int width, int height) {
		if (width > gameCam.viewportWidth) {
			if (gameCam.position.x - width / 2 < 0)
				gameCam.position.x = width / 2;
			else if (gameCam.position.x + width / 2 > GameConfig.MAP_SIZE.x)
				gameCam.position.x = GameConfig.MAP_SIZE.x - width / 2;
		} else {
			if (worldGame.player.getPosition().x - width / 2 < 0)
				gameCam.position.x = width / 2;
			else if (worldGame.player.getPosition().x + width / 2 > GameConfig.MAP_SIZE.x)
				gameCam.position.x = GameConfig.MAP_SIZE.x - width / 2;

		}
		if (height > gameCam.viewportHeight) {

			if (gameCam.position.y - height / 2 < 0)
				gameCam.position.y = height / 2;

			else if (gameCam.position.y + height / 2 > GameConfig.MAP_SIZE.y)
				gameCam.position.y = GameConfig.MAP_SIZE.y - height / 2;
		} else {
			if (worldGame.player.getPosition().y - height / 2 < 0)
				gameCam.position.y = height / 2;
			else if (worldGame.player.getPosition().y + height / 2 > GameConfig.MAP_SIZE.y)
				gameCam.position.y = GameConfig.MAP_SIZE.y - height / 2;
		}
		viewport.update(width, height);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void connected(Controller controller) {

	}

	@Override
	public void disconnected(Controller controller) {
		// TODO Auto-generated method stub

	}

	/**
	 * handle the input that user generates with the controller
	 * 
	 * @param buttonCode
	 *            is the code of the button pressed
	 * @param controller
	 *            is the controller that generates the input
	 */
	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (gameMenu.getScreen().getClass().getName().contains("GameManagerScreen")) {
			if (buttonCode == 2)
				worldGame.player.controllerHasChangedWeapon = true;
			else if (buttonCode == 3)
				worldGame.player.controllerHasShoted = true;
			else if (buttonCode == 6)
				worldGame.player.controllerHasChangedVelocity = true;
			else if (buttonCode == 9)
				gameIsInPause = true;
		}
		return false;
	}

	/**
	 * handle the input that user generates with the controller
	 * 
	 * @param buttonCode
	 *            is the code of the button released
	 * @param controller
	 *            is the controller that generates the input
	 */
	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (gameMenu.getScreen().getClass().getName().contains("GameManagerScreen")) {
			if (buttonCode == 3)
				worldGame.player.controllerHasShoted = false;
			else if (buttonCode == 6)
				worldGame.player.controllerHasChangedVelocity = false;
		}
		return false;

	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	/**
	 * update the direction selected with the controller
	 * 
	 * @param controller
	 *            is the controller that generates the event
	 * @param value
	 *            is the direction selected
	 */
	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if(gameMenu.getScreen().getClass().getName().contains("GameManagerScreen"))
			povDirection = value;
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		// TODO Auto-generated method stub
		return false;
	}
}
