package net.mgsx.dl11.model;

public class GameState {
	
	public final StoryHandler storyHandler;
	
	public int heroLife = GameSettings.HERO_LIFE_INIT;
	public int heroFuel = GameSettings.HERO_FUEL_INIT;
	public float carFuel = GameSettings.CAR_FUEL_INIT;
	
	public boolean changedMapWithCar;
	public boolean changedMapWithoutCar;
	public boolean getMedpack;
	public boolean getFuel;

	public boolean enteredCarWithoutFuel;
	public boolean enteredCarWithFuel;
	public boolean gameOver;
	public boolean visitedLastTile;
	
	public GameState(StoryHandler storyHandler) {
		super();
		this.storyHandler = storyHandler;
	}
	
}
