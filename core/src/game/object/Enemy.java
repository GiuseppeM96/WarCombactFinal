package game.object;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;

import game.manager.World;
import game.pools.ConstantField;
import game.pools.GameConfig;
import game.utility.StepDirection;

public class Enemy extends DynamicObject {

	//public int AI;
	public int researchDirection;
	public int actionDistance;
	public boolean collided = false;
	public boolean alive = true;
	public boolean shoting=false;
	public float shotAnimationTime=0.f;
	public float stateEnemyTime=0.f;
	protected StepDirection[] lastSteps;
	protected final int numSteps = 20000;
	int contDis = 0;
	public int cont = 0;
	public boolean standing=false;
	public boolean shoted=false;
	protected ArrayList<ShotEnemy> newShots = new ArrayList<ShotEnemy>();

	public Enemy() {
		super();
		lastSteps = new StepDirection[numSteps];
		for (int i = 0; i < lastSteps.length; i++)
			lastSteps[i] = StepDirection.ERR;
		setVelocity(ConstantField.ENEMY_STD_VELOCITY);
		size = GameConfig.ENEMY_SIZE;
		actionDistance = 200;
		researchDirection = (int) (Math.random() % 2);
	}
	/*public void setAI(int AI){
		this.AI=AI;
	}*/
	public int getMoveDirection(Vector2 playerPosition) {
		int returnVal = getCodDirection();
		standing=false;
		if (Math.sqrt(Math.pow((position.x - playerPosition.x), 2)
				+ Math.pow((position.y - playerPosition.y), 2)) < actionDistance) {
			if (Math.sqrt(Math.pow((position.x - playerPosition.x), 2)
					+ Math.pow((position.y - playerPosition.y), 2)) < actionDistance/2){
				if((position.x>playerPosition.x-5 && position.x<playerPosition.x+5)){
					int dir;
					if(position.y>=playerPosition.y)
						dir=2;
					else dir=0;
					setDirection(dir);
					if(!shoting){
						shoting=true;
						shotAI();
					}
					standing=true;
					stateEnemyTime=0.f;
				}
				else if(position.y>playerPosition.y-5 && position.y<playerPosition.y+5){
					
					int dir;
					if(position.x>=playerPosition.x)
						dir=3;
					else dir=1;
					setDirection(dir);
					if(!shoting){
						shoting=true;
						shotAI();
					}
					standing=true;
					stateEnemyTime=0.f;
				}
			}
			else if (!collided) {
				setVelocity(ConstantField.ENEMY_SUPER_VELOCITY);
				if (researchDirection == 0) {
					if (playerPosition.x < position.x - 5) {
						addSteps(StepDirection.SX,cont);
						if(cont<numSteps)
							cont++;
							return 3;
					}
					if (playerPosition.x > position.x + 5) {
						addSteps(StepDirection.DX,cont);
						if(cont<numSteps)
							cont++;
							return 1;
					}
					researchDirection = 1;
				}
				if (researchDirection == 1) {
					if (playerPosition.y > position.y + 5) {
						addSteps(StepDirection.UP,cont);
						if(cont<numSteps)
							cont++;
							return 0;
					}
					if (playerPosition.y < position.y - 5) {
						addSteps(StepDirection.DOWN,cont);
						if(cont<numSteps)
							cont++;
							return 2;
					}
					researchDirection = 0;
				}
			} else {

				if(!shoting){
					if (cont < 0) {
						collided = false;
						cont = 0;
						addSteps(convertDir(returnVal),cont);
					} else {
						StepDirection c = lastSteps[cont];
						if(c != StepDirection.ERR)
							returnVal = (convertStepDirection(c) + 2) % 4;
						cont--;
					}
					return returnVal;
				}
			}
		}
		setVelocity(ConstantField.ENEMY_STD_VELOCITY);
		if (!collided){
			addSteps(convertDir(returnVal),cont);
			if(cont<numSteps)
				cont++;
		}
		else{
			if (cont < 0) {
				collided = false;
				cont = 0;
				addSteps(convertDir(returnVal),cont);
				cont++;
			} else {
				if(cont<numSteps){
					StepDirection c = lastSteps[cont];
					returnVal = (convertStepDirection(c) + 2) % 4;
					cont--;
				}
			}
		}
		if(shoting)
			return getCodDirection();
		return returnVal;
	}
	@Override
	public String getType(){
		return "Nemico";
	}
	protected int convertStepDirection(StepDirection d) {
		switch (d) {
		case DOWN:
			return 2;
		case UP:
			return 0;
		case SX:
			return 3;
		case DX:
			return 1;
		default:
			return 0;
		}
	}

	protected StepDirection convertDir(int i) {
		switch (i) {
		case 0:
			return StepDirection.UP;
		case 1:
			return StepDirection.DX;
		case 2:
			return StepDirection.DOWN;
		case 3:
			return StepDirection.SX;
		default:
			return StepDirection.UP;
		}
	}

	protected void addSteps(StepDirection dir,int cont) {
		if(cont>=numSteps){
			cont--;
			shiftArray(lastSteps);
		}
		if(cont>=0)
			lastSteps[cont] = dir;
	}

	private void shiftArray(StepDirection[] last20Steps2) {
		for (int i = 0; i < numSteps-1; i++)
			last20Steps2[i] = last20Steps2[i + 1];
	}

	public void shotAI() {
		Vector2 directionShot=new Vector2();
		switch(getCodDirection()){
			case 0:
				directionShot.set(0, 1);
				break;
			case 1:
				directionShot.set(1, 0);
				break;
			case 2:
				directionShot.set(0, -1);
				break;
			case 3:
				directionShot.set(-1, 0);
				break;
			default : break;
		}
		newShots.add(new ShotEnemy(0, new Vector2(position), directionShot));
		newShots.add(new ShotEnemy(-1, new Vector2(position), directionShot));
		newShots.add(new ShotEnemy(-2, new Vector2(position), directionShot));
	}

	public void addShot() {
		World.shotsEnemy.addAll(newShots);
		newShots.clear();
	}
	
}
