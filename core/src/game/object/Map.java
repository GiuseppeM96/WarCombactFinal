package game.object;

import com.badlogic.gdx.math.Vector2;

import game.pools.GameConfig;

public class Map extends DynamicObject {
	public int type;

	public Map(int i) {
		super();
		size = new Vector2(GameConfig.MAP_SIZE);
		type = i;
	}

	@Override
	public String getType() {
		switch (type) {
		case 1:
			return "Mappa 1";
		case 2:
			return "Mappa 2";
		case 3:
			return "Mappa 3";
		default:
			return "";
		}
	}
}
