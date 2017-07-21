package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.pools.GameConfig;
import game.pools.ImagePool;
import game.pools.MusicPool;

public class IntroScreen implements Screen {

	SpriteBatch batch;
	int write;
	int level;
	Texture backGround;
	Texture text;
	GameMenu gameMenu;
	float screenWidth;
	float screenHeight;

	/**
	 * Create a introduction screen depending on parameter level
	 * 
	 * @param level
	 *            indicates level that will be loaded
	 * @param gameMenu
	 *            indicates game application
	 */
	public IntroScreen(int level, GameMenu gameMenu) {
		this.gameMenu = gameMenu;
		write = 0;
		this.level = level;
		switch (level) {
		case 1:
			text = ImagePool.help;
			backGround = ImagePool.introLevelOne;
			break;
		case 2:
			text = ImagePool.poison;
			backGround = ImagePool.introLevelEven;
			break;
		case 3:
			text = ImagePool.saveVillage;
			backGround = ImagePool.introLevelOdd;
			break;
		case 4:
			text = ImagePool.killEnemies;
			backGround = ImagePool.introLevelEven;
			break;
		}
		batch = new SpriteBatch();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		drawDisplay();
		write++;
		if (write > 300){
			MusicPool.musicMenu.stop();
			if (level == 1)
				gameMenu.swap(5);
			else {
				gameMenu.changeLevel(level);

			}
		}
	}

	/**
	 * draws scene
	 */
	private void drawDisplay() {
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.draw(backGround, 0, 0, screenWidth, screenHeight);
		int x = (int) (ImagePool.camera.viewportWidth / 2) - text.getWidth() / 2;
		int y = (int) (ImagePool.camera.viewportHeight / 2) - text.getHeight() / 2;
		if (write % 50 >= 0 && write % 50 <= 40)
			batch.draw(text, x, y);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		screenWidth = width;
		screenHeight = height;
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

}
