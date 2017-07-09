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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.pools.ImagePool;

public class ScoreMenu implements Screen {
	GameMenu gameMenu;
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

	public ScoreMenu(GameMenu gameMenu) throws IOException {

		this.gameMenu = gameMenu;
		this.playerName = "";
		this.playerScore = gameMenu.world.score;
		bestPlayer = new String[5];
		bestPlayerScore = new int[5];
		batch = new SpriteBatch();
		viewport = new ExtendViewport(500, 500, ImagePool.camera);

		File scoreFile;
		if (gameMenu.free)
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
	}

	public void updateTop5() {
		playerScore = gameMenu.world.score;
		playerName = gameMenu.userInfo.getName();
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

	private void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			Gdx.input.setInputProcessor(null);
			gameMenu.start=true;
			gameMenu.swap(0);
		}
	}

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
}