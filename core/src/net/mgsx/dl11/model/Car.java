package net.mgsx.dl11.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import net.mgsx.dl11.assets.Assets;

public class Car extends Entity {

	private Image car;
	private Image wheelL;
	private Image wheelR;
	private Group actorH;
	public float width, height;
	public boolean controlEnabled;
	private float time;
	private float wheelAngle;
	private GameState game;

	public Car(GameState game, int initX, int initY) {
		super(initX, initY);
		this.game = game;
		this.width = 3;
		this.height = 2;
		
		actorH = new Group();
		
		
		car = new Image(Assets.i.car);
		wheelL = new Image(Assets.i.wheel);
		wheelR = new Image(Assets.i.wheel);
		
		actorH.addActor(car);
		actorH.addActor(wheelL);
		actorH.addActor(wheelR);
		
		
		Group g = new Group();
		g.addActor(actorH);
		actor = g;
	}
	
	@Override
	public void reset() {
		position.set(initX, initY);
		velocity.setZero();
	}
	
	@Override
	public void update(float delta) {
		time += delta;
		
		actor.setPosition(position.x, position.y + .5f);
		
		
		float carScale = 1f;
		
		car.setSize(carScale * 168f / 64f, carScale * 68f / 64f);
		wheelL.setSize(40f/64f, 40f/64f);
		wheelR.setSize(40f/64f, 40f/64f);
		wheelL.setPosition(.45f, 0, Align.center);
		wheelR.setPosition(2.22f, 0, Align.center);
		
		
		/*
		car.setSize(3, 2);
		wheelL.setSize(40f/64f, 40f/64f);
		wheelR.setSize(40f/64f, 40f/64f);
		wheelL.setPosition(.45f, 0, Align.center);
		wheelR.setPosition(2.22f, 0, Align.center);
		*/
		
		if(controlEnabled && game.carFuel > 0){
			car.setY(MathUtils.sin(time * 30) * .1f);
			
			wheelAngle += -velocity.x * 4;
			
			wheelL.setOrigin(Align.center);
			wheelL.setRotation(wheelAngle);
			
			wheelR.setOrigin(Align.center);
			wheelR.setRotation(wheelAngle);
		}
	}
	
}
