package game.object;

import game.pools.GameConfig;

public class AddLifePoints extends StaticObject {

	public static int addPoints = 100;

	public AddLifePoints() {
		super();
		size = GameConfig.LIFE_SIZE;
	}

	public int getLifePoints() {
		return addPoints;
	}

	@Override
	public String getType() {
		return "Guarigione";
	}
}
