package net.mgsx.dl11.model;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import net.mgsx.dl11.assets.Assets;

public class MedPack extends Bonus {
	
	private Image sprite;

	public MedPack(int initX, int initY) {
		super(initX, initY);
		
		Group group = new Group();
		actor = group;
		
		sprite = new Image(Assets.i.getMedPackRegion());
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
		return active && game.heroLife + GameSettings.MEDPACK_LIFE <= GameSettings.HERO_LIFE_MAX;
	}

	@Override
	public boolean aquire(GameState game) {
		if(game.heroLife < GameSettings.HERO_LIFE_MAX){
			unspawn(game);
			game.heroLife += GameSettings.MEDPACK_LIFE;
			Assets.i.audio.playPickupMedpack();
			return true;
		}else{
			Assets.i.audio.playBonusDenied();
			return false;
		}
	}

}
