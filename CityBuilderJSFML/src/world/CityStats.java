package world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import world.Zone.ZoneClass;

public class CityStats {
	
	protected int population;
	protected int money;
	protected Map<ZoneClass, Float> attractivity = new HashMap<ZoneClass, Float>();
	
	/**
	 * Update the population number.
	 * Update the money of the city.
	 * Update the attractivity score of each zones.
	 * @param buildings : City's buildings
	 */
	public void update(List<Building> buildings) {
		this.population = 0;
		this.money = 0;
		for(Building b :  buildings) {
			if(b.getType().equals(Building.BuildingType.HOUSE)) {
				this.population += 4;
			}
		}
		for(Building building : buildings) {
			for(ZoneClass zc : ZoneClass.values()) {
				if(building.getZoneClasses().equals(zc)) {
					attractivity.put(zc, attractivity.get(zc) + 1);
				}
			}
		}
	}
	
	/**
	 * The attrativity score of a zone.
	 * @param zoneClass
	 * @return the score of the zone
	 */
	public float getAttractivity(ZoneClass zoneClass) {
		return attractivity.get(zoneClass);
	}
	
	/**
	 * Get the population number.
	 * @return int : population of the city
	 */
	public int getPopulation() {
		return this.population;
	}
	
	/**
	 * Get the money of the city.
	 * @return int : the money of the city
	 */
	public int getMoney() {
		return this.money;
	}
}	
