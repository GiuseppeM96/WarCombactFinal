package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import game.manager.GameMenu;
import game.object.Character;
import game.object.Map;
import game.object.StaticObject;
import game.pools.ImagePool;

public class BlackHouseScreen implements Screen {

	StaticObject magician;
	boolean collided;
	SpriteBatch worldBatch;
	GameMenu gameMenu;
	Character player;
	float statePlayerTime;
	int dialogue;
	public int level;
	public BlackHouseScreen(GameMenu game) {
		super();
		gameMenu=game;
		this.level=level;
		dialogue=0;
		statePlayerTime=0.f;
		magician = new StaticObject();
		magician.setPosition(new Vector2(300, 30));
		collided=false;
		player = new Character();
		player.setPosition(new Vector2(0,30));
		worldBatch=new SpriteBatch();
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
		if(!collided){
			if(player.collide(magician)){
				collided=true;
				statePlayerTime=0.f;
			}
			else{
				player.move(1, delta);
				statePlayerTime+=delta;
			}
		}
		else if(dialogue<2){
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
				dialogue++;
			}
		}
		else{
			if(player.getPosition().x<5){
				dialogue=0;
				statePlayerTime=0.f;
				collided=false;
				gameMenu.intro(3);
			}
			else player.move(3, delta);
			statePlayerTime+=delta;
		}
		
	}
	private void drawWorld() {
		worldBatch.draw(ImagePool.insideBlackHouse, 0, 0);
		worldBatch.draw(ImagePool.magician, magician.getPosition().x, magician.getPosition().y);
		if(!collided){
			worldBatch.draw(ImagePool.playerAnimationRight.getKeyFrame(statePlayerTime ,true),player.getPosition().x,player.getPosition().y);
		}
		else if(dialogue<2) {
			if(dialogue == 0)
				worldBatch.draw(ImagePool.firstDialogueBlack, 0, 300);
			else worldBatch.draw(ImagePool.secondDialogueBlack, 0, 300);
			worldBatch.draw(ImagePool.playerStopped,player.getPosition().x,player.getPosition().y);
		}
		else {
			worldBatch.draw(ImagePool.playerAnimationLeft.getKeyFrame(statePlayerTime ,true),player.getPosition().x,player.getPosition().y);
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
