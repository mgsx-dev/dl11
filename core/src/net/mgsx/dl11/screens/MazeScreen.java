package net.mgsx.dl11.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import net.mgsx.dl11.maze.Maze;
import net.mgsx.dl11.maze.MazeDrawer;
import net.mgsx.dl11.maze.MazeGenerator;

public class MazeScreen extends ScreenAdapter
{
	private Maze maze;
	private MazeDrawer mazeRenderer;

	public MazeScreen() {
		generate();
	}
	
	private void generate() {
		maze = new MazeGenerator().generate(10, 5);
		if(mazeRenderer != null) mazeRenderer.dispose();
		mazeRenderer = new MazeDrawer(maze);
	}
	private void generateDemo() {
		maze = new MazeGenerator().generate(10, 5, .6f);
		if(mazeRenderer != null) mazeRenderer.dispose();
		mazeRenderer = new MazeDrawer(maze);
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
			generate();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
			new MazeGenerator().removeWalls(maze, 1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
			generateDemo();
		}
		Color c = Color.DARK_GRAY;
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mazeRenderer.render();
	}
}
