package game.object;

import game.pools.GameConfig;

public class Tree extends StaticObject {

	static public float animationTime;
	public Tree(){
		super();
		animationTime=0.f;
		size=GameConfig.TREE_SIZE;
	}
	@Override
	public String getType() {
		return "Albero";
	}
}
