package game.object;


public class Weapon  {

	public int shotRange = 3;
	public int numShots = 3;
	public int damagePerShot = 10;

	public int getNumShots() {
		return numShots;
	}

	public void setNumShots(int numShots) {
		this.numShots = numShots;
	}

	public int getDamagePerShot() {
		return damagePerShot;
	}

	public void setDamagePerShot(int damagePerShot) {
		this.damagePerShot = damagePerShot;
	}

	public void setShotRange(int shotRange) {
		this.shotRange = shotRange;
	}

	public int getShotRange() {
		return shotRange;
	}

	public boolean hasNotShots() {
		// TODO Auto-generated method stub
		return false;
	}

	public void updateNumShots() {
		// TODO Auto-generated method stub

	}
}
