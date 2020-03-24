package net.mgsx.dl11.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MazeDrawer implements Disposable {

	private ShapeRenderer shapes;
	private Viewport viewport;
	private Maze maze;
	
	private final static Rectangle r = new Rectangle();
	
	public MazeDrawer(Maze maze) {
		this.maze = maze;
		shapes = new ShapeRenderer();
		viewport = new FitViewport(maze.w, maze.h);
	}
	
	public void render(){
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		shapes.setProjectionMatrix(viewport.getCamera().combined);
		shapes.setColor(Color.ORANGE);
		shapes.begin(ShapeType.Filled);
		final float baseSize = .5f;
		final float wallSize = .4f;
		for(int y=0 ; y<maze.h ; y++){
			for(int x=0 ; x<maze.w ; x++){
				MazeCell cell = maze.cell(x, y);
				r.setSize(baseSize).setCenter(x + .5f, y + .5f);
				shapes.rect(r.x, r.y, r.width, r.height);
				for(int i=0 ; i<4 ; i++){
					if(!cell.walls[i]){
						GridPoint2 delta = MazeCell.DELTAS[i];
						r.setSize(wallSize).setCenter(x + .5f + delta.x * baseSize/2, y + .5f + delta.y*baseSize/2);
						shapes.rect(r.x, r.y, r.width, r.height);
					}
				}
			}
		}
		shapes.end();
	}

	@Override
	public void dispose() {
		shapes.dispose();
	}
}
