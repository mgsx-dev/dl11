package net.mgsx.dl11.maze;

import com.badlogic.gdx.math.GridPoint2;

public class MazeCell {
	public static final int WEST = 0;
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;
	
	public static final GridPoint2 [] DELTAS = {
			new GridPoint2(-1, 0),
			new GridPoint2(0, 1),
			new GridPoint2(1, 0),
			new GridPoint2(0, -1)			
	};
	
	public static final GridPoint2 [] DELTAS_YUP = {
			new GridPoint2(-1, 0),
			new GridPoint2(0, -1),
			new GridPoint2(1, 0),
			new GridPoint2(0, 1)			
	};
	
	public final int x, y;
	
	boolean visited;
	
	public boolean [] walls = new boolean[4];

	public MazeCell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		for(int i=0 ; i<4 ; i++) walls[i] = true;
	}
	
}
