package game.object;

import game.pools.GameConfig;

public class BigHut extends StaticObject {

	public BigHut(){
		super();
		size=GameConfig.BIGHUT_SIZE;
	}	
	@Override
	public String getType() {
		return "Capanna Grande";
	}
}
	
