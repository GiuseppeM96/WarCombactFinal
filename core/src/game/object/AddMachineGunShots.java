package game.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import game.pools.GameConfig;

public class AddMachineGunShots extends StaticObject{

	public static int shots = 50;
	public AddMachineGunShots() {
		super();
		size=GameConfig.MACHINEGUN_SIZE;
	}
	@Override
	public String getType() {
		return "Mitra";
	}
}
