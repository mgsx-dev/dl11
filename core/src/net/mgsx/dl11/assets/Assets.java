package net.mgsx.dl11.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import net.mgsx.dl11.utils.MapAnalyser;

public class Assets {
	public static Assets i;
	public Skin skin;
	
	public final Array<MapDesc> maps = new Array<MapDesc>();
	
	public Assets() {
		for(int i=0 ; i<4 ; i++){
			loadMap("maps/map" + (i+1) + ".tmx");
		}
		skin = new Skin(Gdx.files.internal("skins/game-skin.json"));
	}

	private void loadMap(String filename) {
		MapDesc md = new MapDesc();
		md.map = new TmxMapLoader().load(filename);
		md.mask = MapAnalyser.analyse(md.map);
		maps.add(md);
	}

	private TextureRegion getTile(int id) {
		return maps.first().map.getTileSets().getTile(id + 1).getTextureRegion();
	}

	public TextureRegion getWhitePixel() {
		return skin.getRegion("white");
	}
	
	public TextureRegion getDroneRegion(boolean horizontal, boolean vertical) {
		if(horizontal && vertical) return getTile(7);
		if(horizontal) return getTile(3);
		if(vertical) return getTile(6);
		return getTile(2);
	}

	public TextureRegion getHeroRegion() {
		return getTile(8);
	}
}
