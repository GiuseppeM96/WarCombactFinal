package game.screens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.net.ScorePlayer;
import game.pools.ImagePool;

public class ScoreNetScreen implements Screen,ControllerListener {
	GameMenu gameMenu;
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
	ArrayList<ScorePlayer> scorePlayers;

	public ScoreNetScreen(GameMenu gameMenu) {

		hasPressedEnter = false;
		this.gameMenu = gameMenu;
		this.playerName = "";
		this.playerScore = gameMenu.world.score;
		bestPlayer = new String[5];
		bestPlayerScore = new int[5];
		batch = new SpriteBatch();
		viewport = new ExtendViewport(500, 500, ImagePool.camera);

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

		controller = new Controllers();
		controller.addListener(this);
		boolean change=true;
		scorePlayers = gameMenu.getScoreList();
		while(change){
			change = false;
			for( int i = 0; i<scorePlayers.size()-1; i++){
				if(scorePlayers.get(i).score > scorePlayers.get(i+1).score ){
					ScorePlayer changement = scorePlayers.get(i);
					scorePlayers.set(i, scorePlayers.get(i+1)) ;
					scorePlayers.set(i+1, changement) ;
					change = true;
				}
				
			}
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || hasPressedEnter) {
			hasPressedEnter = false;
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
		
		/*
		ImagePool.font.setColor(Color.GREEN);
		ImagePool.font.draw(batch, " S C O R E :  " + playerName.toUpperCase() + "    " + playerScore, 230, 341);
		*/
		ImagePool.font.setColor(Color.RED);
		ImagePool.font.draw(batch, "T O P  P L A Y E R :", 230, 421);
		ImagePool.font.setColor(Color.WHITE);
		
		
		for (int i = 0; i < gameMenu.scorePlayers.size(); i++) {
			gameMenu.scorePlayers.get(i);
			ImagePool.font.draw(batch, " S C O R E :  " + scorePlayers.get(i).getName()+ "    " + scorePlayers.get(i).getScore(), 230, 241+(i*50));
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

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if(buttonCode == 0 && gameMenu.getScreen().getClass().getName().contains("ScoreMenu"))
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