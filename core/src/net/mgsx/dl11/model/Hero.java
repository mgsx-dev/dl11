package net.mgsx.dl11.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.utils.UniControl;

public class Hero extends Entity
{

	private WorldTile world;
	private Group group;
	private Image sprite;
	public boolean fired;
	private float blinkTimeout;
	private float time;
	public int life = 10; // TODO
	public boolean controlEnabled = true;

	public Hero(WorldTile world, int initX, int initY) {
		super(initX, initY);
		this.world = world;
		actor = group = new Group();
		sprite = new Image(Assets.i.getHeroRegion());
		sprite.setSize(1, 1);
		sprite.setPosition(0, 0, Align.center);
		group.addActor(sprite);
	}
	
	private static final Vector2 v2 = new Vector2();
	
	public boolean isGhost(){
		return blinkTimeout > 0;
	}
	
	@Override
	public void update(float delta) {
		
		time += delta;
		
		if(fired){
			if(blinkTimeout <= 0){
				blinkTimeout = 3;
				life--;
				System.out.println("Lost life: " + life);
			}
		}
		
		if(blinkTimeout > 0){
			blinkTimeout -= delta;
			if(blinkTimeout <= 0){
				// ? sound
			}
		}
		
		velocity.setZero();
		if(controlEnabled){
			if(UniControl.isPressed(UniControl.RIGHT)){
				velocity.add(1, 0);
			}
			if(UniControl.isPressed(UniControl.LEFT)){
				velocity.add(-1, 0);
			}
			if(UniControl.isPressed(UniControl.UP)){
				velocity.add(0, 1);
			}
			if(UniControl.isPressed(UniControl.DOWN)){
				velocity.add(0, -1);
			}
		}
		float speed = 6;
		velocity.nor().scl(speed);
		
		position.mulAdd(velocity, delta);
		
		
		if(world.clamp(this)){
			sprite.setColor(Color.GOLD);
		}else{
			sprite.setColor(Color.WHITE);
		}
		
		if(blinkTimeout > 0){
			sprite.getColor().a = (time * 10) % 1f > .8f ? 1 : .2f;
		}
		
		
		/*
		for(int i=0 ; i<directions.length ; i++){
			world.rayCast(castByDirection[i], this.position, directions[i], false);
			if(castByDirection[i].dst(position) < 1f){
				// velocity.setZero();
				if(i==LEFT || i==RIGHT) position.x = v2.x;
				if(i==UP || i==DOWN) position.y = v2.y;
				// position.set(v2);
			}
		}
		
		*/
		actor.setPosition(position.x, position.y);
	}
	
}
