package game.net;

import com.badlogic.gdx.math.Vector2;

import game.object.AddLifePoints;
import game.object.AddMachineGunShots;
import game.object.AddShotGunShots;
import game.object.DynamicObject;
import game.object.Weapon;
import game.pools.ConstantField;
import game.pools.GameConfig;
import game.pools.ImagePool;

public class NetCharacter extends DynamicObject {
	
	public String weaponType;
	Weapon currentWeapon=new Weapon();
	public int lifePoints=1000;
	public int machineGunShots=100;
	public int shotGunShots=60;
	public boolean shoting=false;
	public boolean died=false;
	public boolean shoted=false;
	public int code=-1;
	public float shotAnimationTime=0.f;
	public float stateAnimationTime=0.f;
	public float diedAnimationTime=0.f;
	public boolean moving=false;
	
	public NetCharacter() {
		super();
		shoting=false;
		weaponType="ShotGun";
		setVelocity(ConstantField.PLAYER_STD_VELOCITY);
		size=GameConfig.PLAYER_SIZE;
		direction.set(-1, 0);
	}

	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}

	public void setCurrentWeapon(Weapon currentWeapon) {
		this.currentWeapon = currentWeapon;
	}

	public int getFrame() {
		if(this.direction.x<0)
			return 3;
		if(this.direction.x>0)
			return 1;
		if(this.direction.y<0)
			return 2;
		if(this.direction.y>0)
			return 0;
		return 0;
	}


	public void addLife() {
		lifePoints+=AddLifePoints.addPoints;
	}

	public void addShots(String type) {
		if(type.equals("MachineGun")){
			if(ConstantField.MAX_NUM_SHOT_MACHINEGUN>=machineGunShots+AddMachineGunShots.shots)
				machineGunShots+=AddMachineGunShots.shots;
			else machineGunShots=ConstantField.MAX_NUM_SHOT_MACHINEGUN;
		}
		else 
			if(ConstantField.MAX_NUM_SHOT_SHOTGUN>=machineGunShots+AddMachineGunShots.shots)
				shotGunShots+=AddShotGunShots.shots;
			else shotGunShots=ConstantField.MAX_NUM_SHOT_SHOTGUN;
		
	}

	public void changeSpeed(int newSpeed) {
		float frameDuration;
		if(getVelocity() > newSpeed)
			frameDuration=0.075f;
		else frameDuration=0.15f;
		setVelocity(newSpeed);
		switch(getCodDirection()){
			case 0:
				ImagePool.playerAnimationUp.setFrameDuration(frameDuration);
				break;
			case 1:
				ImagePool.playerAnimationRight.setFrameDuration(frameDuration);
				break;
			case 2:
				ImagePool.playerAnimationDown.setFrameDuration(frameDuration);
				break;
			case 3:
				ImagePool.playerAnimationLeft.setFrameDuration(frameDuration);
				break;
		}
	}

	public void changeWeapon() {
		if(weaponType.equals("ShotGun")){
			weaponType="MachineGun";
			currentWeapon.numShots=5;
		}
		else{
			weaponType="ShotGun";
			currentWeapon.numShots=3;
		}
		
	}
	@Override
	public String getType() {
		return "Player";
	}

	public boolean hasNotShots() {
		if(weaponType.equals("ShotGun")){
			return shotGunShots<=0;
		}
		return machineGunShots<=0;
	}

	public void updateNumShots() {
		if(weaponType.equals("ShotGun")){
			shotGunShots-=3;
		}
		else machineGunShots-=5;
	}

	public int getNumTotShot() {
		if(weaponType.equals("ShotGun"))
			return ConstantField.MAX_NUM_SHOT_SHOTGUN;
		return ConstantField.MAX_NUM_SHOT_MACHINEGUN;
	}

	public int getNumShot() {
		if(weaponType.equals("ShotGun"))
			return shotGunShots;
		return machineGunShots;
	}

	public void reGenerate() {
		died=false;
		lifePoints=1000;
		diedAnimationTime=0.f;		
	}

}