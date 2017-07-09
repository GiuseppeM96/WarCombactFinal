package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.pools.ImagePool;

public class ChooseAIscreen implements Screen {

	private GameMenu gameMenu;

	BitmapFont font;
	boolean choosed;
	private TextButton pcAI;
	private TextButton myAI;
	private TextButton back; // back lo hanno tutti

	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;

	private Vector2[] vectorPosition;// static ?
	private Vector2[] vectorDimension;// static ?
	private int itemSelected;

	public Stage stage;
	Viewport viewport = new ExtendViewport(500, 500, ImagePool.camera);
	SpriteBatch batch = new SpriteBatch();

	TextField name;

	public ChooseAIscreen(GameMenu gameMenu) {

		this.gameMenu = gameMenu;
		font = new BitmapFont();
		macchinaSprite = new Sprite(ImagePool.macchina);
		joystickSprite = new Sprite(ImagePool.joystick);
		selectedSprite = new Sprite(ImagePool.selected);
		backGround = new Sprite(ImagePool.backGround);
		name = new TextField("", ImagePool.skin);
		name.setMessageText("_____");
		name.setFocusTraversal(true);
		name.setVisible(false);
		joystickSprite.setPosition(500, -30);

		Table mainTable = new Table();
		stage = new Stage(viewport, batch);
		name.setPosition(250, 250);
		mainTable.setFillParent(true);
		mainTable.bottom();

		myAI = new TextButton("my AI", ImagePool.skin);
		myAI.setColor(Color.RED);
		myAI.setPosition(130, 90);

		pcAI = new TextButton("pc AI", ImagePool.skin);
		pcAI.setColor(Color.RED);
		pcAI.setPosition(380, 90);

		back = new TextButton("Back", ImagePool.skin);
		back.setColor(Color.BLUE);
		back.setPosition(260, 30);

		mainTable.setLayoutEnabled(false);
		mainTable.add(pcAI);
		mainTable.add(myAI);
		mainTable.add(back);
		mainTable.add(name);

		stage.addActor(mainTable);
		Gdx.input.setInputProcessor(stage);
		vectorPosition = new Vector2[3];
		vectorDimension = new Vector2[3];
		initVector();
		itemSelected = 1;
		selectedSprite.setSize(vectorDimension[itemSelected].x, vectorDimension[itemSelected].y);
		selectedSprite.setPosition(vectorPosition[itemSelected].x, vectorPosition[itemSelected].y);
		
		choosed=false;
	}

	private void initVector() {
		vectorDimension[0] = new Vector2(myAI.getWidth() + myAI.getWidth() / 2,
				myAI.getHeight() + myAI.getHeight() / 2);
		vectorDimension[2] = new Vector2(pcAI.getWidth() + pcAI.getWidth() / 2,
				pcAI.getHeight() + pcAI.getHeight() / 2);
		vectorDimension[1] = new Vector2(back.getWidth() + back.getWidth() / 2,
				back.getHeight() + back.getHeight() / 2);

		vectorPosition[0] = new Vector2(107, 75);
		vectorPosition[2] = new Vector2(365, 77);
		vectorPosition[1] = new Vector2(242, 16);
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
		boolean selectedIsMoved=false;
		if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !choosed)
			if(itemSelected<2){
				selectedIsMoved=true;
				itemSelected++;
			}
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !choosed)
			if(itemSelected>0){
				selectedIsMoved=true;
				itemSelected--;
			}
		if(selectedIsMoved){
			selectedSprite.setSize(vectorDimension[itemSelected].x, vectorDimension[itemSelected].y);
			selectedSprite.setPosition(vectorPosition[itemSelected].x, vectorPosition[itemSelected].y);

		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
			if(itemSelected==1)
				gameMenu.swap(0);
			else if(!choosed)
				choosed=true;
			else if(choosed){
				if(itemSelected==0){
					gameMenu.className="game.personalAI."+name.getText();
				}
				else{
					gameMenu.className="game.object.Enemy";
				}
				name.setText("");
				name.setMessageText("_____");
				choosed=false;
				gameMenu.swap(3);
			}
		}
		
	}

	private void draw() {
		backGround.draw(batch);
		macchinaSprite.draw(batch);
		joystickSprite.draw(batch);
		selectedSprite.draw(batch);
		if (selectedSprite.getX() != 0)
			selectedSprite.draw(batch);

		font.setColor(Color.WHITE);
		if(!choosed)
			font.draw(batch, "C H O O S E   Y O U R   A I", 230, 341);
		else {
			font.draw(batch,"P R E S S  E N T E R  T O  C O N T I N U E",208,215);
			if(itemSelected==0){
			font.draw(batch, "Y O U  C H O O S E  Y O U R  A I  :",230,341);
			font.draw(batch,"I N S E R T  A I  N A M E",230,321);
			name.setVisible(true);
			name.setFocusTraversal(true);
			Gdx.input.setInputProcessor(stage);
			}
			else if(itemSelected==2){
			font.draw(batch, "Y O U  C H O O S E  : ",230,341);
			font.draw(batch, " P C  A I",230,321);
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
