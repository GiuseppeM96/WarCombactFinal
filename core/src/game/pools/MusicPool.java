package game.pools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicPool {

	public static Music musicMenu = Gdx.audio.newMusic(Gdx.files.internal("Music/musicMenu.mp3"));
	public static Music walkingSound = Gdx.audio.newMusic(Gdx.files.internal("Music/WalkingNew.ogg"));
	public static Music shotGunSound = Gdx.audio.newMusic(Gdx.files.internal("Music/shotGun.ogg"));
	public static Music machineGunSound = Gdx.audio.newMusic(Gdx.files.internal("Music/shotGun5.ogg"));
	public static Music reloadSound = Gdx.audio.newMusic(Gdx.files.internal("Music/reload.mp3"));
	public static Music addShotSound = Gdx.audio.newMusic(Gdx.files.internal("Music/addShot.ogg"));
	public static Music addLifePoints = Gdx.audio.newMusic(Gdx.files.internal("Music/addLifePoints.ogg"));
	public static Music pickLetter = Gdx.audio.newMusic(Gdx.files.internal("Music/pickLetter.ogg"));
}
