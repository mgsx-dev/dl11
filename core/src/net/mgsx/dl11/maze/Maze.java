package net.mgsx.dl11.maze;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;

public class Maze {
	public final int w, h;
	
	private final MazeCell[] cells;

	public Maze(int w, int h) {
		super();
		this.w = w;
		this.h = h;
		this.cells = new MazeCell[w*h];
		for(int y=0 ; y<h ; y++)
			for(int x=0 ; x<w ; x++)
				cells[y*w+x] = new MazeCell(x,y);
	}

	public void removeWall(int x, int y, int wall) {
		MazeCell cell = cell(x, y);
		cell.walls[wall] = false;
		MazeCell cell2 = cell(x, y, wall);
		if(cell2 != null){
			cell2.walls[(wall + 2) % 4] = false;
		}
	}
	
	public MazeCell cell(int x, int y, int wall) {
		GridPoint2 delta = MazeCell.DELTAS[wall];
		int x2 = x + delta.x;
		int y2 = y + delta.y;
		MazeCell cell2 = cell(x2, y2);
		return cell2;
	}

	public MazeCell cell(int x, int y) {
		// TODO wrap X, wrap Y
		if(x >= 0 && x < w && y >= 0 && y < h) return cells[y*w+x];
		return null;
	}

	public MazeCell anyCell() {
		return cells[MathUtils.random(w*h-1)];
	}

	public MazeCell neighborUnvisited(MazeCell cell, int direction) {
		MazeCell n = cell(cell.x, cell.y, direction);
		if(n == null || n.visited) return null;
		return n;
	}
	
	
}
