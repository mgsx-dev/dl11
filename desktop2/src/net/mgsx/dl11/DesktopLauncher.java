package net.mgsx.dl11;

import java.awt.SplashScreen;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.mgsx.dl11.DL11Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		// config.setWindowedMode(640, 480);
		config.useVsync(false);
		new Lwjgl3Application(new DL11Game(){
			private Sync sync = new Sync();
			@Override
			public void create() {
				SplashScreen splashScreen = SplashScreen.getSplashScreen();
				if(splashScreen != null){
					splashScreen.close();
				}
				super.create();
			}
			@Override
			public void render() {
				super.render();
				sync.sync(60);
			}
		}, config);
	}
}
