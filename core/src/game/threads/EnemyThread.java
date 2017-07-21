package game.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import game.manager.World;
import game.object.Character;
import game.object.Enemy;
import game.object.ShotEnemy;
import game.object.ShotPlayer;
import game.object.StaticObject;

public class EnemyThread extends Thread {

	Vector2 playerPosition;
	public boolean stopThread;
	public boolean canAdd;
	public ArrayList<Enemy> addedEnemy;

	public EnemyThread(Vector2 playerPosition) {
		super();
		stopThread = false;
		this.playerPosition = playerPosition;
		addedEnemy = new ArrayList<Enemy>();
	}

	@Override
	public void run() {
		super.run();
		Random rand = new Random(World.enemies.size());
		long start = System.currentTimeMillis();
		long current;
		float dt = 0.f;
		ArrayList<Enemy> enemyDied = new ArrayList<Enemy>();
		while (!(World.enemies.isEmpty())) {
			if (!stopThread) {
				synchronized (this) {
					current = System.currentTimeMillis();
					dt = current - start;
					for (Enemy e : World.enemies) {
						if (e.shoting) {
							e.shotAnimationTime += dt / 1000;
							if (e.shotAnimationTime > 0.3 && !e.shoted) {
								e.addShot();
								e.shoted = true;
							} else if (e.shotAnimationTime > 0.6) {
								e.shoted = false;
								e.shoting = false;
								e.shotAnimationTime = 0.f;
							}
						} else if (!e.standing) {
							try {
								e.move((int) (World.classe.getMethod("getMoveDirection", Vector2.class).invoke(e,
										playerPosition)), dt / 1000);
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalArgumentException e1) {
								System.out.println("errore");
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (NoSuchMethodException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (SecurityException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							StaticObject tmp = World.checkCollisionObject(e);
							if (tmp instanceof ShotPlayer || !e.alive) {
								enemyDied.add(e);
								World.score += 100;
							} else if (tmp != null) {
								e.collided = true;
								e.cont--;
								e.stateEnemyTime = 0.f;
							}
							e.stateEnemyTime += dt / 1000;
						} else {
							StaticObject tmp = World.checkCollisionObject(e);
							if (tmp instanceof ShotPlayer || !e.alive) {
								enemyDied.add(e);
								World.score += 100;
							} else if (tmp != null) {
								e.collided = true;
								e.cont--;
								e.stateEnemyTime = 0.f;
							}
							e.stateEnemyTime += dt / 1000;
						}
						start = current;
					}
				}
				World.enemies.removeAll(enemyDied);
				enemyDied.clear();
				if (stopThread)
					break;

			}
		}
	}
}
