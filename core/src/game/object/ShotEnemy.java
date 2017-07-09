package game.object;

import com.badlogic.gdx.math.Vector2;

import game.manager.World;
import game.pools.ConstantField;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class ShotEnemy extends DynamicObject {
	int target;
	final int shotRange=3;
	public boolean visible=true;
	public ShotEnemy(int target,Vector2 position,Vector2 direction) {
		super();
		setVelocity(ConstantField.SHOT_VELOCITY);
		this.target=target;
		this.position = position;
		this.direction = direction;
		
		//set shot position at the center of player with correct direction
		position.x+=30*(direction.x+1);
		position.y+=30*(direction.y+1);
		Vector2 scale=new Vector2(direction);
		scale.scl(20*target);
		position.add(scale);
		
		// per regolarizzare la posizione degli spari in base alla dir
		position.add(new Vector2(30*direction.x,30*direction.y));
		
		if(direction.x!=0){
			size.x= GameConfig.SHOT_SIZE.x;
			size.y= GameConfig.SHOT_SIZE.y;
		}
		else{
			size.x= GameConfig.SHOT_VERTICAL_SIZE.x;
			size.y= GameConfig.SHOT_VERTICAL_SIZE.y;
		}
			
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}

}
