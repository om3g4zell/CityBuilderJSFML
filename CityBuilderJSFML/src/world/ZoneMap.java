package world;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.system.Vector2i;

public class ZoneMap {
	protected List<List<Zone>> zoneMap;
	protected Vector2i size;
	
	public ZoneMap(int width, int height) {
		this.size = new Vector2i(width, height);
		
		// Instantiates the zones
		this.zoneMap = new  ArrayList<List<Zone>>();
			
		// Create a large enough two-dimensional array.
		for(int i = 0 ; i < size.y ; ++i) {
			ArrayList<Zone> zone = new ArrayList<Zone>();
					
			for(int j = 0 ; j < size.x ; ++j) {
				zone.add(new Zone(j, i, Zone.ZoneClass.FREE));
			}
					
			this.zoneMap.add(zone);
		}
	}
	
	/**
	 * return the zone map
	 * @return List<List<Zone>> : the zone map
	 */
	public List<List<Zone>> getZoneMap() {
		return this.zoneMap;
	}
	
	/**
	 * The new zone map
	 * @param List<List<Zone>> : the new zone map
	 */
	public void setZoneMap(List<List<Zone>> zm) {
		this.zoneMap = zm;
	}
	
	/**
	 * return the size of the zone map
	 * @return Vector2i : the size of the zone map
	 */
	public Vector2i getSize() {
		return this.size;
	}
}
