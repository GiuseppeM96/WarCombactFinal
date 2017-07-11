package game.screens;

import java.io.IOException;

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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.pools.ImagePool;

public class PauseMenu implements Screen,ControllerListener {

	private GameMenu gameMenu;

	Controllers controller;
	
	int checkPause = 0;
	private Vector2[][] matrixPosition;
	private Vector2[][] matrixDimension;
	private Vector2 itemSelected;

	private TextButton help;
	private TextButton settings;
	private TextButton resume;
	private TextButton backToMenu;
	private TextButton save;
	
	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;

	static BitmapFont font = new BitmapFont();

	public Stage stage;
	OrthographicCamera camera;
	Viewport viewport;

	SpriteBatch batch;

	private boolean hasPressedEnter;
	boolean matchSaved;

	private int controllerMoveDirection;

	public PauseMenu(GameMenu gameMenu) {
		this.gameMenu = gameMenu;
		
		matchSaved=false;
		
		controller = new Controllers();
		controller.addListener(this);
		controllerMoveDirection = -1;
		
		batch = new SpriteBatch();

		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		selectedSprite = new Sprite(ImagePool.selected);
		backGround= new Sprite(ImagePool.backGround);
		
		viewport = new ExtendViewport(500, 500, ImagePool.camera);

		Table mainTable = new Table();
		stage = new Stage(viewport, batch);

		mainTable.setFillParent(true);
		mainTable.bottom();

		backToMenu = new TextButton("Back To Menu", ImagePool.skin);
		backToMenu.setColor(Color.RED);
		backToMenu.setPosition(340, 80);

		help = new TextButton("Help", ImagePool.skin);
		help.setColor(Color.BLUE);
		help.setPosition(200, 10);// aggiungere

		settings = new TextButton("Settings", ImagePool.skin);
		settings.setColor(Color.BLUE);
		settings.setPosition(360, 10);// aggiungere pos

		resume = new TextButton("Resume", ImagePool.skin);
		resume.setColor(Color.RED);
		resume.setPosition(180, 80);// aggiungere pos

		save= new TextButton("Save",ImagePool.skin);
		save.setColor(Color.DARK_GRAY);
		save.setPosition(80, 10);
		
		mainTable.setLayoutEnabled(false);
		mainTable.add(backToMenu);
		mainTable.add(settings);
		mainTable.add(resume);
		mainTable.add(help);
		mainTable.add(save);
		
		stage.addActor(mainTable);
		Gdx.input.setInputProcessor(stage);
		matrixPosition = new Vector2[2][3];
		matrixDimension = new Vector2[2][3];
		initMatrix();
		itemSelected = new Vector2(0, 0);
		joystickSprite.setPosition(500, -30);
		selectedSprite.setPosition(matrixPosition[0][1].x, matrixPosition[0][1].y);
		selectedSprite.setSize(matrixDimension[0][1].x, matrixDimension[0][1].y);
	}

	private void initMatrix() {
		matrixDimension[0][1] = new Vector2(resume.getWidth() + resume.getWidth() / 2,
				resume.getHeight() + resume.getHeight() / 2);
		matrixDimension[0][2] = new Vector2(backToMenu.getWidth() + backToMenu.getWidth() / 2,
				backToMenu.getHeight() + backToMenu.getHeight() / 2);
		matrixDimension[1][1] = new Vector2(help.getWidth() + help.getWidth() / 2,
				help.getHeight() + help.getHeight() / 2);
		matrixDimension[1][2] = new Vector2(settings.getWidth() + settings.getWidth() / 2,
				settings.getHeight() + settings.getHeight() / 2);
		matrixDimension[1][0]= new Vector2(save.getWidth() + save.getWidth() / 2,
				save.getHeight() + save.getHeight() / 2);
		
		matrixPosition[0][1] = new Vector2(148, 67);
		matrixPosition[0][2] = new Vector2(290, 67);
		matrixPosition[1][1] = new Vector2(185, 0);
		matrixPosition[1][2] = new Vector2(330, -3);
		matrixPosition[1][0] = new Vector2(62,0);

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
			Gdx.input.setInputProcessor(null);
			hasPressedEnter=false;
			switch ((int) itemSelected.x) {
			case 0:
				switch ((int) itemSelected.y) {
				case 1:
					gameMenu.world.resumeEnemy();
					gameMenu.swap(5);
					matchSaved=false;
					break;
				case 2:
					gameMenu.start = true;
					gameMenu.loadGame=false;
					matchSaved=false;
					gameMenu.swap(0);
				default:
					break;
				}
				break;
			case 1:
				switch ((int) itemSelected.y) {
				case 1:
					gameMenu.swap(2);
					break;
				case 2:
					gameMenu.swap(1);
					break;
				case 0:
					try {
						matchSaved=true;
						gameMenu.save();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)  || controllerMoveDirection == 3) {
			joystickSprite.setTexture(ImagePool.joystickLeft);
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)  || controllerMoveDirection == 3)
				if (itemSelected.y > Math.abs((int)itemSelected.x-1)) {
					itemSelected.y--;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}

		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)  || controllerMoveDirection == 1) {
			joystickSprite.setTexture(ImagePool.joystickRight);
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)  || controllerMoveDirection == 1)
				if (itemSelected.y <= 1) {
					itemSelected.y++;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP)  || controllerMoveDirection == 0) {
			joystickSprite.setTexture(ImagePool.joystickUp);
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP)  || controllerMoveDirection == 0){
				if (itemSelected.x > 0) {
					 if(itemSelected.y==0)
						itemSelected.y++;
					itemSelected.x--;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)  || controllerMoveDirection == 2)  {
			joystickSprite.setTexture(ImagePool.joystickDown);
			if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)  || controllerMoveDirection == 2) {
				if (itemSelected.x < 1) { 
					 if(itemSelected.y==0)
						itemSelected.y++;
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

	private void draw() {
		backGround.draw(batch);
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);

		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);

		if (checkPause < 50)
			ImagePool.font.draw(batch, "P A U S E ! !", 300, 280);
		checkPause++;
		if (checkPause == 100)
			checkPause = 0;
		if(matchSaved)
			ImagePool.font.draw(batch, "M A T C H  S A V E D ! !", 270, 230);
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

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("PauseMenu"))
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

		if (gameMenu.getScreen().getClass().getName().contains("PauseMenu"))
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