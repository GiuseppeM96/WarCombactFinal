package game.screens;

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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.net.MyServer;
import game.pools.ImagePool;

public class SettingMultiplayerScreen implements Screen, ControllerListener {

	private GameMenu gameMenu;

	Controllers controller;
	private TextButton go;
	private TextButton back;

	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;

	private Vector2[] vectorPosition;
	private Vector2[] vectorDimension;
	private int itemSelected;

	TextField input;
	TextField port;

	boolean creatingServer;

	Stage stage;
	OrthographicCamera camera;
	Viewport viewport;
	SpriteBatch batch;

	private boolean hasPressedEnter;

	private int controllerMoveDirection;

	public SettingMultiplayerScreen(GameMenu gameMenu, boolean creatingServer) {

		this.gameMenu = gameMenu;
		this.creatingServer = creatingServer;
		controller = new Controllers();
		controller.addListener(this);
		controllerMoveDirection = -1;
		hasPressedEnter = false;
		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		selectedSprite = new Sprite(ImagePool.selected);
		backGround = new Sprite(ImagePool.backGround);

		input = new TextField("", ImagePool.skin);
		input.setMessageText("_____");
		input.setFocusTraversal(true);
		input.setPosition(280, 280);
		
		port = new TextField("", ImagePool.skin);
		port.setMessageText("_____");
		port.setFocusTraversal(true);
		port.setPosition(280, 210);

		joystickSprite.setPosition(500, -30);
		batch = new SpriteBatch();
		viewport = new ExtendViewport(500, 500, ImagePool.camera);
		Table mainTable = new Table();
		stage = new Stage(viewport, batch);
		mainTable.setFillParent(true);
		mainTable.bottom();

		go = new TextButton("   GO   ", ImagePool.skin);
		go.setColor(Color.RED);
		go.setPosition(200, 70);

		back = new TextButton("  Back ", ImagePool.skin);
		back.setColor(Color.BLUE);
		back.setPosition(380, 70);

		mainTable.setLayoutEnabled(false);
		mainTable.add(go);
		mainTable.add(back);
		mainTable.add(input);
		mainTable.add(port);

		stage.addActor(mainTable);
		Gdx.input.setInputProcessor(stage);
		vectorPosition = new Vector2[3];
		vectorDimension = new Vector2[3];
		initVector();
		itemSelected = 1;
		selectedSprite.setSize(vectorDimension[itemSelected].x, vectorDimension[itemSelected].y);
		selectedSprite.setPosition(vectorPosition[itemSelected].x, vectorPosition[itemSelected].y);

		/*
		 * facciamo scegliere il numero di giocatori e un bottone create quando
		 * premo su create creo il NetGameScreen e parte un waitScreen che
		 * sparirà non appena si sono connessi i players
		 */
	}

	private void initVector() {
		vectorDimension[0] = new Vector2(go.getWidth() + go.getWidth() / 2, go.getHeight() + go.getHeight() / 2);
		vectorDimension[1] = new Vector2(back.getWidth() + back.getWidth() / 2,
				back.getHeight() + back.getHeight() / 2);

		vectorPosition[0] = new Vector2(176, 57);
		vectorPosition[1] = new Vector2(355, 57);
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

	private void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || hasPressedEnter) {
			hasPressedEnter = false;
			if (itemSelected == 1)
				gameMenu.swap(7);
			else if (itemSelected == 0) {
				if (creatingServer)
					gameMenu.server = new MyServer(convert(input.getText()),convert(port.getText()));
				else
					gameMenu.serverAddress = input.getText();
					gameMenu.port= convert(port.getText());
				gameMenu.swap(10);
			}
		}
		boolean selectedIsMoved = false;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || controllerMoveDirection == 3)
			joystickSprite.setTexture(ImagePool.joystickLeft);
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controllerMoveDirection == 1)
			joystickSprite.setTexture(ImagePool.joystickRight);
		else if (Gdx.input.isKeyPressed(Input.Keys.UP) || controllerMoveDirection == 0)
			joystickSprite.setTexture(ImagePool.joystickUp);
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || controllerMoveDirection == 2)
			joystickSprite.setTexture(ImagePool.joystickDown);
		else
			joystickSprite.setTexture(ImagePool.joystick);

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || controllerMoveDirection == 1) {
			if (itemSelected == 0) {
				itemSelected++;
				selectedIsMoved = true;
			}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || controllerMoveDirection == 3) {
			if (itemSelected == 1) {
				itemSelected--;
				selectedIsMoved = true;

			}
		}
		controllerMoveDirection = -1;
		if (selectedIsMoved) {
			selectedSprite.setSize(vectorDimension[itemSelected].x, vectorDimension[itemSelected].y);
			selectedSprite.setPosition(vectorPosition[itemSelected].x, vectorPosition[itemSelected].y);
		}
	}

	private void draw() {
		backGround.draw(batch);
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);

		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);

		ImagePool.font.setColor(Color.WHITE);
		if (creatingServer)
			ImagePool.font.draw(batch, "S E T  N U M B E R  P L A Y E R S", 240, 330);
		else
			ImagePool.font.draw(batch, "I N S E R T  I P", 270, 330);
		ImagePool.font.draw(batch, "I N S E R T  P O R T", 270, 260);
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

	private int convert(String cod) {
		char[] tmp = cod.toCharArray();
		int result = 0;
		for (int i = 0; i < tmp.length; i++) {
			result *= 10;
			result += tmp[i] - '0';
		}
		return result;
	}

	@Override
	public void connected(Controller controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnected(Controller controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("SettingMultiplayerScreen"))
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

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		boolean inputIsValid = false;
		if (gameMenu.getScreen().getClass().getName().contains("SettingMultiplayerScreen"))
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
