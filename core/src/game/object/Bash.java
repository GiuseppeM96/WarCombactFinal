package game.object;

import game.pools.GameConfig;

public class Bash extends StaticObject {
	
	public Bash(){
		super();
		size=GameConfig.BASH_SIZE;
	}
	@Override
	public String getType() {
		return "Cespuglio";
	}
}
