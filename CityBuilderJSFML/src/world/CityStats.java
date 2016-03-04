package world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import world.Zone.ZoneClass;

public class CityStats {
	
	protected int population;
	protected int money;
	protected float unemploymentRate;
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
			for(ZoneClass zc : ZoneClass.values()) {
				if(b.getZoneClasses().equals(zc)) {
					attractivity.put(zc, attractivity.get(zc) + 1);
				}
			}
			int unemployment = 0;
			int citizenNumber = 0;
			for(Citizen c : b.getCitizens()) {
				citizenNumber++;
				if(c.getWorkBuildingId() == -1) {
					unemployment++;
				}
			}
			this.unemploymentRate = unemployment / citizenNumber;
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
	
	/**
	 * Get the unemployment rate.
	 * @return float : the unemployment rate
	 */
	public float getUnemploymentRate() {
		return this.unemploymentRate;
	}
}	
