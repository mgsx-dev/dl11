package net.mgsx.dl11.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import net.mgsx.dl11.maze.MazeCell;
import net.mgsx.dl11.model.WorldTile;

public class MapAnalyser {
	
	public static final int WEST_MASK = 1;
	public static final int NORTH_MASK = 2;
	public static final int EAST_MASK = 4;
	public static final int SOUTH_MASK = 8;

	public static int analyse(Grid2D grid) {
		// find NSEW
		int code = 0;
		for(int i=0 ; i<grid.getWidth() ; i++){
			if(!grid.isObstacle(i, 0)){
				code |= SOUTH_MASK;
			}
			if(!grid.isObstacle(i, grid.getHeight()-1)){
				code |= NORTH_MASK;
			}
		}
		for(int i=0 ; i<grid.getHeight() ; i++){
			if(!grid.isObstacle(0, i)){
				code |= WEST_MASK;
			}
			if(!grid.isObstacle(grid.getWidth()-1, i)){
				code |= EAST_MASK;
			}
		}
		return code;
	}

	public static String asString(int mapCode) {
		String s = "";
		if((mapCode & NORTH_MASK) != 0) s += "N";
		if((mapCode & SOUTH_MASK) != 0) s += "S";
		if((mapCode & EAST_MASK) != 0) s += "E";
		if((mapCode & WEST_MASK) != 0) s += "W";
		return s;
	}

	public static GridPoint2 getEntry(Grid2D grid, int direction) {
		// TODO find the middle cell instead of first
		if(direction == MazeCell.SOUTH){
			for(int i=0 ; i<grid.getWidth() ; i++){
				if(!grid.isObstacle(i, 0)){
					return new GridPoint2(i, 0);
				}
			}
		}
		if(direction == MazeCell.NORTH){
			for(int i=0 ; i<grid.getWidth() ; i++){
				if(!grid.isObstacle(i, grid.getHeight()-1)){
					return new GridPoint2(i, grid.getHeight()-1);
				}
			}
		}
		if(direction == MazeCell.WEST){
			for(int i=0 ; i<grid.getHeight() ; i++){
				if(!grid.isObstacle(0, i)){
					return new GridPoint2(0, i);
				}
			}
		}
		if(direction == MazeCell.EAST){
			for(int i=0 ; i<grid.getHeight() ; i++){
				if(!grid.isObstacle(grid.getWidth()-1, i)){
					return new GridPoint2(grid.getWidth()-1, i);
				}
			}
		}
		throw new GdxRuntimeException("no entry point for direction " + direction);
	}

	public static int directionToMask(int dir) {
		return 1 << dir;
	}

	public static int analyse(TiledMap map) {
		Grid2D colMap = WorldTile.createColMap();
		colMap.createFrom((TiledMapTileLayer)map.getLayers().get(0));
		return analyse(colMap);
	}
}
