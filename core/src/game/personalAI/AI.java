package game.personalAI;

import com.badlogic.gdx.math.Vector2;

import game.object.Enemy;
import game.object.ShotEnemy;
import game.pools.ConstantField;
import game.utility.StepDirection;

public class AI extends Enemy{
	
	public int getMoveDirection(Vector2 playerPosition) 
	{
		return 0;
	}

	public void shotAI() {
		
		Vector2 directionShot=new Vector2(0,1);
		
		newShots.add(new ShotEnemy(0, new Vector2(position), directionShot));
		newShots.add(new ShotEnemy(-1, new Vector2(position), directionShot));
		newShots.add(new ShotEnemy(-2, new Vector2(position), directionShot));
	}
}
