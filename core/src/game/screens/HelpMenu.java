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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.pools.ImagePool;

public class HelpMenu implements Screen {

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

	public HelpMenu(GameMenu gameMenu) {
		this.gameMenu = gameMenu;
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
			gameMenu.swap(0);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(currentPage==2)
				currentPage--;
			joystickSprite.setTexture(ImagePool.joystickLeft);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(currentPage==1)
				currentPage++;
			joystickSprite.setTexture(ImagePool.joystickRight);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.UP))
			joystickSprite.setTexture(ImagePool.joystickUp);
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			joystickSprite.setTexture(ImagePool.joystickDown);
		else
			joystickSprite.setTexture(ImagePool.joystick);
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

}

