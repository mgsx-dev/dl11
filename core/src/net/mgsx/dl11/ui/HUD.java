package net.mgsx.dl11.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.model.GameSettings;
import net.mgsx.dl11.model.GameState;

public class HUD extends Table
{

	private GameState game;
	private ItemBar lifeBar;
	private ItemBar fuelBar;
	private MeterUI fuelRate;

	public HUD(GameState game, Skin skin) {
		super(skin);
		
		this.game = game;
		
		setFillParent(true);
		
		Table root = new Table(skin);
		root.defaults().pad(.2f);
		add(root).expand().top().left();
		
		root.setRound(false);
		
		root.add(lifeBar = new ItemBar(game.heroLife, GameSettings.HERO_LIFE_MAX, Assets.i.getLifeRegion(), skin));
		root.add(fuelBar = new ItemBar(game.heroFuel, GameSettings.HERO_FUEL_MAX, Assets.i.getFuelRegion(), skin));
		root.add(fuelRate = new MeterUI(Color.YELLOW, skin)).height(.3f).width(4f);
		
		// debugAll();
		
		
	}
	
	@Override
	public void act(float delta) {
		lifeBar.setValue(game.heroLife);
		fuelBar.setValue(game.heroFuel);
		fuelRate.setValue(game.carFuel / GameSettings.CAR_FUEL_MAX);
		super.act(delta);
	}
	
}
