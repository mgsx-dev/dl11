package net.mgsx.dl11.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
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

	public static Vector2 getEntry(Grid2D grid, int direction) {
		// TODO find the middle cell instead of first
		if(direction == MazeCell.SOUTH){
			return getEntry(grid, 0, 0, 1, 0);
		}
		if(direction == MazeCell.NORTH){
			return getEntry(grid, 0, grid.getHeight()-1, 1, 0);
		}
		if(direction == MazeCell.WEST){
			return getEntry(grid, 0, 0, 0, 1);
		}
		if(direction == MazeCell.EAST){
			return getEntry(grid, grid.getWidth()-1, 0, 0, 1);
		}
		throw new GdxRuntimeException("no entry point for direction " + direction);
	}
	
	private static Vector2 getEntry(Grid2D grid, int ix, int iy, int dx, int dy){
		GridPoint2 start = null, end = null;
		for(int x=ix, y=iy ; x>=0 && x<grid.getWidth() && y>=0 && y<grid.getHeight() ; x+=dx, y+=dy){
			if(start == null){
				if(!grid.isObstacle(x, y)){
					start = new GridPoint2(x, y);
					end = new GridPoint2(x, y);
				}
			}else{
				if(grid.isObstacle(x, y)){
					break;
				}
				end.set(x, y);
			}
		}
		if(start == null) throw new GdxRuntimeException("no entry found");
		float fx = (float)(start.x + end.x) / 2f + .5f;
		float fy = (float)(start.y + end.y) / 2f + .5f;
		return new Vector2(fx, fy);
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
