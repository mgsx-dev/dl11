package net.mgsx.dl11.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.assets.MapDesc;
import net.mgsx.dl11.maze.MazeCell;
import net.mgsx.dl11.utils.MapAnalyser;
import net.mgsx.dl11.utils.MapUtils;

public class WorldMap {
	private WorldTile [] cells;
	private int w, h;
	private GameState game;
	private final int initX;
	private final int initY;
	
	public WorldMap(GameState game, int w, int h) {
		super();
		this.game = game;
		this.w = w;
		this.h = h;
		cells = new WorldTile[w*h];
		
		initX = 0;
		initY = h/2;
		
		WorldTile initTile = loadTile(initX, initY, Assets.i.initMap);
		initTile.isFirstTile = true;
		WorldTile lastTile = loadTile(w-1, h/2, Assets.i.lastMap);
		lastTile.isLastTile = true;
	}

	private WorldTile getTile(int x, int y){
		return cells[y*w+x];
	}
	
	private WorldTile getTile(int x, int y, int direction){
		if(x>=0 && x<w && y>=0 && y<h){
			WorldTile cell = cells[y*w+x];
			if(cell == null){ 
				cell = loadTile(x, y, direction);
			}
			return cell;
		}
		return null;
		// throw new GdxRuntimeException("tile out of bounds:" + x + "," + y);
	}
	
	private WorldTile loadTile(int x, int y, int direction)
	{
		// TODO load a random map depending on maze config.
		
		int dirInv = (direction + 2) % 4;
		
		int requiredMask = MapAnalyser.directionToMask(dirInv);
		
		TiledMap mapAsset = findMap(requiredMask, 0xf);
		
		return loadTile(x, y, mapAsset);
	}
	

	private WorldTile loadTile(int x, int y, TiledMap mapAsset) {
		
		WorldTile worldTile = new WorldTile(game);
		worldTile.x = x;
		worldTile.y = y;
		
		cells[y*w+x] = worldTile;
		
		worldTile.map = MapUtils.copyMap(mapAsset, false);
		worldTile.map.getLayers().add(MapUtils.copyLayer((TiledMapTileLayer)mapAsset.getLayers().get(0)));
		worldTile.loadCollisions((TiledMapTileLayer)mapAsset.getLayers().get(0));
		worldTile.load((TiledMapTileLayer)mapAsset.getLayers().get(1));
		
		return worldTile;
	}

	private TiledMap findMap(int requiredMask, int allowedMasks) {
		Array<MapDesc> filtered = new Array<MapDesc>();
		for(MapDesc md : Assets.i.maps){
			if((md.mask & requiredMask) == requiredMask){//  && (md.mask & ~allowedMasks) == 0){
				filtered.add(md);
			}
		}
		return filtered.random().map;
	}

	public WorldTile getAdjTile(WorldTile tile, int direction){
		GridPoint2 delta = MazeCell.DELTAS[direction];
		WorldTile adj = getTile(tile.x + delta.x, tile.y + delta.y, direction);
		return adj;
	}

	public WorldTile getInitTile() {
		
		return getTile(initX, initY);
	}
	
}
