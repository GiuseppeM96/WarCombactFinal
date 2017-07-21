package it.unical.game.desktop; 

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import game.manager.WarCombat;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.addIcon("src/img/icone.png", Files.FileType.Internal);
		new LwjglApplication(new WarCombat(), config);
	}
}

