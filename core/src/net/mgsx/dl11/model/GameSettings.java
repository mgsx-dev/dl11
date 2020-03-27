package net.mgsx.dl11.model;

public class GameSettings {
	public static final float HUD_WIDTH = GameSettings.WORLD_WIDTH * 16;
	public static final float HUD_HEIGHT = GameSettings.WORLD_HEIGHT * 16;
	
	public static int MAP_WIDTH = 5; // XXX 10;
	public static int MAP_HEIGHT = 5;
	
	public static int HERO_LIFE_INIT = 5; // XXX
	public static int HERO_LIFE_MAX = 10;
	
	public static int HERO_FUEL_MAX = 10;
	public static int HERO_FUEL_INIT = 0; // XXX
	
	public static int MEDPACK_LIFE = 1;

	public static float CAR_FUEL_CONSUMPTION_PER_METER = .1f;

	public static int CAR_FUEL_INIT = 0;
	public static int CAR_FUEL_MAX = 10;
	
	public static int DRONE_DAMAGES = 1;
	public static int NEXT_TILE_DAMAGES = 1;
	public static final float WORLD_WIDTH = 24; //768;
	public static final float WORLD_HEIGHT = 18; //576;
	
}
