package game.screens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.WarCombat;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class ScoreMenu implements Screen,ControllerListener {
	WarCombat game;
	Controllers controller;
	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;
	private TextButton back;

	Stage stage;
	SpriteBatch batch;
	Viewport viewport;

	String playerName;
	int playerScore;
	String[] bestPlayer;
	int[] bestPlayerScore;
	private boolean hasPressedEnter;

	public ScoreMenu(WarCombat game) throws IOException {

		hasPressedEnter = false;
		this.game = game;
		this.playerName = "";
		this.playerScore = game.world.score;
		bestPlayer = new String[5];
		bestPlayerScore = new int[5];
		batch = new SpriteBatch();
		viewport = new ExtendViewport(500, 500, ImagePool.camera);

		File scoreFile;
		if (game.free)
			scoreFile = new File("src/scoreFree.txt");
		else
			scoreFile = new File("src/score.txt");
		FileReader fileReader = new FileReader(scoreFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		selectedSprite = new Sprite(ImagePool.selected);
		backGround = new Sprite(ImagePool.backGround);

		Table mainTable = new Table();
		stage = new Stage(viewport, batch);

		mainTable.setFillParent(true);
		mainTable.bottom();

		back = new TextButton("Back", ImagePool.skin);
		back.setColor(Color.BLUE);
		back.setPosition(300, 40);

		mainTable.setLayoutEnabled(false);
		mainTable.add(back);

		stage.addActor(mainTable);

		Gdx.input.setInputProcessor(stage);
		joystickSprite.setPosition(500, -30);
		selectedSprite.setPosition(285, 27);
		selectedSprite.setSize(back.getWidth() + back.getWidth() / 2, back.getHeight() + back.getHeight() / 2);

		String line = bufferedReader.readLine();
		for (int i = 0; i < 5; i++) {
			int j = 0;
			char[] arrayLine = new char[line.length()];
			line.getChars(0, line.length(), arrayLine, 0);
			String name = "";
			int score = 0;
			while (arrayLine[j] != ';') {
				name += arrayLine[j];
				j++;
			}
			for (int k = j + 1; k < line.length(); k++) {
				score *= 10;
				score += (int) (arrayLine[k] - 48);
			}
			bestPlayer[i] = name;
			bestPlayerScore[i] = score;
			line = bufferedReader.readLine();

		}
		scoreFile.setWritable(true);
		updateTop5();

		FileWriter fileWriter = new FileWriter(scoreFile);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		PrintWriter printWriter = new PrintWriter(bufferedWriter);
		for (int i = 0; i < 5; i++) {
			printWriter.println(bestPlayer[i] + ";" + bestPlayerScore[i]);
		}
		printWriter.flush();
		GameConfig.controller.addListener(this);
	}

	/**
	 * update the high score
	 */
	public void updateTop5() {
		playerScore = game.world.score;
		playerName = game.userInfo.getName();
		if (playerScore < bestPlayerScore[4])
			return;
		bestPlayerScore[4] = playerScore;
		bestPlayer[4] = playerName;
		for (int j = 4; j >= 1; j--) {
			if (bestPlayerScore[j] > bestPlayerScore[j - 1]) {
				String swapString = bestPlayer[j];
				bestPlayer[j] = bestPlayer[j - 1];
				bestPlayer[j - 1] = swapString;
				int swap = bestPlayerScore[j];
				bestPlayerScore[j] = bestPlayerScore[j - 1];
				bestPlayerScore[j - 1] = swap;
			} else
				break;
		}
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
	/**
	 * handle the input and evolve the scene
	 */
	private void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || hasPressedEnter) {
			hasPressedEnter = false;
			Gdx.input.setInputProcessor(null);
			game.start=true;
			game.swap(0);
		}
	}
	/**
	 * draw the scene
	 */
	private void draw() {
		backGround.draw(batch);
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);

		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);

		ImagePool.font.setColor(Color.GREEN);
		ImagePool.font.draw(batch, " S C O R E :  " + playerName.toUpperCase() + "    " + playerScore, 230, 341);

		ImagePool.font.setColor(Color.RED);
		ImagePool.font.draw(batch, "T O P  5  P L A Y E R :", 230, 321);
		ImagePool.font.setColor(Color.WHITE);
		for (int i = 0; i < 5; i++) {
			if (bestPlayer[i] == playerName && bestPlayerScore[i] == playerScore) {
				ImagePool.font.setColor(Color.GREEN);
				ImagePool.font.draw(batch, i + 1 + " )  " + bestPlayer[i].toUpperCase() + "    " + bestPlayerScore[i],
						230, 301 - 20 * i);
				ImagePool.font.setColor(Color.WHITE);
			} else {
				ImagePool.font.draw(batch, i + 1 + " )  " + bestPlayer[i].toUpperCase() + "    " + bestPlayerScore[i],
						230, 301 - 20 * i);
			}
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
		if(buttonCode == 0 && game.getScreen().getClass().getName().contains("ScoreMenu"))
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