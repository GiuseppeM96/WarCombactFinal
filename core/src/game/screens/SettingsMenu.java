package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.pools.GameConfig;
import game.pools.ImagePool;
import game.pools.MusicPool;

public class SettingsMenu implements Screen,ControllerListener {
	private GameMenu gameMenu;

	Controllers controller;
	private int itemSelected;
	private Vector2[] matrixDimension;
	private Vector2[] matrixPosition;

	private TextButton music;
	private TextButton audio;
	private TextButton back;

	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;

	Stage stage;
	Viewport viewport;

	SpriteBatch batch;

	private boolean hasPressedEnter;

	private int controllerMoveDirection;

	static public boolean isMusicEnable;
	static public boolean isAudioEnable;

	public SettingsMenu(GameMenu gameMenu) {
		this.gameMenu = gameMenu;
		//controller = new Controllers();
		GameConfig.controller.addListener(this);
		controllerMoveDirection = -1;
		hasPressedEnter = false;
		itemSelected = 2;
		matrixPosition = new Vector2[3];
		matrixDimension = new Vector2[3];
		isMusicEnable = true;
		isAudioEnable = true;
		batch = new SpriteBatch();
		viewport = new ExtendViewport(500, 500, ImagePool.camera);

		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		backGround=new Sprite(ImagePool.backGround);
		selectedSprite = new Sprite(ImagePool.selected);

		Table mainTable = new Table();
		stage = new Stage(viewport, batch);
		
		mainTable.setFillParent(true);
		mainTable.bottom();

		back = new TextButton("Back", ImagePool.skin);
		back.setColor(Color.BLUE);
		back.setPosition(390, 40);

		music = new TextButton("Music", ImagePool.skin);
		music.setColor(Color.RED);
		music.setPosition(260, 40);

		audio = new TextButton("Audio", ImagePool.skin);
		audio.setColor(Color.RED);
		audio.setPosition(130, 40);

		mainTable.setLayoutEnabled(false);
		mainTable.add(back);
		mainTable.add(audio);
		mainTable.add(music);

		stage.addActor(mainTable);

		Gdx.input.setInputProcessor(stage);
		joystickSprite.setPosition(500, -30);
		selectedSprite.setPosition(370, 27);
		selectedSprite.setSize(back.getWidth() + back.getWidth() / 2, back.getHeight() + back.getHeight() / 2);
		initMatrix();

	}
	/**
	 * init the matrix that contains the position where the itemSelected image could stay and the size of the itemSelected that depends by the key selected
	 */
	private void initMatrix() {
		matrixDimension[1] = new Vector2(music.getWidth() + music.getWidth() / 2,
				music.getHeight() + music.getHeight() / 2);
		matrixDimension[0] = new Vector2(audio.getWidth() + audio.getWidth() / 2,
				audio.getHeight() + audio.getHeight() / 2);
		matrixDimension[2] = new Vector2(back.getWidth() + back.getWidth() / 2,
				back.getHeight() + back.getHeight() / 2);

		matrixPosition[0] = new Vector2(110, 27);
		matrixPosition[1] = new Vector2(240, 27);
		matrixPosition[2] = new Vector2(370, 27);
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
	
	/** handle the input and evolve the scene
	 * 
	 */
	private void update() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || controllerMoveDirection == 3) {
			joystickSprite.setTexture(ImagePool.joystickLeft);
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || controllerMoveDirection == 3)
				if (itemSelected > 0) {
					itemSelected--;
					selectedSprite.setPosition(matrixPosition[itemSelected].x, matrixPosition[itemSelected].y);
					selectedSprite.setSize(matrixDimension[itemSelected].x, matrixDimension[itemSelected].y);
				}

		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controllerMoveDirection == 1) {
			joystickSprite.setTexture(ImagePool.joystickRight);
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || controllerMoveDirection == 1)
				if (itemSelected < 2) {
					itemSelected++;
					selectedSprite.setPosition(matrixPosition[itemSelected].x, matrixPosition[itemSelected].y);
					selectedSprite.setSize(matrixDimension[itemSelected].x, matrixDimension[itemSelected].y);
				}
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP) || controllerMoveDirection == 0)
			joystickSprite.setTexture(ImagePool.joystickUp);
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || controllerMoveDirection == 2)
			joystickSprite.setTexture(ImagePool.joystickDown);
		else
			joystickSprite.setTexture(ImagePool.joystick);
		
		controllerMoveDirection = -1;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || hasPressedEnter) {
			hasPressedEnter = false;
			switch (itemSelected) {
			case 0:
				isAudioEnable = !isAudioEnable;
				break;
			case 1:
				if(isMusicEnable)
					MusicPool.musicMenu.stop();
				else 
					MusicPool.musicMenu.play();
				isMusicEnable = !isMusicEnable;
				break;
			case 2:
				gameMenu.swap(0);
				break;
			default:
				break;
			}
		
		}

	}

	/**
	 * draw the scene
	 */
	private void draw() {
		backGround.draw(batch);
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);

		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);

		ImagePool.font.setColor(Color.WHITE);

		ImagePool.font.draw(batch, "A U D I O :", 230, 341);

		ImagePool.font.draw(batch, "M U S I C :", 230, 285);
		if (isAudioEnable)
			ImagePool.font.draw(batch, "Y E S", 370, 341);
		else
			ImagePool.font.draw(batch, "N O", 370, 341);
		if (isMusicEnable)
			ImagePool.font.draw(batch, "Y E S", 370, 285);
		else
			ImagePool.font.draw(batch, "N O", 370, 285);

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
	 * @param buttonCode is the code of the button pressed
	 * @param controller is the controller that generates the input
	 */
	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if(buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("SettingsMenu"))
			hasPressedEnter = true;
		return false;
	}
	/**
	 * handle the input that user generates with the controller
	 * @param buttonCode is the code of the button released
	 * @param controller is the controller that generates the input
	 */
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

		if (gameMenu.getScreen().getClass().getName().contains("SettingsMenu"))
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
		}		return false;
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

