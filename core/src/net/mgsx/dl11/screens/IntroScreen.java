package net.mgsx.dl11.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.dl11.DL11Game;
import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.model.GameSettings;
import net.mgsx.dl11.model.Story;
import net.mgsx.dl11.ui.Dialogs;
import net.mgsx.dl11.utils.StageScreen;

public class IntroScreen extends StageScreen {

	public IntroScreen() {
		super(new FitViewport(GameSettings.HUD_WIDTH, GameSettings.HUD_HEIGHT));
		
		// Skin skin = Assets.i.skin;
		
		Dialogs.spawnInfo(stage, Story.introText(), Align.center, ()->DL11Game.i().setScreen(new GameScreen()));
	}
	
	@Override
	public void show() {
		Assets.i.audio.playMusicIntro();
		super.show();
	}
	
	@Override
	public void render(float delta) {
		clear(Color.BLACK);
		super.render(delta);
	}
}