package net.mgsx.dl11.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class UniControl {

	public static final int DOWN = 1, RIGHT = 3, UP = 0, LEFT = 2;
	
	// Layouts ARROWS, WSAD, ZSQD
	private static final int [][] keyLayouts = {
	        {Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT},
	        {Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D},
	        {Input.Keys.Z, Input.Keys.S, Input.Keys.Q, Input.Keys.D},
	};

	public static boolean isPressed(int direction){
		for(int [] keyLayout : keyLayouts){
			if(Gdx.input.isKeyPressed(keyLayout[direction])) return true;
		}
		return false;
	}
	
	public static boolean isJustPressed(int direction){
		for(int [] keyLayout : keyLayouts){
			if(Gdx.input.isKeyJustPressed(keyLayout[direction])) return true;
		}
		return false;
	}

	public static boolean isActionJustPressed(){
		return Gdx.input.isKeyJustPressed(Input.Keys.E);
	}

	public static boolean isOptionJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.R);
	}

}
