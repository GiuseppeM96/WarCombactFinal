package game.object;

import com.badlogic.gdx.math.Vector2;

import game.interfaces.ICollideable;

public class StaticObject implements ICollideable {
	public Vector2 position=new Vector2();
	public Vector2 size=new Vector2();
	
	@Override
	public boolean collide(ICollideable object) {
		if(object instanceof StaticObject){
			StaticObject tmp =(StaticObject)object;
			if(!(this.position.x>tmp.position.x+tmp.size.x || tmp.position.x>this.position.x+15+this.size.x|| this.position.y>tmp.position.y+tmp.size.y || tmp.position.y>this.position.y-10+this.size.y))
				return true;
		}
		return false;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
