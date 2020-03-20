package net.mgsx.dl11.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.model.WorldTile;
import net.mgsx.dl11.utils.MapUtils;
import net.mgsx.dl11.utils.StageScreen;

public class GameScreen extends StageScreen
{
	private static final float WORLD_WIDTH = 24; //768;
	private static final float WORLD_HEIGHT = 18; //576;
	private static final float unitScale = 1f / 32f;

	private OrthogonalTiledMapRenderer mapRenderer;
	
	private WorldTile worldTile;
	private Group entitiesGroup;
	
	public GameScreen() {
		super(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
		
		// XXX demo map
		WorldTile worldTile = new WorldTile();
		this.worldTile = worldTile;
		TiledMap mapAsset = Assets.i.mapPOC;
		
		worldTile.map = MapUtils.copyMap(mapAsset, false);
		worldTile.map.getLayers().add(MapUtils.copyLayer((TiledMapTileLayer)mapAsset.getLayers().get(0)));
		worldTile.loadCollisions((TiledMapTileLayer)mapAsset.getLayers().get(0));
		worldTile.load((TiledMapTileLayer)mapAsset.getLayers().get(1));
		stage.addActor(entitiesGroup = new Group());
		worldTile.getActors(entitiesGroup);
		
		
		mapRenderer = new OrthogonalTiledMapRenderer(worldTile.map, unitScale);
	}
	
	@Override
	public void render(float delta) {
		
		worldTile.update(delta);
		
		clear(Color.BLACK);
		
		// render map here
		mapRenderer.setView((OrthographicCamera)viewport.getCamera());
		mapRenderer.render();
		
		super.render(delta);
	}
}
