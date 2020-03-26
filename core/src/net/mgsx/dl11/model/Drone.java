package net.mgsx.dl11.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.model.Laser.State;

// TODO voir ce qu'il se passe quand le hero est dans la voiture : les drone se déplacenet mais ne tirent pas ?

public class Drone extends Entity {

	private boolean horizontal;
	private boolean vertical;
	private float timeoutMove = 0;
	private WorldTile world;
	public float speed = 1;
	
	private int currentDirection;
	private Image sprite;
	
	public boolean active = true;
	
	private final Array<Laser> lasers = new Array<Laser>();
	
	public Drone(WorldTile world, int initX, int initY, boolean horizontal, boolean vertical) {
		super(initX, initY);
		this.world = world;
		this.horizontal = horizontal;
		this.vertical = vertical;
		
		Group group = new Group();
		actor = group;
		
		sprite = new Image(Assets.i.getDroneRegion(horizontal, vertical));
		sprite.setSize(1, 1);
		sprite.setPosition(0, 0, Align.center);
		
		for(int i=0 ; i<4 ; i++){
			Laser laser = new Laser();
			laser.direction.set(directions[i].x, directions[i].y);
			lasers.add(laser);
			group.addActor(laser.actor);
		}
		group.addActor(sprite);
		
		currentDirection = !horizontal ? RIGHT : DOWN;
	}
	
	@Override
	public void reset() {
		super.reset();
		if(lasers != null){
			for(Laser laser : lasers){
				laser.reset();
				laser.actor.setVisible(true);
			}
		}
	}
	
	public boolean isFiringPlayer() {
		for(Laser laser : lasers){
			if(laser.playerOn && laser.state == State.FIRE) return true;
		}
		return false;
	}
	
	@Override
	public void update(float delta) {
		
		if(!active){
			actor.setPosition(position.x, position.y);
			for(Laser laser : lasers){
				laser.actor.setVisible(false);
			}
			return;
		}else{
			for(Laser laser : lasers){
				laser.actor.setVisible(true);
			}
		}
		
		boolean movable = horizontal || vertical;
		boolean hurtPlayer = false;
		for(Laser laser : lasers){
			hurtPlayer |= laser.playerOn = world.rayCast(laser.wallPosition, this.position, laser.direction, true);
			
		}
		if(movable){
			
			// XXX timeoutMove -= delta;
			
			if(world.clamp(this) || timeoutMove < 0){
				
				// chose another direction
				if(horizontal && vertical){ // XXX
					currentDirection = MathUtils.random(3);
				}else if(horizontal){
					currentDirection = currentDirection == LEFT ? RIGHT : LEFT;
				}else{
					currentDirection = currentDirection == UP ? DOWN : UP;
				}
				
				timeoutMove = MathUtils.random(2f, 4f);
			}
			
			velocity.set(directions[currentDirection].x, directions[currentDirection].y).scl(speed);
			
		}
		
		// TODO update shots (ray cast)
		
		sprite.setColor(hurtPlayer ? Color.ORANGE : Color.WHITE);
		
		
		// apply velocity (clamp if necessary ? )
		position.mulAdd(velocity, delta);
		actor.setPosition(position.x, position.y);
		// TODO set velocity if needed ...
		
		for(Laser laser : lasers){
			laser.update(position, delta);
		}
	}

}
