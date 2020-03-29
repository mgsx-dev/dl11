package net.mgsx.dl11.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

public class GameSettings 
{
	public static boolean DEBUG = true; // TODO false for prod
	public static final ObjectMap<String, String> debugOptions = new ObjectMap<String, String>();
	public static String[] arguments;
	
	public static final int HUD_WIDTH = GameSettings.WORLD_WIDTH * 16;
	public static final int HUD_HEIGHT = GameSettings.WORLD_HEIGHT * 16;
	
	public static int MAP_WIDTH = 10; // XXX 10;
	public static int MAP_HEIGHT = 5;
	
	public static int HERO_LIFE_INIT = 5; // XXX
	public static int HERO_LIFE_MAX = 10;
	public static int HERO_LIFE_ALARM = 3;
	
	public static int HERO_FUEL_MAX = 10;
	public static int HERO_FUEL_INIT = 0; // XXX
	
	public static int MEDPACK_LIFE = 1;

	public static float CAR_FUEL_CONSUMPTION_PER_METER = .1f;

	public static int CAR_FUEL_INIT = 0;
	public static int CAR_FUEL_MAX = 10;
	
	public static int DRONE_DAMAGES = 1;
	public static int NEXT_TILE_DAMAGES = 1;
	
	public static float LASER_ROTATION_SPEED = 20;
	
	public static boolean ALL_LASERS_FIRING = true;
	public static float MAZE_WALLS_RATE = .4f;
	
	public static boolean DISPLAY_UNVISITED_MAZE_CELL = false;
	public static float CAR_SPEED = 1;
	public static float HERO_SPEED = 6;
	
	public static float TILE_TRANSITION_SPEED = 3f;
	
	public static int BONUS_RESPAWN_TICKS = 5;

	
	public static float MUSIC_VOLUME = 1f;
	public static float SFX_VOLUME = 1f;
	
	
	public static final int WORLD_WIDTH = 24; //768;
	public static final int WORLD_HEIGHT = 18; //576;
	
	public static void init() {
		if(DEBUG && arguments.length > 0){
			try {
				PropertiesUtils.load(GameSettings.debugOptions, Gdx.files.local(arguments[0]).reader());
				patchFields();
			} catch (IOException e) {
				throw new GdxRuntimeException(e);
			}
		}
	}

	private static void patchFields() {
		for(Field field : GameSettings.class.getDeclaredFields()){
			int m = field.getModifiers();
			if(!Modifier.isFinal(m) && Modifier.isStatic(m) && Modifier.isPublic(m)){
				String strVal = debugOptions.get(field.getName());
				if(strVal != null){
						try {
							if(field.getType() == float.class) field.setFloat(null, Float.parseFloat(strVal));
							if(field.getType() == int.class) field.setInt(null, Integer.parseInt(strVal));
							if(field.getType() == boolean.class) field.setBoolean(null, Boolean.parseBoolean(strVal));
							if(field.getType() == float.class) field.setFloat(null, Float.parseFloat(strVal));
							if(field.getType() == float.class) field.setFloat(null, Float.parseFloat(strVal));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
				}
			}
		}
	}

}
