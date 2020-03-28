package net.mgsx.dl11.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.assets.MapDesc;
import net.mgsx.dl11.maze.Maze;
import net.mgsx.dl11.maze.MazeCell;
import net.mgsx.dl11.maze.MazeGenerator;
import net.mgsx.dl11.utils.MapAnalyser;
import net.mgsx.dl11.utils.MapUtils;

public class WorldMap {
	private final WorldTile [] cells;
	private final int w, h;
	private final GameState game;
	public final int initX;
	public final int initY;
	public final int lastX;
	public final int lastY;
	public final Maze maze;
	
	public WorldMap(GameState game, int w, int h) {
		super();
		this.game = game;
		this.w = w;
		this.h = h;
		cells = new WorldTile[w*h];
		
		initX = 0;
		initY = h/2;
		
		lastX = w-1;
		lastY = h/2; // TODO random ?
		
		WorldTile initTile = loadTile(initX, initY, Assets.i.initMap, "initialMap");
		initTile.isFirstTile = true;
		WorldTile lastTile = loadTile(lastX, lastY, Assets.i.lastMap, "lastMap");
		lastTile.isLastTile = true;
		
		MazeGenerator generator = new MazeGenerator();
		maze = generator.generate(w, h, 1 - GameSettings.MAZE_WALLS_RATE);
		
		maze.removeWall(initX, initY, MazeCell.EAST);
		maze.removeWall(initX, initY, MazeCell.NORTH);
		maze.removeWall(initX, initY, MazeCell.SOUTH);
		
		maze.removeWall(lastX, lastY, MazeCell.WEST);
		maze.removeWall(lastX, lastY, MazeCell.NORTH);
		maze.removeWall(lastX, lastY, MazeCell.SOUTH);
		
		for(int y=0 ; y<h ; y++){
			for(int x=0 ; x<w ; x++){
				if(cells[y*w+x] == null){
					MazeCell mc = maze.cell(x, y);
					int mask = 0;
					if(!mc.walls[MazeCell.NORTH]) mask |= MapAnalyser.NORTH_MASK;
					if(!mc.walls[MazeCell.SOUTH]) mask |= MapAnalyser.SOUTH_MASK;
					if(!mc.walls[MazeCell.EAST]) mask |= MapAnalyser.EAST_MASK;
					if(!mc.walls[MazeCell.WEST]) mask |= MapAnalyser.WEST_MASK;
					
					MapDesc md = Assets.i.mapsByMask.get(mask).random();
					loadTile(x, y, md.map, md.name);
				}
			}
		}
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
		
		return loadTile(x, y, mapAsset, "not defined");
	}
	

	private WorldTile loadTile(int x, int y, TiledMap mapAsset, String name) {
		
		WorldTile worldTile = new WorldTile(game);
		worldTile.name = name;
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
		String debugMap = GameSettings.debugOptions.get("map");
		if(debugMap != null){
			WorldTile tile = loadTile(w/2, h/2, Assets.i.getMap(debugMap), debugMap);
			// tile.isFirstTile = true;
			return tile;
		}
		return getTile(initX, initY);
	}

	public WorldTile getCell(int x, int y) {
		return cells[y*w+x];
	}
	
}
