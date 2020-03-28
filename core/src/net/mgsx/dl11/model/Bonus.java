package net.mgsx.dl11.model;

abstract public class Bonus extends Entity{

	public boolean active = true;

	public Bonus(int initX, int initY) {
		super(initX, initY);
	}

	abstract public boolean canBeAquired(GameState game);

	abstract public boolean aquire(GameState game);

}
