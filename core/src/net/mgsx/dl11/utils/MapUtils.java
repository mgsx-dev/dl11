package net.mgsx.dl11.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MapUtils {

	public static TiledMap copyMap(TiledMap map, boolean copyLayers) {
		TiledMap newMap = new TiledMap();
		for(TiledMapTileSet ts : map.getTileSets()) 
			newMap.getTileSets().addTileSet(ts);
		newMap.getProperties().putAll(map.getProperties());
		if(copyLayers){
			throw new GdxRuntimeException("NYI"); // TODO impl.
		}
		return newMap;
	}
	
	public static TiledMapTileLayer copyLayer(TiledMapTileLayer layer){
		TiledMapTileLayer newLayer = new TiledMapTileLayer(layer.getWidth(), layer.getHeight(), (int)layer.getTileWidth(), (int)layer.getTileHeight());
		newLayer.getProperties().putAll(layer.getProperties());
		for(int y=0 ; y<newLayer.getHeight() ; y++){
			for(int x=0 ; x<newLayer.getWidth() ; x++){
				Cell cell = layer.getCell(x, y);
				if(cell != null){
					Cell newCell = new Cell();
					newCell.setTile(cell.getTile());
					newLayer.setCell(x, y, newCell);
				}
			}
		}
		return newLayer;
	}
	
	public static void paint(TiledMap map, TiledMap newMap, float offsetX, float offsetY) {
		resize(map, newMap, offsetX, offsetY);
		
		for(int i=0, j=0 ; i<map.getLayers().getCount() && j<newMap.getLayers().getCount() ; ){
			MapLayer layer = map.getLayers().get(i);
			MapLayer newLayer = newMap.getLayers().get(j);
			if(!(layer instanceof TiledMapTileLayer)){
				i++;
			}else if(!(newLayer instanceof TiledMapTileLayer)){
				j++;
			}else{
				paint((TiledMapTileLayer)layer, (TiledMapTileLayer)newLayer, offsetX, offsetY);
				i++;
				j++;
			}
		}
		
	}
	
	private static void paint(TiledMapTileLayer layer, TiledMapTileLayer newLayer, float offsetX, float offsetY) {
		int ox = (int)(offsetX/newLayer.getTileWidth());
		int oy = (int)(offsetY/newLayer.getTileHeight());
		
		for(int y=0 ; y<newLayer.getHeight() ; y++){
			for(int x=0 ; x<newLayer.getWidth() ; x++){
				Cell newCell = newLayer.getCell(x, y);
				Cell cell = null;
				if(newCell != null){
					cell = new Cell();
					cell.setTile(newCell.getTile());
				}
				layer.setCell(x + ox, y + oy, cell);
			}
		}
	}

	public static void resize(TiledMap map, TiledMap newMap, float offsetX, float offsetY) {
		
		Rectangle mapBounds = getBounds(new Rectangle(), map);
		Rectangle newMapBounds = getBounds(new Rectangle(offsetX, offsetY, 0, 0), newMap);
		
		mapBounds.merge(newMapBounds);
		
		Array<MapLayer> newLayers = new Array<MapLayer>();
		for(MapLayer layer : map.getLayers()){
			if(layer instanceof TiledMapTileLayer){
				TiledMapTileLayer newLayer = resize((TiledMapTileLayer)layer, mapBounds);
				newLayers.add(newLayer);
			}
		}
		while(map.getLayers().getCount()>0) map.getLayers().remove(map.getLayers().getCount()-1);
		for(MapLayer layer : newLayers){
			map.getLayers().add(layer);
		}
		
	}
	
	private static TiledMapTileLayer resize(TiledMapTileLayer layer, Rectangle bounds) {
		int width = MathUtils.ceil(bounds.getWidth() / layer.getTileWidth());
		int height = MathUtils.ceil(bounds.getHeight() / layer.getTileHeight());
		TiledMapTileLayer newLayer = new TiledMapTileLayer(width, height, (int)layer.getTileWidth(), (int)layer.getTileHeight());
		for(int y=0 ; y<layer.getHeight() ; y++){
			for(int x=0 ; x<layer.getWidth() ; x++){
				newLayer.setCell(x, y, layer.getCell(x, y));
			}
		}
		return newLayer;
	}

	public static Rectangle getBounds(Rectangle bounds, TiledMap map){
		bounds.width = 0;
		bounds.height = 0;
		for(MapLayer layer : map.getLayers()){
			if(layer instanceof TiledMapTileLayer){
				getBounds(bounds, (TiledMapTileLayer)layer);
			}
		}
		return bounds;
	}
	public static Rectangle getBounds(Rectangle bounds, TiledMapTileLayer tileLayer){
		bounds.width = Math.max(bounds.width, tileLayer.getWidth() * tileLayer.getTileWidth());
		bounds.height = Math.max(bounds.height, tileLayer.getHeight() * tileLayer.getTileHeight());
		return bounds;
	}

}
