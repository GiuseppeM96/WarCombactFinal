package game.screens;

import java.awt.RenderingHints.Key;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.LongAccumulator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class StartMenuScreen implements Screen, ControllerListener {
	private GameMenu gameMenu;

	Controllers controller;

	private Vector2[][] matrixPosition;
	private Vector2[][] matrixDimension;
	private Vector2 itemSelected;

	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;

	int i = 0;
	private TextButton newGame;
	private TextButton loadGame;
	private TextButton multiplayer;
	private TextButton help;
	private TextButton settings;
	private TextButton exit;

	public Stage stage;
	String name;
	Viewport viewport = new ExtendViewport(500, 500, ImagePool.camera);
	SpriteBatch batch = new SpriteBatch();

	private boolean hasPressedEnter;

	private int controllerMoveDirection;

	public StartMenuScreen(GameMenu gameMenu) {
		this.gameMenu = gameMenu;
		// controller = new Controllers();
		GameConfig.controller.addListener(this);
		controllerMoveDirection = -1;
		hasPressedEnter = false;
		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		selectedSprite = new Sprite(ImagePool.selected);
		backGround = new Sprite(ImagePool.backGround);

		joystickSprite.setPosition(500, -30);

		Table mainTable = new Table();
		stage = new Stage(viewport, batch);

		mainTable.setFillParent(true);
		mainTable.bottom();

		newGame = new TextButton("New", ImagePool.skin);
		newGame.setColor(Color.RED);
		newGame.setPosition(130, 90);

		loadGame = new TextButton("Load", ImagePool.skin);
		loadGame.setColor(Color.RED);
		loadGame.setPosition(265, 90);

		multiplayer = new TextButton("Multiplayer", ImagePool.skin);
		multiplayer.setColor(Color.RED);
		multiplayer.setPosition(390, 90);

		help = new TextButton("Help", ImagePool.skin);
		help.setColor(Color.BLUE);
		help.setPosition(195, 5);

		settings = new TextButton("Settings", ImagePool.skin);
		settings.setColor(Color.BLUE);
		settings.setPosition(320, 5);

		exit = new TextButton("Exit", ImagePool.skin);
		exit.setColor(Color.DARK_GRAY);
		exit.setPosition(70, 5);

		mainTable.setLayoutEnabled(false);
		mainTable.add(newGame);
		mainTable.add(loadGame);
		mainTable.add(multiplayer);
		mainTable.add(help);
		mainTable.add(settings);
		mainTable.add(exit);
		stage.addActor(mainTable);
		Gdx.input.setInputProcessor(stage);
		matrixPosition = new Vector2[2][3];
		matrixDimension = new Vector2[2][3];
		initMatrix();
		itemSelected = new Vector2(0, 0);
		selectedSprite.setSize(matrixDimension[0][0].x, matrixDimension[0][0].y);
		selectedSprite.setPosition(matrixPosition[0][0].x, matrixPosition[0][0].y);

	}

	/**
	 * init the matrix that contains the position where the itemSelected image
	 * could stay and the size of the itemSelected that depends by the key
	 * selected
	 */
	private void initMatrix() {
		matrixPosition[0][0] = new Vector2(114, 80);
		matrixPosition[0][1] = new Vector2(249, 80);
		matrixPosition[0][2] = new Vector2(353, 78);
		matrixPosition[1][1] = new Vector2(179, -5);
		matrixPosition[1][2] = new Vector2(293, -7);
		matrixPosition[1][0] = new Vector2(55, -5);

		matrixDimension[0][0] = new Vector2(newGame.getWidth() + newGame.getWidth() / 2,
				newGame.getHeight() + newGame.getHeight() / 2);
		matrixDimension[0][1] = new Vector2(loadGame.getWidth() + loadGame.getWidth() / 2,
				loadGame.getHeight() + loadGame.getHeight() / 2);
		matrixDimension[0][2] = new Vector2(multiplayer.getWidth() + multiplayer.getWidth() / 2,
				multiplayer.getHeight() + multiplayer.getHeight() / 2);
		matrixDimension[1][1] = new Vector2(help.getWidth() + help.getWidth() / 2,
				help.getHeight() + help.getHeight() / 2);
		matrixDimension[1][2] = new Vector2(settings.getWidth() + settings.getWidth() / 2,
				settings.getHeight() + settings.getHeight() / 2);
		matrixDimension[1][0] = new Vector2(exit.getWidth() + exit.getWidth() / 2,
				exit.getHeight() + exit.getHeight() / 2);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		batch.begin();
		Gdx.gl20.glClearColor(.0f, .0f, .0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		draw();
		update();

		batch.end();
		stage.act();
		stage.draw();
	}

	private void draw() {
		backGround.draw(batch);
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);
		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);
		ImagePool.font.setColor(Color.WHITE);
		ImagePool.font.draw(batch, "PRESS TO START", 290, 250);
	}

	/**
	 * handle the input and evolve the scene
	 */
	private void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || hasPressedEnter) {
			Gdx.input.setInputProcessor(null);
			hasPressedEnter = false;
			switch ((int) itemSelected.x) {
			case 0:
				switch ((int) itemSelected.y) {
				case 0:
					gameMenu.swap(6);
					break;
				case 1:
					gameMenu.loadGame = true;
					gameMenu.swap(3);
					break;
				case 2:
					gameMenu.swap(7);
				default:
					break;
				}
				break;
			case 1:
				switch ((int) itemSelected.y) {
				case 0:
					Gdx.app.exit();
					break;
				case 1:
					gameMenu.swap(2);
					break;
				case 2:
					gameMenu.swap(1);
					break;
				default:
					break;
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || controllerMoveDirection == 3) {
			joystickSprite.setTexture(ImagePool.joystickLeft);
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || controllerMoveDirection == 3)
				if (itemSelected.y > 0) {
					itemSelected.y--;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}

		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controllerMoveDirection == 1) {
			joystickSprite.setTexture(ImagePool.joystickRight);
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || controllerMoveDirection == 1)
				if (itemSelected.y < 2) {
					itemSelected.y++;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP) || controllerMoveDirection == 0) {
			joystickSprite.setTexture(ImagePool.joystickUp);
			if (Gdx.input.isKeyPressed(Input.Keys.UP) || controllerMoveDirection == 0) {
				if (itemSelected.x > 0) {
					itemSelected.x--;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || controllerMoveDirection == 2) {
			joystickSprite.setTexture(ImagePool.joystickDown);
			if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || controllerMoveDirection == 2) {
				if (itemSelected.x < 1) {
					itemSelected.x++;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}

			}
		} else
			joystickSprite.setTexture(ImagePool.joystick);
		controllerMoveDirection = -1;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		stage.dispose();
	}

	@Override
	public void connected(Controller controller) {
		// TODO Auto-generated method stub

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
		if (buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("StartMenuScreen"))
			hasPressedEnter = true;
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
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
		boolean inputIsValid = false;

		if (gameMenu.getScreen().getClass().getName().contains("StartMenuScreen"))
			inputIsValid = true;

		if (inputIsValid) {
			if (value == PovDirection.north)
				controllerMoveDirection = 0;
			else if (value == PovDirection.east)
				controllerMoveDirection = 1;
			else if (value == PovDirection.south)
				controllerMoveDirection = 2;
			else if (value == PovDirection.west)
				controllerMoveDirection = 3;
		}
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