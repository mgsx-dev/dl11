package net.mgsx.dl11;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mgsx.dl11.model.GameSettings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		GameSettings.arguments = arg;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new DL11Game(), config);
	}
}
