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
import game.pools.ImagePool;

public class HelpMenu implements Screen,ControllerListener {

	Controllers controller;
	int currentPage;
	private GameMenu gameMenu;

	private TextButton newGame;

	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;

	Stage stage;
	OrthographicCamera camera;
	Viewport viewport;
	SpriteBatch batch;

	private TextButton back;
	private int controllerMoveDirection;
	private boolean hasPressedEnter;

	public HelpMenu(GameMenu gameMenu) {
		
		this.gameMenu = gameMenu;
		controller = new Controllers();
		controller.addListener(this);
		hasPressedEnter = false;
		controllerMoveDirection = -1;
		batch = new SpriteBatch();
		
		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		selectedSprite = new Sprite(ImagePool.selected);
		backGround = new Sprite(ImagePool.backGround);
		
		viewport = new ExtendViewport(500, 500, ImagePool.camera);

		Table mainTable = new Table();
		stage = new Stage(viewport, batch);

		mainTable.setFillParent(true);
		mainTable.bottom();

		back = new TextButton("Back", ImagePool.skin);
		back.setColor(Color.RED);
		back.setPosition(270, 40);

		mainTable.setLayoutEnabled(false);
		mainTable.add(back);
		stage.addActor(mainTable);
		Gdx.input.setInputProcessor(stage);
		joystickSprite.setPosition(500, -30);
		selectedSprite.setPosition(253, 27);
		selectedSprite.setSize(back.getWidth() + back.getWidth() / 2, back.getHeight() + back.getHeight() / 2);
		currentPage=1;
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || hasPressedEnter){
			gameMenu.swap(0);
			hasPressedEnter = false;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || controllerMoveDirection == 3){
			if(currentPage==2)
				currentPage--;
			joystickSprite.setTexture(ImagePool.joystickLeft);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controllerMoveDirection == 1){
			if(currentPage==1)
				currentPage++;
			joystickSprite.setTexture(ImagePool.joystickRight);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.UP) || controllerMoveDirection == 0)
			joystickSprite.setTexture(ImagePool.joystickUp);
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || controllerMoveDirection == 2)
			joystickSprite.setTexture(ImagePool.joystickDown);
		else
			joystickSprite.setTexture(ImagePool.joystick);
		controllerMoveDirection = -1;
	}

	private void draw() {
		backGround.draw(batch);
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);

		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);

		ImagePool.font.setColor(Color.WHITE);
		// font.draw(batch, "INFO", 290, 250);
		if(currentPage == 1){
			batch.draw(ImagePool.goUp, 230, 321);
			ImagePool.font.draw(batch, "G O    U P", 315, 341);
			batch.draw(ImagePool.goRight, 230, 295);
			ImagePool.font.draw(batch, "G O    R I G H T", 315, 315);
	
			batch.draw(ImagePool.goDown, 230, 270);
			ImagePool.font.draw(batch, "G O    D O W N", 315, 289);
	
			batch.draw(ImagePool.goLeft, 230, 245);
			ImagePool.font.draw(batch, "G O    L E F T", 315, 260);
	
			batch.draw(ImagePool.zed, 230, 221);
			ImagePool.font.draw(batch, "R U N", 315, 237);
	
			batch.draw(ImagePool.space, 214, 198);
			ImagePool.font.draw(batch, "S H O T", 315, 212);
			
			batch.draw(ImagePool.goRight, 460, 200);

		}
		else{
			batch.draw(ImagePool.x,230,321);
			ImagePool.font.draw(batch,"C H A N G E  W E A P O N",285,341);
			
			batch.draw(ImagePool.esc,230,291);
			ImagePool.font.draw(batch,"P A U S E ",285,311);

		}
			
	}

	@Override
	public void resize(int width, int height) {

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

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if( buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("HelpMenu"))
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

		if (gameMenu.getScreen().getClass().getName().contains("HelpMenu"))
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

