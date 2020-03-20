package net.mgsx.dl11.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import net.mgsx.dl11.assets.Assets;

// TODO lazer shuld be an entity as well (compound and controlled by the drone (same group)
public class Drone extends Entity {

	private boolean horizontal;
	private boolean vertical;
	
	private float timeoutMove = 0;
	
	
	private WorldTile world;
	
	public float speed = 1;
	
	
	private boolean hurstHero[] = new boolean[4];
	
	private int currentDirection;
	private Image sprite;
	private Image lazers[] = new Image[4];
	
	public Drone(WorldTile world, int initX, int initY, boolean horizontal, boolean vertical) {
		super(initX, initY);
		this.world = world;
		this.horizontal = horizontal;
		this.vertical = vertical;
		
		Group group = new Group();
		actor = group;
		
		sprite = new Image(Assets.i.getDroneRegion());
		sprite.setSize(1, 1);
		sprite.setPosition(0, 0, Align.center);
		group.addActor(sprite);
		
		for(int i=0 ; i<4 ; i++){
			lazers[i] = new Image(Assets.i.getWhitePixel());
			group.addActor(lazers[i]);
		}
		
		currentDirection = RIGHT;
	}
	
	@Override
	public void update(float delta) {
		boolean movable = horizontal || vertical;
		boolean hurtPlayer = false;
		for(int i=0 ; i<directions.length ; i++){
			hurstHero[i] = world.rayCast(castByDirection[i], this.position, directions[i], true);
			hurtPlayer |= hurstHero[i];
		}
		if(movable){
			
			// XXX timeoutMove -= delta;
			
			if(castByDirection[currentDirection].dst(position) < 1f || timeoutMove < 0){
				
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
		
		float thickness = .1f;
		
		for(int i=0 ; i<4 ; i++){
			Image lazer = lazers[i];
			// horizontal
				lazer.setPosition(0, 0, Align.left);
				lazer.setSize(position.dst(castByDirection[i]), thickness);
				lazer.setRotation(i * 90 + 180);
				lazer.setColor(1,0,0, hurstHero[i] ? 1 : .3f);
		}
		
		sprite.setColor(hurtPlayer ? Color.ORANGE : Color.WHITE);
		
		
		// apply velocity (clamp if necessary ? )
		position.mulAdd(velocity, delta);
		actor.setPosition(position.x, position.y);
		// TODO set velocity if needed ...
	}

}
