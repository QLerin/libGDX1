package com.gdx.invader.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.invader.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = com.gdx.invader.main.Game.TITLE;
		config.width = com.gdx.invader.main.Game.V_WIDTH * com.gdx.invader.main.Game.SCALE;
		config.height = com.gdx.invader.main.Game.V_HEIGHT * com.gdx.invader.main.Game.SCALE;
		new LwjglApplication(new com.gdx.invader.main.Game(), config);
	}
}
