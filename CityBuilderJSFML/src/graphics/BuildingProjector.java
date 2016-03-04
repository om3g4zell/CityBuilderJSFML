package graphics;

import java.util.List;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2i;

import graphics.Tile.TileType;
import world.Building;
import world.Building.BuildingType;

/**
 * Methods to "draw" the buildings on the tile map.
 */
public class BuildingProjector {
	/**
	 * Converts the building type to the corresponding tile type.
	 * @param buildingType : the type of the building
	 * @return the equivalent in tile type
	 */
	public static TileType getCorrespondingTileType(BuildingType buildingType) {
		switch(buildingType) {
			case GENERATOR:
				return TileType.BUILDING_GENERATOR;
			case GROCERY_STORE:
				return TileType.BUILDING_SUPERMARKET;
			case HOUSE:
				return TileType.BUILDING_HOUSE;
			case HYDROLIC_STATION:
				return TileType.BUILDING_HYDROLIC_STATION;
			case ROAD:
				return TileType.BUILDING_ROAD;
			case ANTENNA_4G:
				return TileType.BUILDING_ANTENNA_4G;
			default:
				return TileType.TERRAIN_GRASS;
		}
	}
	
	/**
	 * Projects the building list onto the tilemap.
	 * @param buildings : the list of buildings on the map
	 * @param tilemap : the world tile map
	 */
	public static void project(List<Building> buildings, TileMap tilemap) {
		for(int i = 0 ; i < tilemap.size.x ; i++) {
			for(int j = 0 ; j < tilemap.size.y ; j++) {
				tilemap.setTile(new Vector2i(i, j), Tile.TileType.TERRAIN_GRASS);
			}
		}
		
		for(Building b : buildings) {
			// Take the size.
			IntRect hitbox = b.getHitbox();
			
			// Set tiles.
			for(int i = hitbox.top ; i < hitbox.top + hitbox.height ; ++i) {
				for(int j = hitbox.left ; j < hitbox.left + hitbox.width ; ++j) {
					tilemap.setTile(new Vector2i(j, i), getCorrespondingTileType(b.getType()));
				}
			}
		}
	}
}
