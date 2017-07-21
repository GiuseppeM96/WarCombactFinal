package game.pools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.File;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ImagePool {
	public static Texture mapOne = new Texture("img/mapOne.jpg");
	public static Texture mapTwo = new Texture("img/mapTwo.jpg");
	public static Texture mapThree = new Texture("img/mapThree.jpg");
	public static Texture navigationMap = new Texture("img/miniMappa.png");
	public static Texture navigationPlayer = new Texture("img/personaggio/navigationPlayer.png");
	public static Texture navigationEnemy = new Texture("img/personaggio/nemico/navigationEnemy.png");
	public static Texture hut = new Texture("img/buildings/badHouse.png");
	public static Texture casino = new Texture("img/buildings/BlackBuild.png");
	public static Texture castle = new Texture("img/buildings/goldCastle.png");
	public static Texture bigHut = new Texture("img/buildings/CapannaGrande.png");
	public static Texture tree = new Texture("img/Albero/Albero.png");
	public static Texture moveTree = new Texture("img/Albero/AnimazioneAlbero.png");
	public static Texture insideCastle = new Texture("img/buildings/castelloDentro.jpg");
	public static Texture insideBlackHouse = new Texture("img/black.png");;
	public static Texture firstDialogue = new Texture("img/firstDialogue.png");
	public static Texture secondDialogue = new Texture("img/secondDialogue.png");
	public static Texture thirdDialogue = new Texture("img/thirdDialogue.png");
	public static Texture fourthDialogue = new Texture("img/fourthDialogue.png");
	public static Texture firstDialogueBlack = new Texture("img/primoDialogoBlack.png");
	public static Texture secondDialogueBlack = new Texture("img/secondoDialogoBlack.png");
	public static Texture magician = new Texture("img/personaggio/mago.png");
	public static Texture explotion = new Texture("img/skills/poisonAnimation.png");
	public static Texture connecting = new Texture("img/skills/connectingAnimation.png");
	public static Texture potion = new Texture("img/skills/potion.png");
	public static Texture scroll = new Texture("img/skills/scroll.png");
	public static Texture people = new Texture("img/personaggio/contadino.png");
	public static Texture triangle = new Texture("img/skills/triangle.png");
	public static Texture emptyLetter = new Texture("img/trattino.png");

	public static Texture bash = new Texture("img/Albero/cespuglio.png");
	public static Texture addLife = new Texture("img/skills/lifePoints.png");
	public static Texture shotGun = new Texture("img/skills/shotGun.png");
	public static Texture machineGun = new Texture("img/skills/mitra.png");
	public static Texture moveEnemy = new Texture("img/personaggio/nemico/muoviNemico.png");
	public static Texture movePlayer = new Texture("img/personaggio/muoviPlayer.png");
	public static Texture wakeUpPeople = new Texture("img/personaggio/peopleWakeUp.png");
	public static Texture king = new Texture("img/personaggio/king.png");
	public static Texture playerStopped = new Texture("img/personaggio/destra.png");
	public static Texture boyDied = new Texture("img/personaggio/peopleDies.png");

	public static Texture shotPlayer = new Texture("img/personaggio/animazioneSparo.png");
	public static Texture diedPlayer = new Texture("img/personaggio/morte.png");
	public static Texture shotEnemy = new Texture("img/personaggio/nemico/animazioSparoNemico.png");
	public static Texture shot = new Texture("img/skills/proiettile.png");
	public static Texture verticalShot = new Texture("img/skills/proiettileVerticale.png");
	public static Texture winterShot = new Texture("img/skills/proiettileInvernale.png");
	public static Texture winterVerticalShot = new Texture("img/skills/proiettileVerticaleInvernale.png");
	public static Texture life100 = new Texture("img/skills/life100.png");
	public static Texture bar = new Texture("img/skills/infoBar.png");
	public static Texture rightShot = new Texture("img/personaggio/rightShot.png");
	public static Texture flag = new Texture("img/buildings/flag.png");
	public static Texture well = new Texture("img/buildings/pozzo.png");
	public static Texture killEnemies = new Texture("img/killEnemies.png");
	public static Texture help = new Texture("img/Help.png");;
	public static Texture poison = new Texture("img/poison.png");;
	public static Texture saveVillage = new Texture("img/saveVillage.png");;
	public static Texture introLevelOne = new Texture("img/introLevelOne.png");;
	public static Texture introLevelEven = new Texture("img/introEvenLevel.png");
	public static Texture introLevelOdd = new Texture("img/introOddLevel.png");
	public static Texture connectingBackGround = new Texture("img/connectionBackGround.jpg");

	public static Texture A = new Texture(("img/A.png"));
	public static Texture E = new Texture(("img/E.png"));
	public static Texture G = new Texture(("img/G.png"));
	public static Texture H = new Texture(("img/H.png"));
	public static Texture I = new Texture(("img/I.png"));
	public static Texture L = new Texture(("img/L.png"));
	public static Texture N = new Texture(("img/N.png"));
	public static Texture O = new Texture(("img/O.png"));
	public static Texture P = new Texture(("img/P.png"));
	public static Texture S = new Texture(("img/S.png"));
	public static Texture V = new Texture(("img/V.png"));

	public static TextureRegion[][] matrixConnecting = TextureRegion.split(connecting, 600, 300);
	public static Animation<TextureRegion> connectingAnimation = new Animation<TextureRegion>(.4f, matrixConnecting[0]);

	public static TextureRegion[][] matrixPotion = TextureRegion.split(explotion, 640, 480);
	public static Animation<TextureRegion> potionAnimation = new Animation<TextureRegion>(.3f, matrixPotion[0]);

	public static TextureRegion[][] matrixWakeUp = TextureRegion.split(wakeUpPeople, 60, 60);
	public static Animation<TextureRegion> wakeUpAnimation = new Animation<TextureRegion>(.3f, matrixWakeUp[0]);

	public static TextureRegion[][] matrixTree = TextureRegion.split(moveTree, 63, 100);
	public static Animation<TextureRegion> treeAnimation = new Animation<TextureRegion>(.3f, matrixTree[0]);

	public static TextureRegion[][] matrixDiedPlayer = TextureRegion.split(diedPlayer, 60, 60);
	public static Animation<TextureRegion> playerAnimationDied = new Animation<TextureRegion>(0.15f,
			matrixDiedPlayer[0]);

	public static TextureRegion[][] matrixMove = TextureRegion.split(movePlayer, 60, 60);
	public static Animation<TextureRegion> playerAnimationUp = new Animation<TextureRegion>(0.15f, matrixMove[0]);
	public static Animation<TextureRegion> playerAnimationRight = new Animation<TextureRegion>(0.15f, matrixMove[1]);
	public static Animation<TextureRegion> playerAnimationDown = new Animation<TextureRegion>(0.15f, matrixMove[2]);
	public static Animation<TextureRegion> playerAnimationLeft = new Animation<TextureRegion>(0.15f, matrixMove[3]);

	public static TextureRegion[][] matrixShot = TextureRegion.split(shotPlayer, 60, 60);
	public static Animation<TextureRegion> shotPlayerAnimationUp = new Animation<TextureRegion>(0.05f, matrixShot[0]);
	public static Animation<TextureRegion> shotPlayerAnimationRight = new Animation<TextureRegion>(0.05f,
			matrixShot[1]);
	public static Animation<TextureRegion> shotPlayerAnimationDown = new Animation<TextureRegion>(0.05f, matrixShot[2]);
	public static Animation<TextureRegion> shotPlayerAnimationLeft = new Animation<TextureRegion>(0.05f, matrixShot[3]);

	public static TextureRegion[][] matrixMoveEnemy = TextureRegion.split(moveEnemy, 60, 60);
	public static Animation<TextureRegion> enemyAnimationUp = new Animation<TextureRegion>(0.15f, matrixMoveEnemy[0]);
	public static Animation<TextureRegion> enemyAnimationRight = new Animation<TextureRegion>(0.15f,
			matrixMoveEnemy[1]);
	public static Animation<TextureRegion> enemyAnimationDown = new Animation<TextureRegion>(0.15f, matrixMoveEnemy[2]);
	public static Animation<TextureRegion> enemyAnimationLeft = new Animation<TextureRegion>(0.15f, matrixMoveEnemy[3]);

	public static TextureRegion[][] matrixShotEnemy = TextureRegion.split(shotEnemy, 60, 60);
	public static Animation<TextureRegion> shotEnemyAnimationUp = new Animation<TextureRegion>(0.05f,
			matrixShotEnemy[0]);
	public static Animation<TextureRegion> shotEnemyAnimationRight = new Animation<TextureRegion>(0.05f,
			matrixShotEnemy[1]);
	public static Animation<TextureRegion> shotEnemyAnimationDown = new Animation<TextureRegion>(0.05f,
			matrixShotEnemy[2]);
	public static Animation<TextureRegion> shotEnemyAnimationLeft = new Animation<TextureRegion>(0.05f,
			matrixShotEnemy[3]);

	public static Texture joystick = new Texture("Skin/joystick.png");
	public static Texture joystickLeft = new Texture("Skin/joystick-dl.png");
	public static Texture joystickRight = new Texture("Skin/joystick-dr.png");
	public static Texture joystickUp = new Texture("Skin/joystick-u.png");
	public static Texture joystickDown = new Texture("Skin/joystick-d.png");
	public static Texture selected = new Texture("Skin/joystick-bg.png");
	public static Texture macchina = new Texture("Skin/booth.png");
	public static Texture goUp = new Texture("Skin/up.png");
	public static Texture goDown = new Texture("Skin/down.png");
	public static Texture goRight = new Texture("Skin/right.png");
	public static Texture goLeft = new Texture("Skin/left.png");
	public static Texture space = new Texture("Skin/space.png");
	public static Texture zed = new Texture("Skin/zed.png");
	public static Texture backGround = new Texture("Skin/bg.png");
	public static Texture x = new Texture("img/x.png");
	public static Texture esc = new Texture("img/esc.png");

	public static TextureAtlas texture = new TextureAtlas(Gdx.files.internal("Skin/arcade-ui.atlas"));
	public static Skin skin = new Skin(Gdx.files.internal("Skin/arcade-ui.json"), texture);

	public static BitmapFont font = new BitmapFont();

	public static OrthographicCamera camera = new OrthographicCamera(640, 480);

}
