package graphics;

import org.jsfml.system.Vector2i;

/*
 * Represents a tile
 */
public class Tile {
	// Enum of each tileType
	public static enum TileType {
		TERRAIN_GRASS,
		BUILDING_HOUSE,
		BUILDING_SUPERMARKET,
		BUILDING_ROAD,
		BUILDING_GENERATOR
	}
	
	// Attributes.
	protected TileType tileType;
	protected int id;
	protected Vector2i position;
	
	// Set the tileType
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}
	
	// Return the tileType
	public TileType getTileType() {
		return this.tileType;
	}
	
	// Set the tile ID
	public void setId(int id) {
		this.id = id;
	}
	
	// Return the tile ID
	public int getId() {
		return this.id;
	}
	
	// Set the position of the tile with Vector2i
	public void setPosition(Vector2i a) {
		this.position = a;
	}
	
	// Set the position of the tile with int
	public void setPosition(int x, int y) {
		setPosition(new Vector2i(x,y));
	}
	
	// Return the position of the tile
	public Vector2i getPosition() {
		return this.position;
	}
}
