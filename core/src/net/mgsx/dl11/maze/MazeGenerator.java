package net.mgsx.dl11.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

public class MazeGenerator 
{
	
	public Maze generate(int width, int height){
		final Maze maze = new Maze(width, height);
		generate(maze);
		return maze;
	}
	
	public void generate(Maze maze){
		
		// TODO algo DFS : https://fr.wikipedia.org/wiki/Mod%C3%A9lisation_math%C3%A9matique_de_labyrinthe
		
		Array<MazeCell> stack = new Array<MazeCell>();
		stack.add(maze.anyCell());
		
		IntArray possible = new IntArray(4);
		
		do{
			MazeCell current = stack.peek();
			current.visited = true;
			
			possible.clear();
			for(int i=0 ; i<4 ; i++){
				MazeCell n = maze.neighborUnvisited(current, i);
				if(n != null){
					possible.add(i);
				}
			}
			if(possible.size > 0){
				int wall = possible.random();
				maze.removeWall(current.x, current.y, wall);
				stack.add(maze.cell(current.x, current.y, wall));
			}else{
				stack.pop();
			}
			
		}while(stack.peek() != stack.first());
		
	}
	
	public Maze generate(int width, int height, float wallRate){
		Maze maze = generate(width, height);
		
		Array<Wall> walls = findWalls(maze);
		
		int n = MathUtils.floor(walls.size * (1 - wallRate));
		
		for(int i=0 ; i<n && walls.size > 0 ; i++){
			Wall wall = walls.removeIndex(MathUtils.random(walls.size-1));
			maze.removeWall(wall.x, wall.y, wall.d);
		}
		return maze;
	}
	
	private static class Wall{
		int x, y, d;

		public Wall(int x, int y, int d) {
			super();
			this.x = x;
			this.y = y;
			this.d = d;
		}
		
	}
	
	private Array<Wall> findWalls(Maze maze){
		Array<Wall> walls = new Array<Wall>();
		for(int y=0 ; y<maze.h ; y++){
			for(int x=0 ; x<maze.w ; x++){
				for(int i=0 ; i<2 ; i++){ // only half directions (west and north)
					MazeCell c = maze.cell(x, y, i);
					int i2 = (i+2)%4;
					if(c != null && c.walls[i2]){
						walls.add(new Wall(x,y,i));
					}
				}
			}
		}
		return walls;
	}
	
	public void removeWalls(Maze maze, int n){
		
		// find all walls
		Array<Wall> walls = findWalls(maze);
		
		for(int i=0 ; i<n && walls.size > 0 ; i++){
			Wall wall = walls.removeIndex(MathUtils.random(walls.size-1));
			maze.removeWall(wall.x, wall.y, wall.d);
		}
	}
}
