package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import game.manager.GameMenu;
import game.object.Castle;
import game.object.Character;
import game.object.DynamicObject;
import game.object.Map;
import game.object.StaticObject;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class CastleScreen implements Screen, ControllerListener {
	boolean hasPressedEnter;
	Controllers controller ;
	StaticObject king;
	public boolean collided;
	SpriteBatch worldBatch;
	GameMenu gameMenu;
	Character player;
	float statePlayerTime;
	Map backGround;
	int dialogue;
	public int level;

	public CastleScreen(GameMenu game, int level) {
		super();
		hasPressedEnter = false;
		controller = new Controllers();
		controller.addListener(this);
		gameMenu = game;
		this.level = level;
		dialogue = 0;
		backGround = new Map(1);
		backGround.setPosition(new Vector2(0, 0));
		statePlayerTime = 0.f;
		king = new StaticObject();
		king.setPosition(new Vector2(350, 150));
		collided = false;
		player = new Character();
		player.setPosition(new Vector2(0, 150));
		worldBatch = new SpriteBatch();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		worldBatch.begin();

		drawWorld();
		update(delta);

		worldBatch.end();
	}

	private void update(float delta) {
		if (!collided) {
			if (player.collide(king)) {
				collided = true;
				statePlayerTime = 0.f;
			} else {
				player.move(1, delta);
				statePlayerTime += delta;
			}
		} else if (dialogue < 2) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || hasPressedEnter) {
				dialogue++;
				hasPressedEnter = false;
			}
		} else {
			if (player.getPosition().x < 5) {
				dialogue = 0;
				statePlayerTime = 0.f;
				collided = false;
				if (level == 1)
					gameMenu.intro(2);
				else {
					gameMenu.gameCompleted();

				}
			} else
				player.move(3, delta);
			statePlayerTime += delta;
		}

	}

	private void drawWorld() {
		if (!collided) {
			worldBatch.draw(ImagePool.insideCastle, 0, 0);
			worldBatch.draw(ImagePool.king, king.getPosition().x, king.getPosition().y);
			worldBatch.draw(ImagePool.playerAnimationRight.getKeyFrame(statePlayerTime, true), player.getPosition().x,
					player.getPosition().y);
		} else if (dialogue < 2) {
			worldBatch.draw(ImagePool.insideCastle, 0, 0);
			if (level == 1)
				if (dialogue == 0)
					worldBatch.draw(ImagePool.firstDialogue, 0, 0);
				else
					worldBatch.draw(ImagePool.secondDialogue, 0, 0);
			else if (dialogue == 0)
				worldBatch.draw(ImagePool.thirdDialogue, 0, 0);
			else
				worldBatch.draw(ImagePool.fourthDialogue, 0, 0);
			worldBatch.draw(ImagePool.king, king.getPosition().x, king.getPosition().y);
			worldBatch.draw(ImagePool.playerStopped, player.getPosition().x, player.getPosition().y);
		} else {
			worldBatch.draw(ImagePool.insideCastle, 0, 0);
			worldBatch.draw(ImagePool.king, king.getPosition().x, king.getPosition().y);
			worldBatch.draw(ImagePool.playerAnimationLeft.getKeyFrame(statePlayerTime, true), player.getPosition().x,
					player.getPosition().y);
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
		// TODO Auto-generated method stub

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
		if (buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("CastleScreen"))
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
		// TODO Auto-generated method stub
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
