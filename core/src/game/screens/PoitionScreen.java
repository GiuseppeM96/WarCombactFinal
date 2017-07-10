package game.screens;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
import game.object.Character;
import game.object.DynamicObject;
import game.object.Enemy;
import game.object.Hut;
import game.object.Letter;
import game.object.Map;
import game.object.StaticObject;
import game.object.Tree;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class PoitionScreen implements Screen{

	DynamicObject potion;
	boolean collided;
	SpriteBatch worldBatch;
	GameMenu gameMenu;
	Character player;
	float statePlayerTime;
	ArrayList<Character> people;
	ArrayList<Hut> huts;
	ArrayList<BigHut> bigHuts;
	Map screenMap;
	boolean esplotion;
	boolean wakingUp;
	boolean end;
	int animation;
	OrthographicCamera cam;
	public PoitionScreen(GameMenu game) {
		super();
		gameMenu=game;
		cam=new OrthographicCamera(640,480);
		cam.position.x=GameConfig.MAP_SIZE.x/2;
		cam.position.y=GameConfig.MAP_SIZE.y/2;
		esplotion=false;
		wakingUp=false;
		end=false;
		animation=0;
		people=new ArrayList<Character>();
		huts=new ArrayList<Hut>();
		bigHuts=new ArrayList<BigHut>();
		try {
			initVillage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		statePlayerTime=0.f;
		potion = new DynamicObject();
		potion.setVelocity(300);
		potion.setDirection(2);
		potion.setPosition(new Vector2(GameConfig.MAP_SIZE.x/2, GameConfig.MAP_SIZE.y/2+240));
		collided=false;
		player = new Character();
		player.setPosition(new Vector2(0,30));
		worldBatch=new SpriteBatch();
		System.out.println(bigHuts.size()+" "+people.size());
	}
	private void initVillage() throws IOException {
		FileReader reader = new FileReader("src/GameComplete.txt");
		BufferedReader buffer = new BufferedReader(reader);
		String line=buffer.readLine();
		while(line!= null){
			String type = new String(),codx = new String(),cody = new String();
			char [] arrayLine = new char[line.length()];
			line.getChars(0, line.length(), arrayLine, 0);
			int i = 0;
			for (; arrayLine[i]!=';';i++) {
				type+=arrayLine[i];
			}
			i++;
			for (; arrayLine[i]!=';';i++) {
				codx+=arrayLine[i];
			}
			i++;
			for (; arrayLine[i]!=';';i++) {
				cody+=arrayLine[i];
			}
			StaticObject tmp=createNewObject(type,codx,cody);
			if(tmp instanceof Map)
				screenMap=(Map) tmp;
			else if(tmp instanceof Character)
				people.add((Character)tmp);
			else if(tmp instanceof Hut)
				huts.add((Hut)tmp);
			else if(tmp instanceof BigHut)
				bigHuts.add((BigHut) tmp);
			line=buffer.readLine();
		}
		buffer.close();
	}
	private StaticObject createNewObject(String type, String codx, String cody) {
		StaticObject tmp = getObject(type);
		tmp.setPosition(new Vector2(convert(codx), convert(cody)));
		return tmp;
	}
	private float convert(String codx) {
		char[] tmp =codx.toCharArray();
		int result=0;
		for (int i = 0; i < tmp.length; i++) {
			result*=10;
			result+=tmp[i]-'0';
		}
		return result;
	}
	private StaticObject getObject(String type) {
		switch(type){
		case "1":
			return new Map(1);
		case "2":
			return new Map(2);
		case "3":
		return new Hut();
		case "4":
			return new Castle();
		case "5":
			return new BlackHouse();
		case "6":
			return new BigHut();
		case "7":
			return new Bash();
		case "8":
			return new Tree();
		case "9":
			return new AddMachineGunShots();
		case "10":
			return new AddShotGunShots();
		case "11":
			return new AddLifePoints();
		case "12":
			return new Character();
		case "13":
			return new Letter('a');
		case "14":
			return new Letter('e');
		case "15":
			return new Letter('g');
		case "16":
			return new Letter('h');
		case "17":
			return new Letter('i');
		case "18":
			return new Letter('l');
		case "19":
			return new Letter('n');
		case "20":
			return new Letter('o');
		case "21":
			return new Letter('p');
		case "22":
			return new Letter('s');
		case "23":
			return new Letter('v');
		default:
			return null;
		}
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		worldBatch.setProjectionMatrix(cam.combined);
		worldBatch.begin();
		
		drawWorld();
		update(delta);
		cam.update();
		worldBatch.end();
	}

	private void update(float delta) {
		if(!collided){
			if(potion.getPosition().y<GameConfig.MAP_SIZE.y/2){
				collided=true;
			}
			else{
				potion.move(2, delta);
			}
		}
		else 
			statePlayerTime+=delta;
	}
	private void drawWorld() {
		drawVillage();
		if (animation <= 1000) {
			cam.zoom+=0.006f;
			animation++;
		}
		else end=true;
		if(!collided){
			worldBatch.draw(ImagePool.potion, potion.getPosition().x, potion.getPosition().y);			
		}
		else{
			if(!wakingUp)
			worldBatch.draw(ImagePool.potionAnimation.getKeyFrame(statePlayerTime ,true),potion.getPosition().x-GameConfig.SCREEN_WIDTH/2,potion.getPosition().y-GameConfig.SCREEN_HEIGHT/2);
			if(statePlayerTime>0.9 && !wakingUp){
				esplotion=true;
				wakingUp=true;
				statePlayerTime=0.f;
			}
			else if(wakingUp){
				if(statePlayerTime>1.8){
					end=true;
					
				}
			}
		}
		if(end)
			gameMenu.swap(4);
	}
	
	private void drawVillage() {
		worldBatch.draw(ImagePool.mapTwo, 0, 0);
		for(Character c:people){
			if(!esplotion){
				worldBatch.draw(ImagePool.boyDied, c.getPosition().x, c.getPosition().y);
			}
			else{
				if(statePlayerTime<1.8)
					worldBatch.draw(ImagePool.wakeUpAnimation.getKeyFrame(statePlayerTime ,true),c.getPosition().x,c.getPosition().y);
				else worldBatch.draw(ImagePool.people, c.getPosition().x, c.getPosition().y);
			}
		}
		for(Hut h:huts){
			worldBatch.draw(ImagePool.hut, h.getPosition().x, h.getPosition().y);
		}
		for(BigHut b:bigHuts){
			worldBatch.draw(ImagePool.bigHut, b.getPosition().x, b.getPosition().y);
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
	
}
