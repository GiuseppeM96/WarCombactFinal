package game.object;

import game.pools.GameConfig;

public class Castle extends StaticObject {

	public Castle() {
		super();
		size = GameConfig.CASTLE_SIZE;
	}

	@Override
	public String getType() {
		return "Castello";
	}
}
