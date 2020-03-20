package net.mgsx.dl11.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	public static Assets i;
	public TiledMap mapPOC;
	public Skin skin;
	
	public Assets() {
		mapPOC = new TmxMapLoader().load("maps/map1.tmx");
		skin = new Skin(Gdx.files.internal("skins/game-skin.json"));
	}

	private TextureRegion getTile(int id) {
		return mapPOC.getTileSets().getTile(id + 1).getTextureRegion();
	}

	public TextureRegion getWhitePixel() {
		return skin.getRegion("white");
	}
	
	public TextureRegion getDroneRegion() {
		return getTile(7);
	}

	public TextureRegion getHeroRegion() {
		return getTile(8);
	}
}
