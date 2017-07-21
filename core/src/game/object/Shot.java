package game.object;

import com.badlogic.gdx.math.Vector2;

import game.manager.World;
import game.object.DynamicObject;
import game.object.Weapon;
import game.pools.ConstantField;
import game.pools.GameConfig;

public class Shot extends DynamicObject {

	Weapon weapon;
	int target;
	public int codeOwner;
	public boolean visible;

	public Shot(int target, Weapon weapon, int code, Vector2 position, Vector2 direction) {
		super();
		visible = true;
		setVelocity(ConstantField.SHOT_VELOCITY);
		this.target = target;
		this.position = new Vector2(position);
		this.direction = new Vector2(direction);
		this.codeOwner = code;
		
		// set shot position at the center of player with correct direction
		this.position.x += 30 * (this.direction.x + 1);
		this.position.y += 30 * (this.direction.y + 1);
		Vector2 scale = new Vector2(this.direction);
		scale.scl(20 * target);
		this.position.add(scale);
		
		// per regolarizzare la posizione degli spari in base alla dir
		this.position.add(new Vector2(30 * this.direction.x, 30 * this.direction.y));
		if (this.direction.x != 0) {
			size.x = GameConfig.SHOT_SIZE.x;
			size.y = GameConfig.SHOT_SIZE.y;
		} else {
			size.x = GameConfig.SHOT_VERTICAL_SIZE.x;
			size.y = GameConfig.SHOT_VERTICAL_SIZE.y;
		}
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public Weapon getWeapon() {
		return weapon;
	}
}
