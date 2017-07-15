package game.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import game.interfaces.ICollideable;
import game.interfaces.IMoveable;
import game.manager.World;
import game.pools.GameConfig;

public class DynamicObject extends StaticObject implements IMoveable {

	private int velocity = 100;
	public Vector2 direction = new Vector2();

	@Override
	public void move(int dir, float dt) {
		setDirection(dir);
		position.x += direction.x * getVelocity() * dt;
		position.y += direction.y * getVelocity() * dt;
		if (!(position.x >= 0 && position.y >= 0 && position.x < GameConfig.MAP_SIZE.x - GameConfig.PLAYER_SIZE.x * 2
				&& position.y < GameConfig.MAP_SIZE.y - GameConfig.PLAYER_SIZE.y)) {
			if (!(this instanceof ShotEnemy) && !(this instanceof ShotPlayer) && !(this instanceof Shot)) {
				setDirection((dir + 2) % 4);
				position.x += direction.x * getVelocity() * dt;
				position.y += direction.y * getVelocity() * dt;
			}
		}
	}

	public int getCodDirection() {
		if (this.direction.x < 0)
			return 3;
		if (this.direction.x > 0)
			return 1;
		if (this.direction.y < 0)
			return 2;
		if (this.direction.y > 0)
			return 0;
		return 0;
	}

	public void setDirection(int dir) {
		switch (dir) {
		case 0:
			direction.set(0, 1);
			break;
		case 1:
			direction.set(1, 0);
			break;
		case 2:
			direction.set(0, -1);
			break;
		case 3:
			direction.set(-1, 0);
			break;
		default:
			break;
		}
	}

	public Vector2 getDirection() {
		return direction;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
}
