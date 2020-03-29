package net.mgsx.dl11.model;

import com.badlogic.gdx.utils.Align;

import net.mgsx.dl11.assets.Assets;

public class Story {

	// TODO parler du héro, qu'il suporte plus les radiations que les autres...
	// TODO parler de la minimap (key C)
	
	public static String introText() {
		String text = "";
		text += "AB. 239\n";
		text += "239 years after the global thermonuclear war, almost all the planet is hostile.";
		text += "\n";
		text += "Still few humans surviving under the earth and need foods to survive.";
		text += "\n";
		text += "I am one of the volunteer, looking for foods and stuff at earth surface.";
		text += "\n";
		text += "I had an accident with the RV but i managed to fix it.";
		text += "\n";
		text += "Now I have to bring back our only RV to the shelter.";
		text += "\n";
		text += "Nukes powered drones monitor the restricted area, i should take care.";
		return text;
	}
	
	public static void enteringNewTile(GameState game, WorldTile worldTile) {
		
		// TODO maybe filter entities here : only food, only drone, etc...
		
		// TODO handle last tile : with/without the car
		
		if(worldTile.hero.car == null){
			if(!game.changedMapWithoutCar){
				game.changedMapWithoutCar = true;
				String text = "Radiation hurts me, i shouldn't walk around too much...";
				game.storyHandler.spawnText(text, Align.top);
			}
			if(worldTile.isLastTile){
				if(!game.visitedLastTile){
					game.visitedLastTile = true;
					game.storyHandler.spawnText("I found the shelter but i need to bring back the RV now...", Align.top);
				}
				Assets.i.audio.playLastTileNoCar(); // TODO each time or first time only ?
			}
		}else{
			if(!game.changedMapWithCar){
				game.changedMapWithCar = true;
				String text = "Drones seams disabled while i stay in car !";
				game.storyHandler.spawnText(text, Align.top);
			}
			if(worldTile.isLastTile){
				game.gameOver = true;
				game.storyHandler.spawnText("I made it!", Align.top);
				Assets.i.audio.playMusicOutro();
				Assets.i.audio.playLastTileWithCar();
			}
		}
	}

	public static void enteringGame(GameState game) {
		String introText = "";
		introText += "This place have high radiation, i shouldn't get too far";
		introText += "\n";
		introText += "I should get some fuel and med packs around before exploring surroundings.";
		introText += "\n";
		
		game.storyHandler.spawnText(introText, Align.bottom);
	}

	public static void pickupBonus(GameState game, Bonus bonus) {
		if(bonus instanceof MedPack){
			if(!game.getMedpack){
				game.getMedpack = true;
				game.storyHandler.spawnText("This medpack will be helpful", Align.top);
			}
		}
		if(bonus instanceof Fuel){
			if(!game.getFuel){
				game.getFuel = true;
				game.storyHandler.spawnText("I should bring back this fuel to my vehicle", Align.top);
			}
		}
	}

	public static void enteringCar(GameState game) {
		if(!game.enteredCarWithFuel && game.carFuel > 0){
			game.enteredCarWithFuel = true;
			game.storyHandler.spawnText("I fill up the tank now we can move to the target point.\nPRESS X TO EXIT THE CAR", Align.top);
		}
		if(!game.enteredCarWithoutFuel && game.carFuel <= 0){
			game.enteredCarWithoutFuel = true;
			game.storyHandler.spawnText("Tank is empty, i need to find some fuel.\nPRESS X TO EXIT THE CAR", Align.top);
		}
	}

	public static void exitingCar(GameState game) {
		// nothing
	}

	public static void heroFail(GameState game) {
		if(!game.gameOver){
			game.gameOver = true;
			game.storyHandler.spawnText("I failed...", Align.center);
			Assets.i.audio.playHeroDead();
			Assets.i.audio.playMusicFail();
		}
	}
	
}
