package game.screens;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.pools.ImagePool;

public class GameModMenu implements Screen {

	private GameMenu gameMenu;

	BitmapFont font;

	int animation;
	boolean gameIsStarting;
	private TextButton storyMode;
	private TextButton freeMode;
	private TextButton back; // back lo hanno tutti

	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;

	private Vector2[][] matrixPosition;// static ?
	private Vector2[][] matrixDimension;// static ?
	private Vector2 itemSelected;

	public Stage stage;
	Viewport viewport = new ExtendViewport(500, 500, ImagePool.camera);
	SpriteBatch batch = new SpriteBatch();

	TextField name;
	
	public GameModMenu(GameMenu gameMenu) {

		this.gameMenu = gameMenu;
		gameIsStarting = false;
		animation = 0;
		font = new BitmapFont();
		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		selectedSprite = new Sprite(ImagePool.selected);
		backGround= new Sprite(ImagePool.backGround);
		name = new TextField("", ImagePool.skin);
		name.setMessageText("_____");
		name.setFocusTraversal(true);
		joystickSprite.setPosition(500, -30);

		Table mainTable = new Table();
		stage = new Stage(viewport, batch);
		mainTable.add(name);
		name.setPosition(230, 300);
		mainTable.setFillParent(true);
		mainTable.bottom();

		storyMode = new TextButton("Story Mode", ImagePool.skin);
		storyMode.setColor(Color.RED);
		storyMode.setPosition(130, 90);

		freeMode = new TextButton("Free Mode", ImagePool.skin);
		freeMode.setColor(Color.RED);
		freeMode.setPosition(380, 90);

		back = new TextButton("Back", ImagePool.skin);
		back.setColor(Color.BLUE);
		back.setPosition(300, 20);

		mainTable.setLayoutEnabled(false);
		mainTable.add(storyMode);
		mainTable.add(freeMode);
		mainTable.add(back);

		stage.addActor(mainTable);
		Gdx.input.setInputProcessor(stage);
		matrixPosition = new Vector2[2][2];
		matrixDimension = new Vector2[2][2];
		initMatrix();
		itemSelected = new Vector2(1, 0);
		selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
				matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
		selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
				matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);

	}

	private void initMatrix() {
		matrixDimension[0][0] = new Vector2(storyMode.getWidth() + storyMode.getWidth() / 2,
				storyMode.getHeight() + storyMode.getHeight() / 2);
		matrixDimension[0][1] = new Vector2(freeMode.getWidth() + freeMode.getWidth() / 2,
				freeMode.getHeight() + freeMode.getHeight() / 2);
		matrixDimension[1][0] = new Vector2(back.getWidth() + back.getWidth() / 2,
				back.getHeight() + back.getHeight() / 2);

		matrixPosition[0][0] = new Vector2(95, 77);
		matrixPosition[0][1] = new Vector2(345, 77);
		matrixPosition[1][0] = new Vector2(285, 5);
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

	private void draw() {
		backGround.draw(batch);		
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);
		selectedSprite.draw(batch);
		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);

		font.setColor(Color.WHITE);

		font.draw(batch, "I N S E R T  N A M E :", 230, 341);

	}

	private void update() {
		if (gameIsStarting) {

			if (animation >= 100 && animation < 200) {
				ImagePool.camera.zoom -= 0.06;
				ImagePool.camera.rotate(-1.9f);
			} else if (animation < 200) {
				ImagePool.camera.zoom += 0.06;
				ImagePool.camera.rotate(1.9f);

			} else {
				ImagePool.camera.zoom -= 0.004;
			}
			if(animation>=400){
				animation=-1;
				gameIsStarting=false;
				if(itemSelected.y==0){
					gameMenu.free=false;
					gameMenu.loadGame();
					gameMenu.intro(GameMenu.currentLevel);
				}
				else{
					gameMenu.free=true;
					gameMenu.intro(4);
				}
			
			}
			animation++;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			Gdx.input.setInputProcessor(null);
			switch ((int) itemSelected.x) {
			case 0:
				gameIsStarting = true;
				gameMenu.userInfo.setName(name.getText());
				break;
			case 1:
				GameMenu.loadGame=false;
				gameMenu.swap(0);
				break;
			default:
				break;
				/* continuare con matrice con il set screen dello swap */
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
				if (itemSelected.y < 1 && itemSelected.x == 0) {
					itemSelected.y++;
					selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
					selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
							matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
				}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			joystickSprite.setTexture(ImagePool.joystickUp);
			if (itemSelected.x == 1) {
				itemSelected.x = 0;
				selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
						matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
				selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
						matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
			}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			joystickSprite.setTexture(ImagePool.joystickDown);
			if (itemSelected.x == 0) {
				itemSelected.x++;
				itemSelected.y = 0;
				selectedSprite.setPosition(matrixPosition[(int) itemSelected.x][(int) itemSelected.y].x,
						matrixPosition[(int) itemSelected.x][(int) itemSelected.y].y);
				selectedSprite.setSize(matrixDimension[(int) itemSelected.x][(int) itemSelected.y].x,
						matrixDimension[(int) itemSelected.x][(int) itemSelected.y].y);
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
