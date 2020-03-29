package net.mgsx.dl11.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.dl11.DL11Game;
import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.model.GameSettings;
import net.mgsx.dl11.utils.StageScreen;
import net.mgsx.dl11.utils.UI;

public class MenuScreen extends StageScreen {

	public MenuScreen() {
		super(new FitViewport(GameSettings.HUD_WIDTH, GameSettings.HUD_HEIGHT));
		
		Skin skin = Assets.i.skin;
		
		Table root = new Table(skin);
		root.setFillParent(true);
		stage.addActor(root);
		
		root.setBackground(new TextureRegionDrawable(Assets.i.titleScreen));
		
		/*
		root.add(new Label("Anno Bellum 239", skin, "title")).getActor().setColor(Color.BLACK);
		root.row();
		root.add("CatWired - kAy mOttO - MGSX - Toonguila").getActor().setColor(Color.DARK_GRAY);
		root.row();
		*/
		
		root.add(UI.onChange(UI.blinkButton("Press X to start"), event->startGame())).expand().bottom()
		.padBottom(25).row();
	}
	
	private void startGame(){
		Assets.i.audio.playMenuButton();
		DL11Game.i().setScreen(new IntroScreen());
	}
	
	@Override
	public void show() {
		Assets.i.audio.playMusicMenu();
		super.show();
	}
	
	@Override
	public void render(float delta) {
		clear(Color.BLACK);
		super.render(delta);
	}
}
