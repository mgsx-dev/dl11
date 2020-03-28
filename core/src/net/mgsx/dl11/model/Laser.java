package net.mgsx.dl11.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.mgsx.dl11.assets.Assets;

public class Laser {
	
	public Image actor;
	public Vector2 direction = new Vector2();
	public Vector2 wallPosition = new Vector2();
	public boolean playerOn;
	
	public static enum State{
		OFF, IDLE, BLINK, FIRE
	}
	public State state;
	private float time;
	public boolean rotative;
	
	public Laser() {
		actor = new Image(Assets.i.getWhitePixel());
		state = State.IDLE;
	}
	
	public void update(Vector2 center, float delta) {
		time += delta;
		
		if(rotative) direction.rotate(delta * GameSettings.LASER_ROTATION_SPEED);
		
		// update state
		if(state == State.BLINK){
			if((time * 10) % 1f > .5f){
				actor.setColor(1,0,0,.5f);
			}else{
				actor.setColor(0,0,0,0);
			}
			if(time > 1){
				time = 0;
				state = State.FIRE;
			}
			
		}else if(state == State.FIRE){
			if(playerOn) time = 0;
			actor.setColor(1,0,0,1f);
			if(time > 3){
				time = 0;
				state = State.IDLE;
			}
		}else if(state == State.OFF){
			actor.setColor(1,0,0,0f);
		}else if(state == State.IDLE){
			if(playerOn){
				state = State.BLINK;
				time = 0;
			}
			// actor.setColor(1,0,0, playerOn ? 1 : .3f);
			actor.setColor(1,0,0,.3f);
		}
		
		
		float thickness = state == State.FIRE ? .3f : .2f;
		actor.setSize(center.dst(wallPosition), thickness);
		actor.setPosition(0, -actor.getHeight()/2);
		actor.setOrigin(0, actor.getHeight()/2);
		actor.setRotation(direction.angle());
		
		

	}

	public void reset() {
		playerOn = false;
		state = State.IDLE;
	}

}
