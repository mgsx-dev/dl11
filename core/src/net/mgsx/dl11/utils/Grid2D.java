package net.mgsx.dl11.utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Grid2D {
	private int w, h;
	private boolean [] obstacleMap;
	private float scaleX, scaleY;
	private Ray2D ray = new Ray2D();
	private GridPoint2 gridPosition = new GridPoint2();
	private Vector2 delta = new Vector2();
	
	private static final GridPoint2[] corners = new GridPoint2[]{
			new GridPoint2(1, 1),
			new GridPoint2(0, 1),
			new GridPoint2(0, 0),
			new GridPoint2(1, 0)
	};
	private static final GridPoint2[] diagonal = new GridPoint2[]{
			new GridPoint2(1, 1),
			new GridPoint2(-1, 1),
			new GridPoint2(-1, -1),
			new GridPoint2(1, -1)
	};
	private static final GridPoint2[] orthogonal = new GridPoint2[]{
			new GridPoint2(1, 0),
			new GridPoint2(0, 1),
			new GridPoint2(-1, 0),
			new GridPoint2(0, -1)
	};
	
	public void createFrom(TiledMapTileLayer layer){
		scaleX = 1f; // XXX / layer.getTileWidth();
		scaleY = 1f; // XXX / layer.getTileHeight();
		w = layer.getWidth();
		h = layer.getHeight();
		obstacleMap = new boolean[w*h];
		for(int y=0 ; y<h ; y++){
			for(int x=0 ; x<w ; x++){
				obstacleMap[y*w+x] = isObstacle(layer.getCell(x, y));
			}
		}
	}

	abstract protected boolean isObstacle(Cell cell);

	public float rayCast(Vector2 collisionPosition, Vector2 collisionNormal, Ray2D worldRay)
	{
				
		// convert world ray to normalized ray
		ray.origin.set(worldRay.origin).scl(scaleX, scaleY);
		ray.direction.set(worldRay.direction);
		
		// convert ray origin to grid position
		gridPosition.set(MathUtils.floor(ray.origin.x), MathUtils.floor(ray.origin.y));
		
		// convert ray direction to cadran
		int cadran;
		if(ray.direction.x < 0){
			if(ray.direction.y < 0){
				cadran = 2;
			}else{
				cadran = 1;
			}
		}else{
			if(ray.direction.y < 0){
				cadran = 3;
			}else{
				cadran = 0;
			}
		}
		
		GridPoint2 localCorner = corners[cadran];
		
		GridPoint2 gridDirection;
		for(;;){
			
			// compute delta vector from ray origin to corner
			delta.set(gridPosition.x + localCorner.x, gridPosition.y + localCorner.y).sub(ray.origin);
			
			// cross product sign means if ray is CW or CCW relative to corner
			// and says which edge the ray pass through.
			float crs = delta.crs(ray.direction);
			if(crs > 0){
				gridDirection = orthogonal[(cadran+1) & 3]; // optimized % 4
			}else if(crs < 0){
				gridDirection = orthogonal[cadran];
			}else{
				gridDirection = diagonal[cadran];
			}
			
			gridPosition.add(gridDirection);
			
			// stop when collide with obstacle (or map border)
			if(collide(gridPosition)){
				break;
			}
		}
		
		// normal is gridDirection opposite
		int nx = -gridDirection.x;
		int ny = -gridDirection.y;
		
		// project ray on edge to get ray length
		float dot1 = delta.dot(nx, ny);
		float dot2 = ray.direction.dot(nx, ny);
		float length = dot1 / dot2;
		
		// compute final ray target
		collisionPosition.set(ray.origin).mulAdd(ray.direction, length).scl(1f / scaleX, 1f / scaleY);
		
		// returns collision normal
		collisionNormal.set(nx, ny);
		
		return length;
	}
	
	public boolean collide(Vector2 worldPoint){
		gridPosition.set(MathUtils.floor(worldPoint.x * scaleX), MathUtils.floor(worldPoint.y * scaleY));
		return collide(gridPosition);
	}

	private boolean collide(GridPoint2 point) {
		return collide(point.x, point.y);
	}

	private boolean collide(int x, int y) {
		if(x>=0 && x<w && y>=0 && y<h) return obstacleMap[y*w+x];
		return true;
	}
	
	/**
	 * Simple intersection with displacement apply to circle.
	 * Fast objects not supported with this method.
	 * @param center
	 * @param radius
	 * @return
	 */
	public boolean intersectCircle(Vector2 center, float radius){
		
		int ix1 = MathUtils.floor((center.x - radius) * scaleX);
		int ix2 = MathUtils.ceil((center.x + radius) * scaleX);
		
		int iy1 = MathUtils.floor((center.y - radius) * scaleY);
		int iy2 = MathUtils.ceil((center.y + radius) * scaleY);
		
		boolean colliding = false;
		for(int ix = ix1 ; ix <= ix2 ; ix++){
			for(int iy = iy1 ; iy <= iy2 ; iy++){
				if(collide(ix, iy)){
					colliding |= intersectSegment(center, radius, ix, iy, ix+1, iy);
					colliding |= intersectSegment(center, radius, ix, iy+1, ix+1, iy+1);
					colliding |= intersectSegment(center, radius, ix, iy, ix, iy+1);
					colliding |= intersectSegment(center, radius, ix+1, iy, ix+1, iy+1);
					
					colliding |= intersectPoint(center, radius, ix, iy);
					colliding |= intersectPoint(center, radius, ix+1, iy);
					colliding |= intersectPoint(center, radius, ix, iy+1);
					colliding |= intersectPoint(center, radius, ix+1, iy+1);
				}
			}
			
		}
		
		return colliding;
	}
	
	private Vector2 start = new Vector2();
	private Vector2 end = new Vector2();
	private Vector2 displacement = new Vector2();
	private boolean intersectSegment(Vector2 center, float radius, int ix1, int iy1, int ix2, int iy2){
		start.set(ix1 / scaleX, iy1 / scaleY);
		end.set(ix2 / scaleX, iy2 / scaleY);
		float len = Intersector.intersectSegmentCircleDisplace(start, end, center, radius, displacement);
		if(len < Float.POSITIVE_INFINITY){
			center.mulAdd(displacement, radius - len);
			return true;
		}
		return false;
	}
	
	private boolean intersectPoint(Vector2 center, float radius, int ix, int iy){
		float x = ix / scaleX;
		float y = iy / scaleY;
		displacement.set(x, y).sub(center);
		float len = displacement.len();
		if(len < radius){
			if(len > 0){
				center.mulAdd(displacement, -(radius - len) / len);
			}
			return true;
		}
		return false;
	}
}
