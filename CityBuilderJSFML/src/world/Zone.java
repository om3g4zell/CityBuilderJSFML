package world;

import org.jsfml.system.Vector2i;

/*
 * definite zone 
 */

public class Zone {
	
	/**
	 * Zone class
	 */
	public static enum ZoneClass {
		RESIDENTIAL,
		CULTURAL,
		INDUSTRY,
		COMMERCIAL,
		ROAD,
		FREE
	}
	
	protected Vector2i position;
	protected ZoneClass zoneType;
	
	/**
	 * 
	 * @param x : position x of the zone
	 * @param y : position y of the zone
	 * @param width : sizeX of the zone
	 * @param height : sizeY of the zone
	 * @param type : type of the zone
	 */
	public Zone(int x, int y, ZoneClass type) {
		this.position = new Vector2i(x, y);
		this.zoneType = type;
	}
	
	/**
	 * return the position of the zone
	 * @return Vector2i : the position of the zone
	 */
	public Vector2i getPosition() {
		return this.position;
	}
	
	/**
	 * return the ZoneClass
	 * @return ZoneClass : class of the zones
	 */
	public ZoneClass getType() {
		return this.zoneType;
	}
	
	/**
	 * set the type of the zone
	 * @param type : type of the zone
	 */
	public void setType(ZoneClass type) {
		this.zoneType = type;
	}
	
	
}
