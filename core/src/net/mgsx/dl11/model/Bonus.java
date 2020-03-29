package net.mgsx.dl11.model;

abstract public class Bonus extends Entity{

	public boolean active = true;
	
	public int lastTick;

	public Bonus(int initX, int initY) {
		super(initX, initY);
	}

	abstract public boolean canBeAquired(GameState game);

	abstract public boolean aquire(GameState game);

	public void respawn(GameState game) {
		if(!active && game.ticks >= lastTick + GameSettings.BONUS_RESPAWN_TICKS){
			active = true;
			// actor.setVisible(true);
			setClosed();
		}
	}
	
	abstract protected void setClosed();
	abstract protected void setOpened();

	protected void unspawn(GameState game) {
		active = false;
		// actor.setVisible(false);
		setOpened();
		lastTick = game.ticks;
	}

}
