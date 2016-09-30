package com.foids.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.foids.FoidsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new FoidsGame(), config);
		config.width = 1280;
		config.height = 720;
		config.title = "FOIDS by Cedric Martens";
		config.resizable = false;
	}
}
