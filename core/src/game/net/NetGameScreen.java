package game.net;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.manager.GameMenu;
import game.manager.World;
import game.object.AddLifePoints;
import game.object.AddMachineGunShots;
import game.object.AddShotGunShots;
import game.object.Bash;
import game.object.BigHut;
import game.object.BlackHouse;
import game.object.Castle;
import game.object.Enemy;
import game.object.Hut;
import game.object.Letter;
import game.object.Map;
import game.object.Shot;
import game.object.StaticObject;
import game.object.Tree;
import game.object.Well;
import game.pools.ConstantField;
import game.pools.GameConfig;
import game.pools.ImagePool;
import game.pools.MusicPool;
import game.screens.SettingsMenu;

public class NetGameScreen implements Screen,ActionListener,ControllerListener{

	Controllers controller;
	SpriteBatch batch;
	NetWorld worldGame;
	Socket s;
	String server_ip;
	int port;
	PrintWriter out;
	ClientReciverMessage listen;
	boolean wait;
	public boolean canRemove;
	public boolean canDraw;
	public OrthographicCamera gameCam;
	Viewport viewport;
	float shotAnimationTime;
	private BitmapFont score;
	GameMenu gameMenu;
	Timer matchTimer;
	private PovDirection povDirection;
	private boolean gameIsInPause;
	
	
	/**
	 * Constructor 
	 * @param ip indicates the server ip where you try to connect
	 * @param gameMenu indicates the game application
	 */
	public NetGameScreen(String ip,GameMenu gameMenu) {
		controller = new Controllers();
		controller.addListener(this);
		server_ip=ip;
		port=12345;
		ClientReciverMessage listen;
		wait=true;
		canRemove=false;
		canDraw=true;
		shotAnimationTime=0.f;
		matchTimer=new Timer(120000, this);
		this.gameMenu=gameMenu;
		try {
			s=new Socket(server_ip, port);
			out=new PrintWriter(s.getOutputStream());
			score=new BitmapFont();
			worldGame=new NetWorld(-1);
			gameCam = new OrthographicCamera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
			gameCam.position.x = GameConfig.SCREEN_WIDTH/2;
			gameCam.position.y = GameConfig.SCREEN_HEIGHT/2;
			viewport = new ScreenViewport(gameCam);
			updateCam();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listen=new ClientReciverMessage(s,this);
		listen.start();
		batch=new SpriteBatch();
	}
	
	/**
	 * Set cam position at player position 
	 */
	private void updateCam() {
		int xPlayer = (int) (worldGame.currentPlayer.getPosition().x + 23);
		int yPlayer = (int) (worldGame.currentPlayer.getPosition().y + 25);
		if (!(xPlayer - gameCam.viewportWidth / 2 < 0 || xPlayer + gameCam.viewportWidth / 2 > 2816))
			gameCam.position.x = xPlayer;
		if (!(yPlayer - gameCam.viewportHeight / 2 < 0 || yPlayer + gameCam.viewportHeight / 2 > 2212))
			gameCam.position.y = yPlayer;
		gameCam.update();		
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		batch.begin();
		batch.setProjectionMatrix(gameCam.combined);
		update(delta);
		if(wait){	
			batch.draw(ImagePool.introLevelEven, 0, 0,GameConfig.SCREEN_WIDTH,GameConfig.SCREEN_WIDTH);
		}
		else{
			batch.draw(ImagePool.mapTwo, 0, 0);
			drawWorld();
			drawInfoBar();
			drawNavigationMap();
			updateShots();
			updateCam();
		}
		batch.end();
	}

	/**
	 * Draw small map that represents all game map 
	 */
	private void drawNavigationMap() {
		float x = gameCam.position.x;
		float y = gameCam.position.y;
		float reduceXPlayer = worldGame.currentPlayer.getPosition().x;
		float reduceYPlayer = worldGame.currentPlayer.getPosition().y;
		reduceXPlayer = ((reduceXPlayer * ImagePool.navigationMap.getWidth()*gameCam.viewportWidth/GameConfig.SCREEN_WIDTH) / GameConfig.MAP_SIZE.x) + x - viewport.getWorldWidth()/ 2;
		reduceYPlayer = ((reduceYPlayer * ImagePool.navigationMap.getHeight()*gameCam.viewportHeight/GameConfig.SCREEN_HEIGHT) / GameConfig.MAP_SIZE.y) + y - viewport.getWorldHeight()/ 2;
		batch.draw(ImagePool.navigationPlayer, reduceXPlayer - ImagePool.navigationPlayer.getWidth() / 2,
				reduceYPlayer - ImagePool.navigationPlayer.getHeight() / 2,ImagePool.navigationPlayer.getWidth()*gameCam.viewportWidth/GameConfig.SCREEN_WIDTH,ImagePool.navigationPlayer.getHeight()*gameCam.viewportHeight/GameConfig.SCREEN_HEIGHT);
		for (NetCharacter e : worldGame.otherPlayers) {
			float reduceXEnemy = e.getPosition().x;
			float reduceYEnemy = e.getPosition().y;
			reduceXEnemy = ((reduceXEnemy * ImagePool.navigationMap.getWidth()*gameCam.viewportWidth/GameConfig.SCREEN_WIDTH) / GameConfig.MAP_SIZE.x) + x - viewport.getWorldWidth()/ 2;
			reduceYEnemy = ((reduceYEnemy * ImagePool.navigationMap.getHeight()*gameCam.viewportHeight/GameConfig.SCREEN_HEIGHT) / GameConfig.MAP_SIZE.y) + y - viewport.getWorldHeight()/ 2;
			batch.draw(ImagePool.navigationEnemy, reduceXEnemy - ImagePool.navigationEnemy.getWidth() / 2,
					reduceYEnemy - ImagePool.navigationEnemy.getHeight() / 2,ImagePool.navigationEnemy.getWidth()*gameCam.viewportWidth/GameConfig.SCREEN_WIDTH,ImagePool.navigationEnemy.getHeight()*gameCam.viewportHeight/GameConfig.SCREEN_HEIGHT);
		}		
	}
	
	/**
	 * Draws the information about player life, number of munitions and score 
	 */
	private void drawInfoBar() {
		
		float x = gameCam.position.x;
		float y = gameCam.position.y;
		Texture currentWeapon = getPlayerWeapon();
		batch.draw(ImagePool.bar,x-viewport.getWorldWidth()/2,y+200*viewport.getWorldHeight()/GameConfig.SCREEN_HEIGHT,ImagePool.bar.getWidth()*gameCam.viewportWidth/GameConfig.MAP_SIZE.x,ImagePool.bar.getHeight());
		batch.draw(currentWeapon, x - 100*viewport.getWorldWidth()/GameConfig.SCREEN_WIDTH, y + 200*viewport.getWorldHeight()/GameConfig.SCREEN_HEIGHT);
		batch.draw(ImagePool.navigationMap,x- viewport.getWorldWidth()/2, y-viewport.getWorldHeight() / 2,ImagePool.navigationMap.getWidth()*gameCam.viewportWidth/GameConfig.SCREEN_WIDTH,ImagePool.navigationMap.getHeight()*gameCam.viewportHeight/GameConfig.SCREEN_HEIGHT);
		int numShots=worldGame.currentPlayer.getNumTotShot();
		int numShotsAvailable=worldGame.currentPlayer.getNumShot();
		score.draw(batch, "COLPI A SEGNO " + worldGame.score+"  MORTE  "+worldGame.diedTimes, x+ 70*viewport.getWorldWidth()/GameConfig.SCREEN_WIDTH, y + 220*viewport.getWorldHeight()/GameConfig.SCREEN_HEIGHT);
		score.draw(batch, "       "+numShotsAvailable+"  /  "+numShots, x - 100*viewport.getWorldWidth()/GameConfig.SCREEN_WIDTH+currentWeapon.getWidth(), y + 220*viewport.getWorldHeight()/GameConfig.SCREEN_HEIGHT);
		if (worldGame.currentPlayer.lifePoints > 0) {
			batch.draw(ImagePool.life100, x - 300*viewport.getWorldWidth()/GameConfig.SCREEN_WIDTH, y + 200*viewport.getWorldHeight()/GameConfig.SCREEN_HEIGHT, (190 * worldGame.currentPlayer.lifePoints) / 1000, 33);
		}
	}
	/**
	 * 
	 * @return Current Weapon Texture 
	 */
	private Texture getPlayerWeapon() {
		if (worldGame.currentPlayer.weaponType.equals("ShotGun"))
			return ImagePool.shotGun;
		else
			return ImagePool.machineGun;
	}
	
	/**
	 * Call the shot update function of NetWorld
	 */
	private void updateShots() {
		worldGame.updateShots();
	}
	
	/**
	 * Handle input and update world  
	 * @param dt time interval
	 */
	private void update(float dt) {
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || povDirection== PovDirection.north) {
			moveAndCheckCollision(0, dt);
			worldGame.currentPlayer.stateAnimationTime+=dt;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || povDirection== PovDirection.east) {
			moveAndCheckCollision(1, dt);
			worldGame.currentPlayer.stateAnimationTime+=dt;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || povDirection== PovDirection.west) {
			moveAndCheckCollision(3, dt);
			worldGame.currentPlayer.stateAnimationTime+=dt;
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || povDirection== PovDirection.south) {
			moveAndCheckCollision(2, dt);
			worldGame.currentPlayer.stateAnimationTime+=dt;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || worldGame.currentPlayer.controllerHasShoted ) {
			worldGame.currentPlayer.shoting = true;
			sendShots();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.X) || worldGame.currentPlayer.controllerHasChangedWeapon){
			if (SettingsMenu.isAudioEnable)
				MusicPool.reloadSound.play();
			worldGame.currentPlayer.changeWeapon();
			worldGame.currentPlayer.controllerHasChangedWeapon=false;
			out.println(3+";"+worldGame.currentPlayer.code+";"+0+";"+0+";"+0+";");
			out.flush();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Z) || worldGame.currentPlayer.controllerHasChangedVelocity) {
			worldGame.currentPlayer.changeSpeed(ConstantField.PLAYER_SUPER_VELOCITY);
		} else
			worldGame.currentPlayer.changeSpeed(ConstantField.PLAYER_STD_VELOCITY);
		updatePlayerAnimation(worldGame.currentPlayer,dt);
		for(NetCharacter c: worldGame.otherPlayers){
			updatePlayerAnimation(c, dt);
			
		}
		if (!worldGame.currentPlayerIsAlive()) {
			worldGame.currentPlayer.died = true;
			out.println(4+";"+worldGame.currentPlayer.code+";"+0+";"+0+";"+0+";");
			out.flush();
		}
	}
		/**
		 * update the state of parameter currentPlayer
		 * @param currentPlayer NetCharacter who we wants to evolve
		 * @param dt time interval
		 */
		private void updatePlayerAnimation(NetCharacter currentPlayer,float dt) {
		if (currentPlayer.shoting && !currentPlayer.died) {
			if (currentPlayer.shotAnimationTime > 0.3 && !currentPlayer.shoted) {
				worldGame.playerHasShot(currentPlayer);
				if (SettingsMenu.isAudioEnable)
					MusicPool.shotGunSound.play();
				currentPlayer.shoted= true;
				currentPlayer.shotAnimationTime += dt;
			}
			else if (currentPlayer.shotAnimationTime > 0.6) {
				currentPlayer.shoting = false;
				currentPlayer.shotAnimationTime = 0.f;
				currentPlayer.shoted = false;
			}
			else currentPlayer.shotAnimationTime += dt;
		}
		else if(currentPlayer.died){
			if (currentPlayer.diedAnimationTime > 0.9) {
				currentPlayer.reGenerate();
				if(currentPlayer.code==worldGame.currentPlayer.code){
					worldGame.diedTimes++;
					resetCam();
				}
				currentPlayer.setPosition(new Vector2(worldGame.spawnPoints.get(currentPlayer.code%worldGame.spawnPoints.size()).position));
			}
			else currentPlayer.diedAnimationTime+=dt;
		}
		
	}
	/**
	 * reset cam position at the position of currentPlayer's spawnPoint 
	 */
	private void resetCam() {
		Vector2 pos=new Vector2(worldGame.spawnPoints.get(worldGame.currentPlayer.code % worldGame.spawnPoints.size()).position);
		gameCam.position.x = pos.x;
		gameCam.position.y = pos.y;
		if(gameCam.position.x-viewport.getScreenWidth()/2 < 0)
			gameCam.position.x=viewport.getScreenWidth()/2;
		else if(gameCam.position.x+viewport.getScreenWidth()/2>GameConfig.MAP_SIZE.x)
			gameCam.position.x=GameConfig.MAP_SIZE.x-viewport.getScreenWidth()/2;
		if(gameCam.position.y-viewport.getScreenHeight() < 0)
			gameCam.position.y=viewport.getScreenHeight()/2;
		else if(gameCam.position.y+viewport.getScreenHeight()/2>GameConfig.MAP_SIZE.y)
			gameCam.position.y=GameConfig.MAP_SIZE.y-viewport.getScreenHeight()/2;
	}
	
	/**
	 * Communicate to the server that we have shot 
	 */
	private void sendShots() {
		out.println(2+";"+worldGame.currentPlayer.code+";"+worldGame.currentPlayer.getPosition().x+";"+worldGame.currentPlayer.getPosition().y+";"+0+";");
		out.flush();
	}
	
	/**
	 * Move currentPlayer and check if it collide with other objects
	 * @param dir indicates direction code
	 * @param dt time interval
	 */
	private void moveAndCheckCollision(int dir, float dt) {
		
		worldGame.currentPlayer.move(dir, dt);
		
		StaticObject collidedObject=worldGame.checkCollisionObject(worldGame.currentPlayer);
		if(collidedObject == null){ 
			worldGame.currentPlayer.stateAnimationTime+=dt;
			out.println(0+";"+worldGame.currentPlayer.code+";"+worldGame.currentPlayer.getVelocity()+";"+dt+";"+dir+";");
			out.flush();
			if(SettingsMenu.isAudioEnable)
				MusicPool.walkingSound.play();
		}
		else if(collidedObject instanceof AddLifePoints){
			if(SettingsMenu.isAudioEnable)
				MusicPool.addLifePoints.play();
			out.println(1+";"+0+";"+(int)collidedObject.getPosition().x+";"+(int)collidedObject.getPosition().y+";"+0+";");
			out.flush();
			worldGame.objects.remove(collidedObject);
		}
		else if(collidedObject instanceof AddShotGunShots){
			if(SettingsMenu.isAudioEnable)
				MusicPool.addShotSound.play();
			out.println(1+";"+1+";"+(int)collidedObject.getPosition().x+";"+(int)collidedObject.getPosition().y+";"+0+";");
			out.flush();
			worldGame.objects.remove(collidedObject);
		}
		else if(collidedObject instanceof AddMachineGunShots){
			if(SettingsMenu.isAudioEnable)
				MusicPool.addShotSound.play();
			out.println(1+";"+2+";"+(int)collidedObject.getPosition().x+";"+(int)collidedObject.getPosition().y+";"+0+";");
			out.flush();
			worldGame.objects.remove(collidedObject);
		}
		else if(collidedObject instanceof Shot){
			if(((Shot)collidedObject).codeOwner != worldGame.currentPlayer.code)
				worldGame.currentPlayer.lifePoints-=10;
			((Shot) collidedObject).visible=false;
		}
		else{
			worldGame.currentPlayer.move((dir+2)%4, dt);
			worldGame.currentPlayer.setDirection(dir);
		}
	}
	/**
	 * Draw all objects in the world 
	 */
	private void drawWorld() {
		canRemove=false;
		if(canDraw){
			ArrayList<StaticObject> objects;
			objects = worldGame.objects;
			Texture tmp = null;
			int cont=0;
			for (StaticObject s : objects) {
				boolean trov = false;
				if (s instanceof Map)
					if (((Map) s).type == 1)
						tmp = ImagePool.mapOne;
					else if(((Map)s).type==2)
						tmp = ImagePool.mapTwo;
					else tmp=ImagePool.mapThree;
				if (s instanceof AddLifePoints)
					tmp = ImagePool.addLife;
				else if (s instanceof AddShotGunShots)
					tmp = ImagePool.shotGun;
				else if (s instanceof AddMachineGunShots)
					tmp = ImagePool.machineGun;
				else if (s instanceof Bash)
					tmp = ImagePool.bash;
				else if (s instanceof BigHut)
					tmp = ImagePool.bigHut;
				else if (s instanceof BlackHouse)
					tmp = ImagePool.casino;
				else if (s instanceof Castle)
					tmp = ImagePool.castle;
				
				else if (s instanceof Letter) {
					tmp = getLetterImage((Letter) s);
				} else if (s instanceof Hut)
					tmp = ImagePool.hut;
				else if (s instanceof Tree) {
					tmp = ImagePool.tree;
					batch.draw(ImagePool.treeAnimation.getKeyFrame(((Tree) s).animationTime, true), s.getPosition().x,
							(int) s.getPosition().y);
					trov = true;
				} else if (s instanceof NetCharacter) {
					tmp=ImagePool.people;
				}
				if (!trov){
					batch.draw(tmp, (int) s.getPosition().x, (int) s.getPosition().y);
				}
			}
			for(Well w:worldGame.spawnPoints){
				batch.draw(ImagePool.well, w.getPosition().x, w.getPosition().y);
			}
			drawPlayer(worldGame.currentPlayer);
			for(NetCharacter c: worldGame.otherPlayers){
				drawPlayer(c);
			}
			for(Shot sh:worldGame.shots){
				if (sh.getTarget() >= 0)
					if (sh.getDirection().x == 0)
						batch.draw(ImagePool.verticalShot, sh.getPosition().x, sh.getPosition().y);
					else batch.draw(ImagePool.shot, sh.getPosition().x, sh.getPosition().y);
			}
		}
		canRemove=true;
	}
	/**
	 * Draw parameter currentPlayer depending currentPlayer state  
	 * @param player NetCharacter that we want draw
	 */
	private void drawPlayer(NetCharacter player) {
		if (!player.died)
			switch (player.getFrame()) {
			case 0:
				if (player.shoting)
					batch.draw(ImagePool.shotPlayerAnimationUp.getKeyFrame(player.shotAnimationTime, true),
							player.getPosition().x, (int) player.getPosition().y);
				else
					batch.draw(ImagePool.playerAnimationUp.getKeyFrame(player.stateAnimationTime, true),
							player.getPosition().x, (int) player.getPosition().y);
				break;
			case 1:
				if (player.shoting)
					batch.draw(ImagePool.shotPlayerAnimationRight.getKeyFrame(player.shotAnimationTime, true),
							player.getPosition().x, (int) player.getPosition().y);
				else
					batch.draw(ImagePool.playerAnimationRight.getKeyFrame(player.stateAnimationTime, true),
							player.getPosition().x, (int) player.getPosition().y);
				break;
			case 2:
				if (player.shoting)
					batch.draw(ImagePool.shotPlayerAnimationDown.getKeyFrame(player.shotAnimationTime, true),
							player.getPosition().x, (int) player.getPosition().y);
				else
					batch.draw(ImagePool.playerAnimationDown.getKeyFrame(player.stateAnimationTime, true),
							player.getPosition().x, (int) player.getPosition().y);
				break;
			case 3:
				if (player.shoting)
					batch.draw(ImagePool.shotPlayerAnimationLeft.getKeyFrame(player.shotAnimationTime, true),
							player.getPosition().x, (int) player.getPosition().y);
				else
					batch.draw(ImagePool.playerAnimationLeft.getKeyFrame(player.stateAnimationTime, true),
							player.getPosition().x, (int) player.getPosition().y);
				break;
			default:
				break;
			}
		else {
			batch.draw(ImagePool.playerAnimationDied.getKeyFrame(player.diedAnimationTime, true),
					player.getPosition().x, player.getPosition().y);
		}
		
	}
	
	/**
	 * find Texture of parameter s
	 * @param s 
	 * @return Texture depending value of s
	 */
	private Texture getLetterImage(Letter s) {
		switch (s.getValue()) {
		case 'a':
			return ImagePool.A;
		case 'e':
			return ImagePool.E;
		case 'g':
			return ImagePool.G;
		case 'h':
			return ImagePool.H;
		case 'i':
			return ImagePool.I;
		case 'l':
			return ImagePool.L;
		case 'n':
			return ImagePool.N;
		case 'o':
			return ImagePool.O;
		case 'p':
			return ImagePool.P;
		case 's':
			return ImagePool.S;
		case 'v':
			return ImagePool.V;
		}
		return null;
	}
	
	
	@Override
	public void resize(int width, int height) {
		if (width > gameCam.viewportWidth) {
			if (gameCam.position.x - width / 2 < 0)
				gameCam.position.x = width / 2;
			else if (gameCam.position.x + width / 2 > GameConfig.MAP_SIZE.x)
				gameCam.position.x = GameConfig.MAP_SIZE.x - width / 2;
		}
		else
		{
			if (worldGame.currentPlayer.getPosition().x - width / 2 < 0)
				gameCam.position.x = width / 2;
			else if (worldGame.currentPlayer.getPosition().x + width / 2 > GameConfig.MAP_SIZE.x)
				gameCam.position.x = GameConfig.MAP_SIZE.x - width / 2;
			
		}
		if (height > gameCam.viewportHeight) {

			if (gameCam.position.y - height / 2 < 0)
				gameCam.position.y = height / 2;

			else if (gameCam.position.y + height / 2 > GameConfig.MAP_SIZE.y)
				gameCam.position.y = GameConfig.MAP_SIZE.y - height / 2;
		}
		else
		{
			if (worldGame.currentPlayer.getPosition().y - height / 2 < 0)
				gameCam.position.y = height / 2;
			else if (worldGame.currentPlayer.getPosition().y + height / 2 > GameConfig.MAP_SIZE.y)
				gameCam.position.y = GameConfig.MAP_SIZE.y - height / 2;
		}
		viewport.update(width, height);

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
		
	}
	public void removeObjects(StaticObject sb) {
			while(!canRemove){}
			canDraw=false;
			worldGame.objects.remove(sb);
			canDraw=true;
	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		out.println(5+";"+gameMenu.userInfo.getName()+";"+(((int)worldGame.score/worldGame.diedTimes)+1)+";"+0+";"+0+";");
		out.flush();
		//gameMenu.swap(12);
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
		if(buttonCode==2)
			worldGame.currentPlayer.controllerHasChangedWeapon=true;
		else if(buttonCode==3)
			worldGame.currentPlayer.controllerHasShoted=true;
		else if(buttonCode == 6)
			worldGame.currentPlayer.controllerHasChangedVelocity=true;
		else if(buttonCode == 9)
			gameIsInPause=true;
		
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if(buttonCode==3)
			worldGame.currentPlayer.controllerHasShoted=false;
		else if(buttonCode == 6)
			worldGame.currentPlayer.controllerHasChangedVelocity=false;		
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		povDirection=value;
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

