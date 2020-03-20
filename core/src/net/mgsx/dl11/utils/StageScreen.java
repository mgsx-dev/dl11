package net.mgsx.dl11.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

abstract public class StageScreen extends ScreenAdapter
{
	protected Stage stage;
	protected Viewport viewport;
	
	public StageScreen() 
	{
		this(new ScreenViewport());
	}
	public StageScreen(Viewport viewport) 
	{
		this.viewport = viewport;
		stage = new Stage(viewport);
	}
	
	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		super.hide();
	}
	
	protected void clear(Color c) {
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}