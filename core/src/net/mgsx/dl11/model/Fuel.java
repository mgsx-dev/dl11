package net.mgsx.dl11.model;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import net.mgsx.dl11.assets.Assets;

public class Fuel extends Bonus {

	private Image sprite;

	public Fuel(int initX, int initY) {
		super(initX, initY);
		
		Group group = new Group();
		actor = group;
		
		sprite = new Image(Assets.i.getFuelRegion());
		sprite.setSize(1, 1);
		sprite.setPosition(0, 0, Align.center);
		
		group.addActor(sprite);
	}
	
	@Override
	public void update(float delta) {
		actor.setPosition(position.x, position.y);
	}

	@Override
	public boolean canBeAquired(GameState game) {
		return active && game.heroFuel < GameSettings.HERO_FUEL_MAX;
	}

	@Override
	public boolean aquire(GameState game) {
		if(game.heroFuel < GameSettings.HERO_FUEL_MAX){
			unspawn(game);
			game.heroFuel++;
			Assets.i.audio.playPickupFuel();
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected void setClosed() {
		sprite.setDrawable(new TextureRegionDrawable(Assets.i.getFuelRegion()));
	}

	@Override
	protected void setOpened() {
		sprite.setDrawable(new TextureRegionDrawable(Assets.i.getFuelOpenRegion()));
	}

}
