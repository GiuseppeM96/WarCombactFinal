package game.screens;

import java.awt.RenderingHints.Key;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.LongAccumulator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
import game.pools.ImagePool;

public class StartMenuScreen implements Screen {
	private GameMenu gameMenu;
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

	public StartMenuScreen(GameMenu gameMenu) {
		this.gameMenu = gameMenu;
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
		
		exit= new TextButton("Exit",ImagePool.skin);
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

	private void initMatrix() {
		matrixPosition[0][0] = new Vector2(114, 80);
		matrixPosition[0][1] = new Vector2(249, 80);
		matrixPosition[0][2] = new Vector2(353, 78);
		matrixPosition[1][1] = new Vector2(179, -5);
		matrixPosition[1][2] = new Vector2(293, -7);
		matrixPosition[1][0] = new Vector2( 55,-5);
		
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

	private void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			Gdx.input.setInputProcessor(null);
			switch ((int) itemSelected.x) {
			case 0:
				switch ((int) itemSelected.y) {
				case 0:
					gameMenu.swap(6);
					break;
				case 1:
					gameMenu.loadGame=true;
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

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			joystickSprite.setTexture(ImagePool.joystickLeft);
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
				if (itemSelected.y > 0) {
					itemSelected.y--;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}

		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			joystickSprite.setTexture(ImagePool.joystickRight);
			if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
				if (itemSelected.y < 2 ) {
					itemSelected.y++;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			joystickSprite.setTexture(ImagePool.joystickUp);
			if (itemSelected.x > 0) {
				itemSelected.x--;
				selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
						matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
				selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
						matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			joystickSprite.setTexture(ImagePool.joystickDown);
			if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
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

}