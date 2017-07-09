package game.object;

import game.pools.GameConfig;

public class BlackHouse extends StaticObject {

	public BlackHouse(){
		super();
		size=GameConfig.BLACKHOUSE_SIZE;
	}
	@Override
	public String getType() {
		return "Casinò";
	}
}
