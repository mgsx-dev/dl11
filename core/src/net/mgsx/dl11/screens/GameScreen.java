package net.mgsx.dl11.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.dl11.DL11Game;
import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.maze.MazeDrawer;
import net.mgsx.dl11.model.Drone;
import net.mgsx.dl11.model.Entity;
import net.mgsx.dl11.model.GameSettings;
import net.mgsx.dl11.model.GameState;
import net.mgsx.dl11.model.Story;
import net.mgsx.dl11.model.StoryHandler;
import net.mgsx.dl11.model.WorldMap;
import net.mgsx.dl11.model.WorldTile;
import net.mgsx.dl11.ui.Dialogs;
import net.mgsx.dl11.ui.HUD;
import net.mgsx.dl11.utils.StageScreen;
import net.mgsx.dl11.utils.UniControl;

public class GameScreen extends StageScreen implements StoryHandler
{
	public static final float unitScale = 1f / 32f;

	private OrthogonalTiledMapRenderer mapRenderer;
	
	private WorldMap worldMap;
	private WorldTile worldTile;
	private Group entitiesGroup;
	private WorldTile nextWorldTile;
	private float transition;
	private GameState game;
	private HUD hud;
	private float fadeOutTime;
	private Stage gameStage;
	private boolean locked;
	private boolean renderMaze;
	private MazeDrawer mazeRenderer;
	private float time;
	private Image frontDrop;
	private Matrix4 fboMatrix = new Matrix4();
	private FrameBuffer fbo;

	
	// TODO once per map, no per tile !
			// FBO Cache
			/*
			int fboW = GameSettings.WORLD_WIDTH * 32;
			int fboH = GameSettings.WORLD_HEIGHT * 32;
			FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, fboW, fboH, false);
			OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(worldTile.map, GameScreen.unitScale);
			FitViewport vp = new FitViewport(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
			vp.update(fboW, fboH);
			fbo.begin();
			renderer.setView((OrthographicCamera)vp.getCamera());
			renderer.render();
			fbo.end();
			renderer.dispose();
			*/
	
	public GameScreen() {
		super(new FitViewport(GameSettings.HUD_WIDTH, GameSettings.HUD_HEIGHT));
		
		int fboW = GameSettings.WORLD_WIDTH * 32;
		int fboH = GameSettings.WORLD_HEIGHT * 32;
		fbo = new FrameBuffer(Format.RGBA8888, fboW, fboH, false);
		fboMatrix.setToOrtho2D(0, 0, GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
		
		// gameStage = new Stage(new PixelPerfectViewport(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT, 32));
		gameStage = new Stage(new FitViewport(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT));
		
		game = new GameState(this);
		
		worldMap = new WorldMap(game, GameSettings.MAP_WIDTH, GameSettings.MAP_HEIGHT);
		
		mazeRenderer = new MazeDrawer(worldMap.maze){
			@Override
			public Color getCellColor(int x, int y) {
				WorldTile cell = worldMap.getCell(x, y);
				if(x == worldTile.x && y == worldTile.y && MathUtils.floor(time * 4) % 2 == 0) return Color.YELLOW;
				if(x == worldMap.lastX && y == worldMap.lastY) return Color.RED;
				if(!cell.visited && !GameSettings.DISPLAY_UNVISITED_MAZE_CELL) return null;
				if(cell.car != null) return Color.GRAY;
				return super.getCellColor(x, y);
			}
			@Override
			protected void extraDrawing(ShapeRenderer shapes, int x, int y) {
				WorldTile cell = worldMap.getCell(x, y);
				if(cell.car != null){
					shapes.setColor(Color.YELLOW);
					shapes.circle(x+.5f, y+.5f, .20f, 36);
				}
			}
		};
		
		gameStage.addActor(entitiesGroup = new Group());
		
		gameStage.addActor(hud = new HUD(game, Assets.i.skin));

		worldTile = worldMap.getInitTile();
		worldTile.visited = true;
		
		worldTile.reset();
		
		if(worldTile.isFirstTile) worldTile.spawnCar();
		
		worldTile.getActors(entitiesGroup);
		
		// worldTile.setEntering(MazeCell.EAST);
		worldTile.active = true;
		
		mapRenderer = new OrthogonalTiledMapRenderer(worldTile.map, unitScale);
		
		frontDrop = new Image(Assets.i.skin.newDrawable("white", Color.BLACK));
		frontDrop.setFillParent(true);
		gameStage.addActor(frontDrop);
		
		Story.enteringGame(game);
	}
	
	@Override
	public void dispose() {
		fbo.dispose();
		gameStage.dispose();
		mapRenderer.dispose();
		super.dispose();
	}
	
	@Override
	public void show() {
		Assets.i.audio.playMusicHero();
		super.show();
	}
	
	@Override
	public void spawnText(String text, int align){
		if(GameSettings.debugOptions.containsKey("skipText")) return;
		locked = true;
		Dialogs.spawnInfo(stage, text, align, ()->unlock());
	}
	
	private void unlock(){
		locked = false;
	}
	
	@Override
	public void render(float delta) {
		
		time += delta;
		
		if(UniControl.isOptionJustPressed()){
			if(renderMaze){
				renderMaze = false;
				locked = false;
				Assets.i.audio.playMapClose();
			}else if(!locked){ // deny show map when messages
				renderMaze = true;
				locked = true;
				Assets.i.audio.playMapOpen();
			}
		}
		
		delta = MathUtils.clamp(delta, 1/120f, 1/30f);
		
		float gameDelta = locked ? 0 : delta;
		
		worldTile.noInput = locked;
		
		boolean isDead = game.heroLife <= 0;
		
		if(isDead){
			fadeOutTime += delta;
			if(fadeOutTime > 1){
				fadeOutTime = 1;
				Story.heroFail(game);
			}
			Assets.i.audio.clearDrones();
		}else{
			worldTile.update(gameDelta);
		}
		
		if(nextWorldTile == null && worldTile.exiting && !GameSettings.debugOptions.containsKey("map")){
			// TODO paint in FBO for transitions ?

			int dir = worldTile.exitDirection;

			nextWorldTile = worldMap.getAdjTile(worldTile, dir);
			
			// XXX temporary test, should'nt happens with real maps
			if(nextWorldTile != null){
				
				Assets.i.audio.clearDrones();
				
				worldTile.active = false;
				nextWorldTile.active = false;
				
				transition = 0;
			}
			
		}
		
		if(nextWorldTile != null){
			worldTile.noInput = true;
			// nextWorldTile.update(gameDelta);
			transition += gameDelta * GameSettings.TILE_TRANSITION_SPEED;
			if(transition >= 1){
				
				int dir = worldTile.exitDirection;
				int dirInv = (dir + 2) % 4;
				
				nextWorldTile.transfert(worldTile);
				
				worldTile.reset();
				
				entitiesGroup.clearChildren();
				
				if(GameSettings.DEBUG){
					System.out.println("enter map: " + nextWorldTile.name);
				}
				
				game.ticks++;
				
				nextWorldTile.setEntering(dirInv);
				
				nextWorldTile.getActors(entitiesGroup);
				
				
				worldTile = nextWorldTile;
				nextWorldTile = null;
				
				transition = 1;
				
				// only remove life if not in car TODO sauf si on change un peu le gameplay vis à vis de l'histoire.
				if(worldTile.hero.car == null){
					Assets.i.audio.playHeroHurtByRadiation();
					game.heroLife -= GameSettings.NEXT_TILE_DAMAGES;
				}
				
			}
		}
		else if(transition > 0){
			transition -= gameDelta * GameSettings.TILE_TRANSITION_SPEED;
			if(transition <= 0){
				transition = 0;
				
				worldTile.active = true;
				
				Story.enteringNewTile(game, worldTile);

				worldTile.visited = true;
			}
		}
		
		clear(Color.BLACK);
		
		// TODO render into FBO anyway to avoid glitches
		
		// render map here
		
		float lum = isDead ? 1 - fadeOutTime : 1 - transition;
		if(renderMaze) lum = .3f;
		frontDrop.getColor().a = 1 - lum;
		frontDrop.setVisible(lum < 1);
		
		boolean FBOMODE = true;
		
		if(FBOMODE){
			
			fbo.begin();
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			mapRenderer.getBatch().setColor(Color.WHITE);
			mapRenderer.setMap(worldTile.map);
			mapRenderer.setView(fboMatrix, 0, 0, GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
			mapRenderer.render();
			fbo.end();
		}else{
			gameStage.getViewport().apply();
			mapRenderer.getBatch().setColor(Color.WHITE);
			mapRenderer.setMap(worldTile.map);
			mapRenderer.setView((OrthographicCamera)gameStage.getViewport().getCamera());
			mapRenderer.render();
		}
		
		/*
		if(nextWorldTile != null){
			mapRenderer.getBatch().setColor(Color.GREEN);
			mapRenderer.setMap(nextWorldTile.map);
			mapRenderer.setView(gameStage.getViewport().getCamera().combined, (1-transition) * GameSettings.WORLD_WIDTH, 0, GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
			mapRenderer.render();
		}
		*/
		
		gameStage.getViewport().apply();
		gameStage.getBatch().begin();
		
		if(FBOMODE){
			gameStage.getBatch().setColor(Color.WHITE);
			gameStage.getBatch().draw(fbo.getColorBufferTexture(), 0f, 0f, gameStage.getWidth(), gameStage.getHeight(), 0f, 0f, 1f, 1f);
		}
		
		for(Entity e : worldTile.entities){
			if(e instanceof Drone){
				Drone d = (Drone)e;
				d.drawLasers(gameStage.getBatch());
			}
		}
		
		gameStage.getBatch().end();
		
		
		gameStage.act();
		gameStage.draw();
		
		stage.getViewport().apply();
		super.render(delta);
		
		if(renderMaze){
			mazeRenderer.render();
		}
		
		Assets.i.audio.playState(game);
		
		if(game.gameOver && !locked){
			Assets.i.audio.clearState();
			DL11Game.i().setScreen(new MenuScreen());
		}
	}
	
	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().update(width, height);
		super.resize(width, height);
	}
}
