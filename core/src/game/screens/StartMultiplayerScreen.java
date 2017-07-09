package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class StartMultiplayerScreen implements Screen{

	
	
	private GameMenu gameMenu;

	private TextButton createMatch;
	private TextButton existingMatch;
	private TextButton back;

	private Sprite macchinaSprite;
	private Sprite joystickSprite;
	private Sprite selectedSprite;
	private Sprite backGround;

	private Vector2[][] vectorPosition;
	private Vector2[][] vectorDimension;
	private Vector2 itemSelected;
	TextField name;
	
	Stage stage;
	OrthographicCamera camera;
	Viewport viewport;
	SpriteBatch batch;


	
	/*
	 * 
	 * da questa classe mettiamo New Match e Add Match
	 * appena premo AddMatch se il server di gameMenu!=null crea il NetGameScreen e va in WaitScreen
	 * NewMatch va in SettingsMultiplayerScreen
	 *  
	 * */
	public StartMultiplayerScreen(GameMenu gameMenu) {
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

		name= new TextField("", ImagePool.skin);
		name = new TextField("", ImagePool.skin);
		name.setMessageText("______");
		name.setFocusTraversal(true);
		name.setPosition(290, 250);
		
		createMatch= new TextButton("Create Match", ImagePool.skin);
		createMatch.setColor(Color.RED);
		createMatch.setPosition(100, 70);
		
		existingMatch= new TextButton("Existing Match",ImagePool.skin);
		existingMatch.setColor(Color.RED);
		existingMatch.setPosition(320, 70);
		
		back = new TextButton("Back", ImagePool.skin);
		back.setColor(Color.BLUE);
		back.setPosition(270, 10);

		mainTable.setLayoutEnabled(false);
		mainTable.add(back);
		mainTable.add(createMatch);
		mainTable.add(existingMatch);
		mainTable.add(name);

		vectorPosition= new Vector2[2][2];
		vectorDimension= new Vector2[2][2];
		initVector();
		
		stage.addActor(mainTable);
		Gdx.input.setInputProcessor(stage);
		joystickSprite.setPosition(500, -30);
		selectedSprite.setSize(vectorDimension[(int) itemSelected.x][(int) itemSelected.y].x, vectorDimension[(int) itemSelected.x][(int) itemSelected.y].y);
		selectedSprite.setPosition(vectorPosition[(int) itemSelected.x][(int) itemSelected.y].x, vectorPosition[(int) itemSelected.x][(int) itemSelected.y].y);
	}

	private void initVector() {
		vectorDimension[0][0] = new Vector2(createMatch.getWidth() + createMatch.getWidth() / 2,
				createMatch.getHeight() + createMatch.getHeight() / 2);
		vectorDimension[0][1] = new Vector2(existingMatch.getWidth() + existingMatch.getWidth() / 2,
				existingMatch.getHeight() + existingMatch.getHeight() / 2);
		vectorDimension[1][0] = new Vector2(back.getWidth() + back.getWidth() / 2,
				back.getHeight() + back.getHeight() / 2);

		vectorPosition[0][0] = new Vector2(52, 57);
		vectorPosition[0][1] = new Vector2(275, 57);
		vectorPosition[1][0] = new Vector2(252, 0);	
		itemSelected= new Vector2(0,0);
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

		ImagePool.font.setColor(Color.WHITE);
		ImagePool.font.draw(batch, "C H O O S E   Y O U R   M O D E", 230, 341);			
		ImagePool.font.draw(batch, "I N S E R T  N A M E",270,300);
	}

	private void update() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
			if(itemSelected.x == 1)
				gameMenu.swap(0);
			else{
				gameMenu.userInfo.setName(name.getText());
				switch ((int)itemSelected.y) {
				case 0:
					gameMenu.swap(8);
					break;
				case 1:
					gameMenu.swap(9);
					break;
				default:
					break;
				}
			}
		}
		boolean selectedIsMoved=false;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) 
			joystickSprite.setTexture(ImagePool.joystickLeft);
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) 
			joystickSprite.setTexture(ImagePool.joystickRight);
		else if (Gdx.input.isKeyPressed(Input.Keys.UP)) 
			joystickSprite.setTexture(ImagePool.joystickUp);
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) 
			joystickSprite.setTexture(ImagePool.joystickDown);
		else
			joystickSprite.setTexture(ImagePool.joystick);

	
		if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
			if(itemSelected.x==0 && itemSelected.y==0){
				itemSelected.y++;
				selectedIsMoved=true;
			}
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
			if(itemSelected.x==0 && itemSelected.y==1){
				itemSelected.y--;
				selectedIsMoved=true;

			}
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
			if(itemSelected.x==0){
				itemSelected.x=1;
				itemSelected.y=0;
				selectedIsMoved=true;

			}
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
			if(itemSelected.x==1){
				selectedIsMoved=true;
				itemSelected.x=0;

			}
		}
		
		if(selectedIsMoved){
			selectedSprite.setSize(vectorDimension[(int) itemSelected.x][(int) itemSelected.y].x, vectorDimension[(int) itemSelected.x][(int) itemSelected.y].y);
			selectedSprite.setPosition(vectorPosition[(int) itemSelected.x][(int) itemSelected.y].x, vectorPosition[(int) itemSelected.x][(int) itemSelected.y].y);
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
