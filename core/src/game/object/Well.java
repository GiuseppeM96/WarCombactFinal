package game.object;

import game.pools.GameConfig;

public class Well extends StaticObject {
	public Well() {
		super();
		size = GameConfig.WELL_SIZE;
	}

	@Override
	public String getType() {
		return "Well";
	}
}
