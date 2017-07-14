package game.object;

import game.pools.GameConfig;

public class Hut extends StaticObject {

	public Hut() {
		super();
		size = GameConfig.HUT_SIZE;
	}

	@Override
	public String getType() {
		return "Capanna";
	}
}
