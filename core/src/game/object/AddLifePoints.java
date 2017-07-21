package game.object;

import game.pools.GameConfig;

public class AddLifePoints extends StaticObject {

	public static int addPoints ;

	public AddLifePoints() {
		super();
		addPoints = 100;
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
