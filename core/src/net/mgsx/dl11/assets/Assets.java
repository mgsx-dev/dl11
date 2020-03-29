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
	public Texture car, wheel;
	
	public final TiledMap initMap, lastMap;
	
	public final Array<Array<MapDesc>> mapsByMask = new Array<Array<MapDesc>>();
	
	public final Audio audio;
	
	public final HeroAnimator hero;
	
	public final TextureRegion droneBlue;
	
	public Assets() {
		
		audio = new Audio();
		
		droneBlue = new TextureRegion(new Texture(Gdx.files.internal("sprites/drone-blue.png")));
		
		hero = new HeroAnimator(new Texture(Gdx.files.internal("sprites/hero-south.png")));
		
		/*
		for(int i=0 ; i<4 ; i++){
			loadMap("maps/map" + (i+1) + ".tmx");
		}
		*/
		
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
		
		car = new Texture("sprites/car.png");
		wheel = new Texture("sprites/wheel.png");
	}

	private void loadMap(String filename) {
		MapDesc md = new MapDesc();
		md.name = filename;
		md.map = new TmxMapLoader().load(filename);
		md.mask = MapAnalyser.analyse(md.map);
		maps.add(md);
		mapsByMask.get(md.mask).add(md);
	}

	private TextureRegion getTile(int id) {
		return maps.first().map.getTileSets().getTile(id + 1).getTextureRegion();
	}

	public TextureRegion getWhitePixel() {
		return skin.getRegion("white");
	}
	
	public TextureRegion getDroneRegion(boolean horizontal, boolean vertical, boolean rotative) {
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
	}

	/*
	public TextureRegion getHeroRegion() {
		return getTile(8);
	}
	*/

	public TextureRegion getMedPackRegion() {
		return getTile(4);
	}

	public TextureRegion getFuelRegion() {
		return getTile(5);
	}

	public TextureRegion getLifeRegion() {
		return getMedPackRegion();
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
