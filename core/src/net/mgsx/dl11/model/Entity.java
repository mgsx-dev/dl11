package net.mgsx.dl11.model;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Entity {
	
	public static final int LEFT = 0;
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	public static final GridPoint2 left = new GridPoint2(-1,0);
	public static final GridPoint2 up = new GridPoint2(0,-1);
	public static final GridPoint2 right = new GridPoint2(1,0);
	public static final GridPoint2 down = new GridPoint2(0,1);
	public static final GridPoint2 [] directions = new GridPoint2[]{left, up, right, down};

	public Actor actor;
	
	protected int initX, initY;
	
	public final Vector2 position = new Vector2();
	public final Vector2 velocity = new Vector2();

	public Entity(int initX, int initY) {
		super();
		this.initX = initX;
		this.initY = initY;
		position.set(initX + .5f, initY + .5f);
		velocity.setZero();
	}

	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

}
