package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import game.manager.GameMenu;
import game.object.Character;
import game.object.Map;
import game.object.StaticObject;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class BlackHouseScreen implements Screen, ControllerListener {
	Controllers controller;
	StaticObject magician;
	boolean collided;
	SpriteBatch worldBatch;
	GameMenu gameMenu;
	Character player;
	float statePlayerTime;
	int dialogue;
	public int level;
	boolean hasPressedEnter = false;

	/**
	 * Create a screen where player go to the magician to take wake-up position
	 * 
	 * @param game
	 */
	public BlackHouseScreen(GameMenu game) {
		super();
		// controller = new Controllers();
		GameConfig.controller.addListener(this);
		gameMenu = game;
		this.level = level;
		dialogue = 0;
		statePlayerTime = 0.f;
		magician = new StaticObject();
		magician.setPosition(new Vector2(300, 30));
		collided = false;
		player = new Character();
		player.setPosition(new Vector2(0, 30));
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

	/**
	 * Makes the scene evolve
	 * 
	 * @param delta
	 *            time interval
	 */
	private void update(float delta) {
		if (!collided) {
			if (player.collide(magician)) {
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
				gameMenu.intro(3);
			} else
				player.move(3, delta);
			statePlayerTime += delta;
		}

	}

	/**
	 * Draw the scene
	 */
	private void drawWorld() {
		worldBatch.draw(ImagePool.insideBlackHouse, 0, 0);
		worldBatch.draw(ImagePool.magician, magician.getPosition().x, magician.getPosition().y);
		if (!collided) {
			worldBatch.draw(ImagePool.playerAnimationRight.getKeyFrame(statePlayerTime, true), player.getPosition().x,
					player.getPosition().y);
		} else if (dialogue < 2) {
			if (dialogue == 0)
				worldBatch.draw(ImagePool.firstDialogueBlack, 0, 300);
			else{
				worldBatch.draw(ImagePool.secondDialogueBlack, 0, 300);
				worldBatch.draw(ImagePool.specialPotion, player.position.x+35, 130);
			}
			worldBatch.draw(ImagePool.playerStopped, player.getPosition().x, player.getPosition().y);
		} else {
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

	/**
	 * handle the input that user generates with the controller
	 * 
	 * @param buttonCode
	 *            is the code of the button pressed
	 * @param controller
	 *            is the controller that generates the input
	 */
	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {

		if (buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("BlackHouseScreen"))
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
