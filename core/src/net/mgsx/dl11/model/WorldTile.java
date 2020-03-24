package net.mgsx.dl11.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import net.mgsx.dl11.utils.Grid2D;
import net.mgsx.dl11.utils.Ray2D;

public class WorldTile {
	private static final Vector2 center = new Vector2();
	private static final Vector2 end = new Vector2();
	private static final Vector2 normal = new Vector2();
	public TiledMap map;
	private final Array<Entity> entities = new Array<Entity>();
	private int width;
	private int height;
	
	private Grid2D colMap = new Grid2D(){
		@Override
		protected boolean isObstacle(Cell cell) {
			return cell == null || cell.getTile().getId() == 2;
		}
	};
	private Hero hero;
	private static final Ray2D worldRay = new Ray2D();
	
	
	public void loadCollisions(TiledMapTileLayer layer){
		colMap.createFrom(layer);
	}

	public void load(TiledMapTileLayer layer) 
	{
		this.width = layer.getWidth();
		this.height = layer.getHeight();
		
		for(int y=0 ; y<layer.getHeight() ; y++){
			for(int x=0 ; x<layer.getWidth() ; x++){
				Cell cell = layer.getCell(x, y);
				if(cell != null){
					TiledMapTile tile = cell.getTile();
					if(tile != null){
						int id = tile.getId();
						createEntity(x, y, id);
					}
				}
			}
		}
		
		
	}

	private void createEntity(int x, int y, int id) {
		if(id == 2){
			entities.add(new Drone(this, x, y, false, false));
		}
		if(id == 3){
			entities.add(new Drone(this, x, y, true, false));
		}
		if(id == 6){
			entities.add(new Drone(this, x, y, false, true));
		}
		if(id == 7){
			entities.add(new Drone(this, x, y, true, true));
		}
		if(id == 8){
			entities.add(hero = new Hero(this, x, y));
		}
		
	}
	
	public void getActors(Group group){
		for(Entity e : entities){
			group.addActor(e.actor);
		}
	}
	
	public void update(float delta){
		
		boolean playerFired = false;
		for(Entity e2 : entities){
			if(e2 instanceof Drone){
				playerFired |= ((Drone) e2).isFiringPlayer();
			}
		}
		hero.fired = playerFired;
		
		for(Entity e : entities){
			e.update(delta);
		}
		
		Hero e = hero;
		if(e.position.x < 1){
			System.out.println("out left");
		}
		if(e.position.x > width - 1 - .5f){
			System.out.println("out right");
		}
		if(e.position.y < 1){
			System.out.println("out down");
		}
		if(e.position.y > height - 1 - .5f){
			System.out.println("out up");
		}
				
	}

	@Deprecated
	public boolean rayCast(Vector2 result, Vector2 start, GridPoint2 direction, boolean castHeroes) {
		return rayCast(result, start, new Vector2(direction.x, direction.y), castHeroes);
	}
	
	public boolean rayCast(Vector2 result, Vector2 start, Vector2 direction, boolean castHeroes) {
		boolean hurtPlayer = false;
		
		worldRay.origin.set(start);
		worldRay.direction.set(direction);
		colMap.rayCast(result, normal, worldRay);
		
		/*
		
		int ix = MathUtils.floor(start.x);
		int iy = MathUtils.floor(start.y);
		int px = ix;
		int py = iy;
		while(ix >= 0 && ix < width && iy >= 0 && iy < height){
			px = ix;
			py = iy;
			ix += direction.x;
			iy += direction.y;
			Cell cell = ((TiledMapTileLayer)map.getLayers().get(0)).getCell(ix, iy);
			if(cell != null && cell.getTile() != null && cell.getTile().getId() == 2){
				break;
			}
		}
		result.set(ix, iy);
		if(direction.x < 0 || direction.y < 0) result.sub(direction.x, direction.y);
		*/
		if(castHeroes){
			end.set(result);
			
			// ray cast player
			for(Entity e : entities){
				if(e instanceof Hero){
					float radius = .5f;
					center.set(e.position);
					if(Intersector.intersectSegmentCircle(start, end, center, radius * radius)){
						result.set(e.position);
						hurtPlayer = true;
					}
				}
			}
		}
		
		
		return hurtPlayer;
	}

	public boolean clamp(Entity hero) {
		return colMap.intersectCircle(hero.position, .5f);
	}
	
}
