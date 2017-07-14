package game.object;

import game.pools.GameConfig;

public class AddShotGunShots extends StaticObject {

	public static final int shots = 30;

	public AddShotGunShots() {
		super();
		size = GameConfig.SHOTGUN_SIZE;
	}

	@Override
	public String getType() {
		return "Fucile";
	}
}
