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
	
	public WorldMap(GameState game, int w, int h) {
		super();
		this.game = game;
		this.w = w;
		this.h = h;
		cells = new WorldTile[w*h];
	}
	
	private WorldTile getTile(int x, int y, int direction){
		if(x>=0 && x<w && y>=0 && y<h){
			WorldTile cell = cells[y*w+x];
			if(cell == null){ // XXX force reload because of some early bugs
				cell = cells[y*w+x] = loadTile(x, y, direction);
			}
			return cell;
		}
		return null;
		// throw new GdxRuntimeException("tile out of bounds:" + x + "," + y);
	}
	
	private WorldTile loadTile(int x, int y, int direction)
	{
		// TODO load a random map depending on maze config.
		
		// XXX load depending on back direction
		TiledMap mapAsset;
		
		if(direction < 0){
			mapAsset = findMap(0xf, 0xf);
		}else{
			
			int dirInv = (direction + 2) % 4;
			
			int requiredMask = MapAnalyser.directionToMask(dirInv);
			
			mapAsset = findMap(requiredMask, 0xf);
		}
		
		// XXX demo map
		WorldTile worldTile = new WorldTile(game);
		worldTile.x = x;
		worldTile.y = y;
		
		worldTile.map = MapUtils.copyMap(mapAsset, false);
		worldTile.map.getLayers().add(MapUtils.copyLayer((TiledMapTileLayer)mapAsset.getLayers().get(0)));
		worldTile.loadCollisions((TiledMapTileLayer)mapAsset.getLayers().get(0));
		worldTile.load((TiledMapTileLayer)mapAsset.getLayers().get(1));
		
		int mapCode = MapAnalyser.analyse(worldTile.getGrid());
		System.out.println(MapAnalyser.asString(mapCode));
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
		
		return getTile(0, GameSettings.MAP_HEIGHT/2, -1);
	}
	
}
