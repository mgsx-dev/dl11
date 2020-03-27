package net.mgsx.dl11;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Collections;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.screens.MenuScreen;

public class DL11Game extends Game {
	
	public static DL11Game i(){
		return (DL11Game)Gdx.app.getApplicationListener();
	}
	
	@Override
	public void create () {
		Collections.allocateIterators = true;
		Assets.i = new Assets();
		// setScreen(new GameScreen());
		// setScreen(new MazeScreen());
		setScreen(new MenuScreen());
	}

	@Override
	public void setScreen(Screen screen) {
		Screen prevScreen = this.screen;
		super.setScreen(screen);
		if(prevScreen != null){
			prevScreen.dispose();
		}
	}
}
