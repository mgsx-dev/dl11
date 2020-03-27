package net.mgsx.dl11.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.dl11.DL11Game;
import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.model.GameSettings;
import net.mgsx.dl11.model.GameState;
import net.mgsx.dl11.model.Story;
import net.mgsx.dl11.model.StoryHandler;
import net.mgsx.dl11.model.WorldMap;
import net.mgsx.dl11.model.WorldTile;
import net.mgsx.dl11.ui.Dialogs;
import net.mgsx.dl11.ui.HUD;
import net.mgsx.dl11.utils.StageScreen;

public class GameScreen extends StageScreen implements StoryHandler
{
	private static final float unitScale = 1f / 32f;

	private OrthogonalTiledMapRenderer mapRenderer;
	
	private WorldMap worldMap;
	private WorldTile worldTile;
	private Group entitiesGroup;
	private WorldTile nextWorldTile;
	private float transition;
	private GameState game;
	private HUD hud;
	private float fadeOutTime;
	private Stage gameStage;
	private boolean locked;
	
	public GameScreen() {
		super(new FitViewport(GameSettings.HUD_WIDTH, GameSettings.HUD_HEIGHT));
		
		gameStage = new Stage(new FitViewport(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT));
		
		game = new GameState(this);
		
		worldMap = new WorldMap(game, GameSettings.MAP_WIDTH, GameSettings.MAP_HEIGHT);
		
		gameStage.addActor(entitiesGroup = new Group());
		
		gameStage.addActor(hud = new HUD(game, Assets.i.skin));

		worldTile = worldMap.getInitTile();
		
		worldTile.reset();
		
		worldTile.spawnCar();
		
		worldTile.getActors(entitiesGroup);
		
		// worldTile.setEntering(MazeCell.EAST);
		worldTile.active = true;
		
		mapRenderer = new OrthogonalTiledMapRenderer(worldTile.map, unitScale);
		
		Story.enteringGame(game);
	}
	
	@Override
	public void spawnText(String text){
		locked = true;
		Dialogs.spawnInfo(stage, text, Align.top, ()->unlock());
	}
	
	private void unlock(){
		locked = false;
	}
	
	@Override
	public void render(float delta) {
		
		delta = MathUtils.clamp(delta, 1/120f, 1/30f);
		
		float gameDelta = locked ? 0 : delta;
		
		worldTile.noInput = locked;
		
		boolean isDead = game.heroLife <= 0;
		
		if(isDead){
			fadeOutTime += delta;
			if(fadeOutTime > 1){
				fadeOutTime = 1;
				Story.heroFail(game);
			}
		}else{
			worldTile.update(gameDelta);
		}
		
		if(nextWorldTile == null && worldTile.exiting){
			// TODO paint in FBO for transitions ?

			int dir = worldTile.exitDirection;
			int dirInv = (dir + 2) % 4;

			nextWorldTile = worldMap.getAdjTile(worldTile, dir);
			
			// XXX temporary test, should'nt happens with real maps
			if(nextWorldTile != null){
				
				nextWorldTile.transfert(worldTile);
				
				worldTile.reset();
				
				entitiesGroup.clearChildren();
				
				
				nextWorldTile.getActors(entitiesGroup);
				
				nextWorldTile.setEntering(dirInv);
				
				transition = 0;
			}
			
		}
		
		if(nextWorldTile != null){
			worldTile.noInput = true;
			nextWorldTile.update(gameDelta);
			transition += gameDelta * 1f;
			if(transition >= 1){
				worldTile = nextWorldTile;
				nextWorldTile = null;
				worldTile.active = true;
				transition = 1;
				
				// only remove life if not in car TODO sauf si on change un peu le gameplay vis à vis de l'histoire.
				if(worldTile.hero.car == null){
					game.heroLife -= GameSettings.NEXT_TILE_DAMAGES;
				}
				
				Story.enteringNewTile(game, worldTile);
			}
		}
		
		clear(Color.BLACK);
		
		// TODO render into FBO anyway to avoid glitches
		gameStage.getViewport().apply();
		
		// render map here
		float lum = isDead ? 1 - fadeOutTime : 1;
		mapRenderer.getBatch().setColor(lum, lum, lum, 1);
		mapRenderer.setMap(worldTile.map);
		mapRenderer.setView((OrthographicCamera)gameStage.getViewport().getCamera());
		mapRenderer.render();
		
		if(nextWorldTile != null){
			mapRenderer.getBatch().setColor(Color.GREEN);
			mapRenderer.setMap(nextWorldTile.map);
			mapRenderer.setView(gameStage.getViewport().getCamera().combined, (1-transition) * GameSettings.WORLD_WIDTH, 0, GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
			mapRenderer.render();
		}
		
		
		gameStage.act();
		gameStage.draw();
		
		stage.getViewport().apply();
		super.render(delta);
		
		if(game.gameOver && !locked){
			DL11Game.i().setScreen(new MenuScreen());
		}
	}
	
	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().update(width, height);
		super.resize(width, height);
	}
}
