package game.pools;

import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;

public class GameConfig {

	public static float SCREEN_WIDTH = 640;
	public static float SCREEN_HEIGHT = 480;
	public static Vector2 PLAYER_SIZE = new Vector2(25, 49);
	public static Vector2 ENEMY_SIZE = new Vector2(25, 49);
	public static Vector2 HUT_SIZE = new Vector2(150, 150);
	public static Vector2 BIGHUT_SIZE = new Vector2(199, 140);
	public static Vector2 BLACKHOUSE_SIZE = new Vector2(276, 176);
	public static Vector2 CASTLE_SIZE = new Vector2(279, 228);
	public static Vector2 WELL_SIZE = new Vector2(150, 150);
	public static Vector2 TREE_SIZE = new Vector2(63, 100);
	public static Vector2 BASH_SIZE = new Vector2(40, 20);
	public static Vector2 LIFE_SIZE = new Vector2(30, 30);
	public static Vector2 MACHINEGUN_SIZE = new Vector2(60, 30);
	public static Vector2 SHOTGUN_SIZE = new Vector2(60, 30);
	public static Vector2 MAP_SIZE = new Vector2(2816, 2212);
	public static Vector2 SHOT_VERTICAL_SIZE = new Vector2(ImagePool.winterVerticalShot.getWidth(),
			ImagePool.winterVerticalShot.getHeight());
	public static Vector2 SHOT_SIZE = new Vector2(ImagePool.winterShot.getWidth(), ImagePool.winterShot.getHeight());
	public static Vector2 LETTER_SIZE = new Vector2(ImagePool.A.getWidth(), ImagePool.A.getHeight());
	public static Controllers controller = new Controllers();
}
