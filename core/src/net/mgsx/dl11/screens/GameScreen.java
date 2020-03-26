package net.mgsx.dl11.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.dl11.maze.MazeCell;
import net.mgsx.dl11.model.GameSettings;
import net.mgsx.dl11.model.WorldMap;
import net.mgsx.dl11.model.WorldTile;
import net.mgsx.dl11.utils.StageScreen;

public class GameScreen extends StageScreen
{
	public static final float WORLD_WIDTH = 24; //768;
	public static final float WORLD_HEIGHT = 18; //576;
	private static final float unitScale = 1f / 32f;

	private OrthogonalTiledMapRenderer mapRenderer;
	
	private WorldMap worldMap;
	private WorldTile worldTile;
	private Group entitiesGroup;
	private WorldTile nextWorldTile;
	private float transition;
	
	public GameScreen() {
		super(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
		
		worldMap = new WorldMap(GameSettings.MAP_WIDTH, GameSettings.MAP_HEIGHT);
		
		stage.addActor(entitiesGroup = new Group());

		worldTile = worldMap.getInitTile();
		
		worldTile.reset();
		
		worldTile.spawnCar();
		
		worldTile.getActors(entitiesGroup);
		
		worldTile.setEntering(MazeCell.EAST);
		worldTile.active = true;
		
		mapRenderer = new OrthogonalTiledMapRenderer(worldTile.map, unitScale);
	}
	
	@Override
	public void render(float delta) {
		
		delta = MathUtils.clamp(delta, 1/120f, 1/30f);
		
		worldTile.update(delta);
		
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
			nextWorldTile.update(delta);
			transition += delta * 1f;
			if(transition >= 1){
				worldTile = nextWorldTile;
				nextWorldTile = null;
				worldTile.active = true;
				transition = 1;
			}
		}
		
		clear(Color.BLACK);
		
		// TODO render into FBO anyway to avoid glitches
		
		// render map here
		mapRenderer.getBatch().setColor(Color.WHITE);
		mapRenderer.setMap(worldTile.map);
		mapRenderer.setView((OrthographicCamera)viewport.getCamera());
		mapRenderer.render();
		
		if(nextWorldTile != null){
			mapRenderer.getBatch().setColor(Color.GREEN);
			mapRenderer.setMap(nextWorldTile.map);
			mapRenderer.setView(viewport.getCamera().combined, (1-transition) * WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT);
			mapRenderer.render();
		}
		
		
		super.render(delta);
	}
}
