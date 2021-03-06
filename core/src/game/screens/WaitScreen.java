package game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.manager.WarCombat;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class WaitScreen implements Screen {

	float animationTime = 0.f;
	SpriteBatch batch;
	WarCombat game;

	public WaitScreen(WarCombat game) {
		batch = new SpriteBatch();
		this.game = game;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(ImagePool.connectingBackGround, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
		batch.draw(ImagePool.connectingAnimation.getKeyFrame(animationTime, true), 200, 100,
				600 * GameConfig.SCREEN_WIDTH / 1200, 300 * GameConfig.SCREEN_HEIGHT / 768);
		animationTime += delta;
		if (game.startGameNet)
			game.swap(11);
		batch.end();
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
		// TODO Auto-generated method stub

	}

}
