package net.mgsx.dl11.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import net.mgsx.dl11.utils.MapAnalyser;

public class Assets {
	public static Assets i;
	public Skin skin;
	
	public final Array<MapDesc> maps = new Array<MapDesc>();
	public TextureRegion car, wheel;
	
	public final TiledMap initMap, lastMap;
	
	public final Array<Array<MapDesc>> mapsByMask = new Array<Array<MapDesc>>();
	
	public final Audio audio;
	
	public final HeroAnimator hero;
	
	public final TextureRegion droneBlue;
	
	public final TextureRegion titleScreen;
	
	public Assets() {
		
		titleScreen = new TextureRegion(new Texture("sprites/title.png"));
		
		audio = new Audio();
		
		for(int i=0 ; i <= 16 ; i++){
			mapsByMask.add(new Array<MapDesc>());
		}
		
		for(String s : Gdx.files.internal("maps.txt").readString().split(",")){
			loadMap("maps/" + s);
		}
		
		initMap = new TmxMapLoader().load("maps/misc-map-init.tmx");
		lastMap  = new TmxMapLoader().load("maps/misc-map-last.tmx");
		
		skin = new Skin(Gdx.files.internal("skins/game-skin.json"));
		skin.getRegion("white").getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		skin.getFont("default-font").getData().setLineHeight(16);
		
		car = skin.getRegion("car");
		wheel = skin.getRegion("wheel");
		
		droneBlue = skin.getRegion("drone-blue");
		
		hero = new HeroAnimator(skin.getRegion("hero-south"));
		
		maps.first().map.getTileSets().getTile(1).getTextureRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	}

	private void loadMap(String filename) {
		MapDesc md = new MapDesc();
		md.name = filename;
		md.map = new TmxMapLoader().load(filename);
		md.mask = MapAnalyser.analyse(md.map);
		maps.add(md);
		mapsByMask.get(md.mask).add(md);
	}

	/*
	private TextureRegion getTile(int id) {
		return maps.first().map.getTileSets().getTile(id + 1).getTextureRegion();
	}
	*/

	public TextureRegion getWhitePixel() {
		return skin.getRegion("white");
	}
	
	public TextureRegion getDroneRegion(boolean horizontal, boolean vertical, boolean rotative) {
		if(rotative){
			if(vertical && horizontal){
				return skin.getRegion("drone-red-bad");
			}else{
				return skin.getRegion("drone-red");
			}
		}else{
			if(vertical && horizontal){
				return skin.getRegion("drone-blue-bad");
			}else{
				return skin.getRegion("drone-blue");
			}
		}
		/*
		if(rotative){
			if(horizontal && vertical) return getTile(15);
			if(horizontal) return getTile(11);
			if(vertical) return getTile(14);
			return getTile(10);
		}
		if(horizontal && vertical) return getTile(7);
		if(horizontal) return getTile(3);
		if(vertical) return getTile(6);
		return getTile(2);
		*/
	}

	/*
	public TextureRegion getHeroRegion() {
		return getTile(8);
	}
	*/

	public TextureRegion getMedPackRegion() {
		return skin.getRegion("medpack2");
	}

	public TextureRegion getFuelRegion() {
		return skin.getRegion("fuel2");
	}
	public TextureRegion getMedPackOpenRegion() {
		return skin.getRegion("medpack2-opened");
	}

	public TextureRegion getFuelOpenRegion() {
		return skin.getRegion("fuel2-open");
	}

	public TextureRegion getLifeRegion() {
		return skin.getRegion("life");
	}
	public TextureRegion getFuelMiniRegion() {
		return skin.getRegion("fuel-mini");
	}
	
	public TextureRegion getLifeEmptyRegion() {
		return skin.getRegion("life-empty");
	}
	public TextureRegion getFuelMiniEmptyRegion() {
		return skin.getRegion("fuel-open-mini");
	}
	
	public TiledMap getMap(String name) {
		for(MapDesc map : maps){
			if(map.name.equals(name)){
				return map.map;
			}
		}
		throw new GdxRuntimeException("map not found: " + name);
	}
}
