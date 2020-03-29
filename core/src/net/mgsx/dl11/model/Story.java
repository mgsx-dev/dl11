package net.mgsx.dl11.model;

import com.badlogic.gdx.utils.Align;

import net.mgsx.dl11.assets.Assets;

public class Story {

	// TODO parler du héro, qu'il suporte plus les radiations que les autres...
	// TODO parler de la minimap (key C)
	
	public static String introText() {
		String text = "";
		text += "{COLOR=black}AB. 239{CLEARCOLOR}\n";
		text += "239 years after the global thermonuclear war, almost all the planet has been wiped out.";
		text += " Only few humans communities survive underground.";
		text += "\n";
		text += "Due to my condition, I'm the one in charge of searching the surface for food, mecanic pieces, and anything helpful for my people.";
		text += "\n";
		text += "\n";
		text += "I just managed to fix our home made RV, we made it out of nothing and that's a miracle it still rolling.";
		text += " Now I have to bring it back to the shelter.";
		text += "\n";
		text += "\n";
		text += "Nukes powered drones monitor areas all around, those vestiges of the War still dangerous, i should take care.";
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
					game.storyHandler.spawnText("Good! I found the shelter but i need to bring back the RV now.", Align.top);
				}
				Assets.i.audio.playLastTileNoCar();
			}
		}else{
			if(!game.changedMapWithCar){
				game.changedMapWithCar = true;
				String text = "Drones are disabled while i stay in car, thanks to our home made lead shield";
				game.storyHandler.spawnText(text, Align.top);
			}
			if(worldTile.isLastTile){
				game.gameOver = true;
				String text = "I made it... finally.\n";
				text += "Some people would say:\n";
				text += "Great Game!\n";
				text += "Congratulation!\n";
				text += "Game Over!\n";
				text += "... but that's simply my every day job ...";
				
				game.storyHandler.spawnText(text, Align.top);
				Assets.i.audio.stopMusic();
				Assets.i.audio.playLastTileWithCar();
			}
		}
	}

	public static void enteringGame(GameState game) {
		String introText = "";
		introText += "This place have high radiation, i shouldn't get too far";
		introText += "\n";
		introText += "I should get some fuel and med packs around before exploring the surroundings.";
		introText += "\n";
		introText += "Oh! By {COLOR=orange}pressing R{CLEARCOLOR} I could open my Radar-Map to see where is the shelter, it should be on the east if i'm not wrong.";
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
			game.storyHandler.spawnText("I fill up the tank now we can move to the shelter.\n{COLOR=orange}PRESS E{CLEARCOLOR} to enter/exit the RV.", Align.top);
		}
		if(!game.enteredCarWithoutFuel && game.carFuel <= 0){
			game.enteredCarWithoutFuel = true;
			game.storyHandler.spawnText("Tank is empty, i need to find some fuel.\n{COLOR=orange}PRESS E{CLEARCOLOR} to enter/exit the RV.", Align.top);
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
			// Assets.i.audio.playMusicFail();
		}
	}
	
}
