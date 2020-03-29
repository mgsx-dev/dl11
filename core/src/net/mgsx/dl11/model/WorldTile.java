package net.mgsx.dl11.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import net.mgsx.dl11.assets.Assets;
import net.mgsx.dl11.maze.MazeCell;
import net.mgsx.dl11.utils.Grid2D;
import net.mgsx.dl11.utils.MapAnalyser;
import net.mgsx.dl11.utils.Ray2D;
import net.mgsx.dl11.utils.UniControl;

public class WorldTile {
	
	public static Grid2D createColMap() {
		return new Grid2D(){
			@Override
			protected boolean isObstacle(Cell cell) {
				return cell == null || cell.getTile().getId() == 2;
			}
		};
	}
	
	private static final Vector2 center = new Vector2();
	private static final Vector2 end = new Vector2();
	private static final Vector2 normal = new Vector2();
	public TiledMap map;
	private final Array<Entity> entities = new Array<Entity>();
	private int width;
	private int height;
	public boolean exiting;
	public int exitDirection;
	public int x, y;
	
	private Grid2D colMap = createColMap();
	public Hero hero;
	private boolean entering;
	public boolean active;
	public Car car;
	private boolean exitingCar;
	private static final Ray2D worldRay = new Ray2D();
	
	private final GameState game;
	public boolean noInput;
	public boolean isLastTile;
	public boolean isFirstTile;
	public boolean visited;
	public String name;
	
	
	public WorldTile(GameState game) {
		super();
		this.game = game;
	}

	public void loadCollisions(TiledMapTileLayer layer){
		colMap.createFrom(layer);
	}
	
	public Grid2D getGrid(){
		return colMap;
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
						createEntity(x, y, id-1);
					}
				}
			}
		}
		
		
	}

	private void createEntity(int x, int y, int id) {
		if(id == 2){
			entities.add(new Drone(this, x, y, false, false, false));
		}
		if(id == 3){
			entities.add(new Drone(this, x, y, true, false, false));
		}
		if(id == 6){
			entities.add(new Drone(this, x, y, false, true, false));
		}
		if(id == 7){
			entities.add(new Drone(this, x, y, true, true, false));
		}
		
		if(id == 10){
			entities.add(new Drone(this, x, y, false, false, true));
		}
		if(id == 11){
			entities.add(new Drone(this, x, y, true, false, true));
		}
		if(id == 14){
			entities.add(new Drone(this, x, y, false, true, true));
		}
		if(id == 15){
			entities.add(new Drone(this, x, y, true, true, true));
		}

		
		if(id == 8){
			entities.add(hero = new Hero(game, this, x, y));
		}
		if(id == 4){
			entities.add(new MedPack(x, y));
		}
		if(id == 5){
			entities.add(new Fuel(x, y));
		}
	}
	
	public void getActors(Group group){
		for(Entity e : entities){
			group.addActor(e.actor);
		}
	}
	
	public void setEntering(int direction){
		// XXX this.hero = hero;
		
		Vector2 point = MapAnalyser.getEntry(colMap, direction);
		
		GridPoint2 delta = MazeCell.DELTAS[direction];
		
		float borderDist = 1f;
		
		this.hero.position.set(point.x - delta.x * borderDist, point.y - delta.y * borderDist);
		
		this.hero.velocity.setZero();
		
		this.hero.controlEnabled = false;
		
		for(Entity e : entities){
			if(e instanceof Bonus){
				((Bonus)e).respawn(game);
			}
		}
		
		entering = true;
	}
	
	public void update(float delta){
		
		if(!active) delta = 0; // XXX disable anims
		
		boolean playerFired = false;
		for(Entity e2 : entities){
			if(e2 instanceof Drone){
				playerFired |= ((Drone) e2).isFiringPlayer();
			}
			if(e2 instanceof Bonus){
				Bonus b = (Bonus)e2;
				if(hero.car == null && b.canBeAquired(game)){
					if(hero.position.dst(e2.position) < 1){
						if(b.aquire(game)){
							Story.pickupBonus(game, b);
						}
					}
				}
			}
		}
		hero.fired = playerFired;
		
		this.hero.controlEnabled = active;
		
		for(Entity e : entities){
			
			if(e instanceof Drone){
				// XXX faut faire un choix là
				// ((Drone) e).active = car == null;
				((Drone) e).active = hero.car == null;
			}
			
			e.update(delta);
		}
		
		if(active){
			// compute full state of all drones
			boolean dronesFiring = false;
			boolean dronesActive = false;
			for(Entity e : entities){
				if(e instanceof Drone){
					Drone d = (Drone)e;
					// XXX if(d.isFiringPlayer())
					dronesFiring |= d.isFiring();
					dronesActive |= d.active;
				}
			}
			
			Assets.i.audio.playDroneStates(dronesFiring, dronesActive);
		}
		
		Hero e = hero;
		
		if(entering){
			float heroRadius = .6f; // XXX extra size

			// TODO some anims
			if(e.position.x > heroRadius && e.position.x < width - heroRadius && e.position.y > heroRadius && e.position.y < height - heroRadius){
				entering = false;
			}
		}else{
			if(!noInput){
				float heroRadius = .5f;
				if(e.position.x <= heroRadius){
					exitDirection = MazeCell.WEST;
					exiting = true;
				}
				if(e.position.x >= width - heroRadius){
					exitDirection = MazeCell.EAST;
					exiting = true;
				}
				if(e.position.y <= heroRadius){
					exitDirection = MazeCell.SOUTH;
					exiting = true;
				}
				if(e.position.y >= height - heroRadius){
					exitDirection = MazeCell.NORTH;
					exiting = true;
				}
				if(car != null && hero.car == null){
					boolean collideWithCar = Grid2D.intersectRectCircle(hero.position, heroRadius, car.position.x, car.position.y, car.width, car.height);
					if(!exitingCar && collideWithCar){
						hero.car = car;
						car.controlEnabled = true;
						hero.position.set(car.position.x + car.width/2, car.position.y + car.height/2);
						hero.actor.setVisible(false);
						exitingCar = false;
						
						// transfert fuel
						int currentFuel = MathUtils.ceil(game.carFuel);
						int fuelEmpty = GameSettings.CAR_FUEL_MAX - currentFuel;
						int fuelToAdd = Math.min(fuelEmpty, game.heroFuel);
						
						game.carFuel += fuelToAdd;
						game.heroFuel -= fuelToAdd;
						
						Story.enteringCar(game);
						
						if(game.carFuel > 0){
							Assets.i.audio.playMusicCar();
						}else{
							Assets.i.audio.playCarNoFioul();
						}
						
					}else if(exitingCar && !collideWithCar){
						exitingCar = false;
					}
				}
				if(car != null && hero.car != null){
					if(UniControl.isActionJustPressed()){
						hero.car = null;
						car.controlEnabled = false;
						hero.actor.setVisible(true);
						hero.actor.toFront();
						exitingCar = true;
						Story.exitingCar(game);
						
						Assets.i.audio.playMusicHero();
						Assets.i.audio.playExitingCar();
					}
				}
			}
		}
				
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

	public boolean clamp(Entity e) {
		if(e instanceof Hero){
			Hero h = (Hero)e;
			Car c = h.car;
			if(c != null){
				boolean collide = false;
				float cRadius = .7f; // XXX 1
				if(colMap.intersectCircle(center.set(h.position.x - .9f, h.position.y), cRadius, false)){
					collide = true;
					h.position.set(center).sub(-.9f, 0);
				}
				if(colMap.intersectCircle(center.set(h.position.x + .5f, h.position.y), cRadius, false)){
					collide = true;
					h.position.set(center).sub(.5f, 0);
				}
//				if(collide){
//					h.position.set(c.position.x + c.width/2, c.position.y+c.height/2);
//				}
				return collide;
			}else{
				return colMap.intersectCircle(e.position, .5f, false);
			}
		}else{
			return colMap.intersectCircle(e.position, .5f, true); // TODO radius based on entity
		}
	}

	public void reset() {
//		entities.removeValue(hero, true);
//		hero = null;
		exiting = false;
		entering = false;
		active = false;
		
		hero.car = null;
		
		for(Entity e : entities){
			if(!(e instanceof Car)){
				e.reset();
			}
		}
	}

	public void spawnCar() {
		// for init tile only
		
		car = new Car(game, (int)(GameSettings.WORLD_WIDTH/2), (int)(GameSettings.WORLD_HEIGHT/2));
		entities.add(car);
	}

	public void transfert(WorldTile worldTile) {
		if(worldTile.hero.car != null){
			hero.car = car = worldTile.car;
			worldTile.entities.removeValue(worldTile.car, true);
			worldTile.car = null;
			entities.add(car);
		}
	}

	
	
}
