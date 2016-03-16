package graphics;

import org.jsfml.system.Vector2i;

/**
 * Represents a tile.
 */
public class Tile {
	/**
	 * Enum of each tileType.
	 */
	public static enum TileType {
		TERRAIN_GRASS,
/*		BUILDING_HOUSE,
		BUILDING_SUPERMARKET,
		BUILDING_ROAD,
		BUILDING_GENERATOR,
		BUILDING_HYDROLIC_STATION,
		BUILDING_ANTENNA_4G,
		BUILDING_POLICE_STATION,
		BUILDING_FIRE_STATION,
		BUILDING_HOSPITAL,
		BUILDING_SCHOOL,
		BUILDING_CINEMAS,
		BUILDING_STADIUM,
		BUILDING_CASINOS,
		BUILDING_MALL,
		BUILDING_RESTAURANT,
		BUILDING_PUB,*/
	}

	/** Track the last id. */
	protected static int lastId = 0;
	
	// Attributes.
	protected TileType tileType;
	protected int id;
	protected Vector2i position;
	
	/**
	 *  Constructor.
	 * @param tileType : the type of the tile
	 * @param position : the position in tile coordinates
	 */
	public Tile(TileType tileType, Vector2i position) {
		this.tileType = tileType;
		this.id = Tile.lastId;
		this.position = position;
		
		// Increment the last id.
		Tile.lastId++;
	}
	
	/**
	 * Returns the tile type.
	 * @return the tile type
	 */
	public TileType getTileType() {
		return this.tileType;
	}
	
	/**
	 * Set the tile ID
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the tile ID
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Sets the position of the tile with Vector2i
	 * @param a : the new position vector
	 */
	public void setPosition(Vector2i a) {
		this.position = a;
	}
	
	/**
	 * Sets the position of the tile with int
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
		setPosition(new Vector2i(x, y));
	}
	
	/**
	 * Returns the position of the tile
	 * @return the position
	 */
	public Vector2i getPosition() {
		return this.position;
	}
}
