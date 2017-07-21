package game.screens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.script.ScriptContext;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.net.ScorePlayer;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class ScoreNetScreen implements Screen, ControllerListener {
	GameMenu gameMenu;
	Controllers controller;
	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite scroll;
	private Sprite backGround;
	private TextButton back;
	int selectedIndex;
	Stage stage;
	SpriteBatch batch;
	Viewport viewport;

	String playerName;
	int playerScore;
	String[] bestPlayer;
	int[] bestPlayerScore;
	private boolean hasPressedEnter;
	ArrayList<ScorePlayer> scorePlayers;
	private int controllerMoveDirection;

	public ScoreNetScreen(GameMenu gameMenu) {

		controllerMoveDirection = -1;
		hasPressedEnter = false;
		this.gameMenu = gameMenu;
		this.playerName = "";
		this.playerScore = gameMenu.world.score;
		selectedIndex = 0;
		bestPlayer = new String[5];
		bestPlayerScore = new int[5];
		batch = new SpriteBatch();
		viewport = new ExtendViewport(500, 500, ImagePool.camera);

		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		selectedSprite = new Sprite(ImagePool.selected);
		scroll = new Sprite(ImagePool.scroll);
		backGround = new Sprite(ImagePool.backGround);

		Table mainTable = new Table();
		stage = new Stage(viewport, batch);

		mainTable.setFillParent(true);
		mainTable.bottom();

		back = new TextButton("Back", ImagePool.skin);
		back.setColor(Color.BLUE);
		back.setPosition(300, 40);

		mainTable.setLayoutEnabled(false);
		mainTable.add(back);

		stage.addActor(mainTable);

		Gdx.input.setInputProcessor(stage);
		joystickSprite.setPosition(500, -30);
		selectedSprite.setPosition(285, 27);
		selectedSprite.setSize(back.getWidth() + back.getWidth() / 2, back.getHeight() + back.getHeight() / 2);

		GameConfig.controller.addListener(this);
		boolean change = true;
		scorePlayers = gameMenu.getScoreList();
		scroll.setPosition(450, 190);
		if (scorePlayers.size() > 4) {
			scroll.setSize(33, 156 - (scorePlayers.size() - 4) * 15);
		}
		while (change) {
			change = false;
			for (int i = 0; i < scorePlayers.size() - 1; i++) {
				if (scorePlayers.get(i).score < scorePlayers.get(i + 1).score) {
					ScorePlayer changement = scorePlayers.get(i);
					scorePlayers.set(i, scorePlayers.get(i + 1));
					scorePlayers.set(i + 1, changement);
					change = true;
				}

			}
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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

	/**
	 * handle the input and evolve the scene
	 */
	private void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || hasPressedEnter) {
			hasPressedEnter = false;
			Gdx.input.setInputProcessor(null);
			gameMenu.start = true;
			gameMenu.swap(0);
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || controllerMoveDirection == 2) {
			joystickSprite.setTexture(ImagePool.joystickDown);
			if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || controllerMoveDirection == 2) {
				if (selectedIndex < scorePlayers.size() - 4)
					selectedIndex++;
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP) || controllerMoveDirection == 0) {
			joystickSprite.setTexture(ImagePool.joystickUp);
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || controllerMoveDirection == 0) {
				if (selectedIndex > 0)
					selectedIndex--;
			}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || controllerMoveDirection == 3)
			joystickSprite.setTexture(ImagePool.joystickLeft);
		else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || controllerMoveDirection == 1)
			joystickSprite.setTexture(ImagePool.joystickRight);
		else
			joystickSprite.setTexture(ImagePool.joystick);

		controllerMoveDirection = -1;
	}

	/**
	 * draw the scene
	 */
	private void draw() {
		backGround.draw(batch);
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);
		if (scorePlayers.size() > 4)
			scroll.setPosition(scroll.getX(), 190 + (scorePlayers.size() - selectedIndex - 3) * 10);
		scroll.draw(batch);
		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);

		ImagePool.font.setColor(Color.RED);
		ImagePool.font.draw(batch, "S C O R E S :", 230, 331);
		ImagePool.font.setColor(Color.WHITE);

		for (int i = selectedIndex; i < selectedIndex + 4 && i < scorePlayers.size(); i++) {
			ImagePool.font.draw(batch,
					i + 1 + " )  " + scorePlayers.get(i).getName() + "    " + scorePlayers.get(i).getScore(), 240,
					300 - (i - selectedIndex) * 20);
		}
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
		if (buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("ScoreNetScreen"))
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

		if (gameMenu.getScreen().getClass().getName().contains("ScoreNetScreen"))
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