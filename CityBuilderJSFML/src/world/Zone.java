package world;

import org.jsfml.graphics.IntRect;

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
		ROAD
	}
	
	protected IntRect hitbox;
	protected ZoneClass zoneType;
	
	/**
	 * 
	 * @param x : position x of the zone
	 * @param y : position y of the zone
	 * @param width : sizeX of the zone
	 * @param height : sizeY of the zone
	 * @param type : type of the zone
	 */
	public Zone(int x, int y ,int width , int height, ZoneClass type) {
		this.hitbox = new IntRect(x,y,width,height);
		this.zoneType = type;
	}
	
	/**
	 * return the hitbox of the zone
	 * @return IntRect : the hitbox of the zone
	 */
	public IntRect getHitbox() {
		return this.hitbox;
	}
	
	/**
	 * return the ZoneClass
	 * @return ZoneClass : class of the zones
	 */
	public ZoneClass getType() {
		return this.zoneType;
	}
}
