package world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import world.Zone.ZoneClass;

public class CityStats {
	protected int population;
	protected float unemploymentRate;
	protected int unemployment;
	protected Map<ZoneClass, Float> attractivity;
	
	public CityStats() {
		this.attractivity = new HashMap<ZoneClass, Float>();
		
		for(ZoneClass z : ZoneClass.values()) {
			attractivity.put(z, 0.f);
		}
	}
	
	/**
	 * Update the population number.
	 * Update the money of the city.
	 * Update the attractivity score of each zones.
	 * @param buildings : City's buildings
	 */
	public void update(List<Building> buildings) {
		this.population = 0;
		
		for(ZoneClass z : ZoneClass.values()) {
			attractivity.put(z, 0.f);
		}
		
		float unemployment = 0;
		
		for(Building b :  buildings) {
			if(b.getType().equals(Building.BuildingType.HOUSE)) {
				this.population += b.getInhabitants().size();
			}
			
			for(ZoneClass zc : ZoneClass.values()) {
				boolean containsZone = false;
				
				for(ZoneClass currentZone : b.getZoneClasses())
					if(zc.equals(currentZone))
						containsZone = true;
				
				if(containsZone) {
					attractivity.put(zc, attractivity.get(zc) + 1);
				}
			}

			for(Citizen c : b.getInhabitants()) {

				if(c.getWorkBuildingId() == -1) {
					unemployment++;
				}
			}
			
		}
		if(this.population > 0)
			this.unemploymentRate = (unemployment / this.population)*100;
		this.unemployment = (int)unemployment;
		
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
	public float getUnemploymentRate() {
		return this.unemploymentRate;
	}
	
	/**
	 * Get the unemployment.
	 * @return int : the unemployment
	 */
	public int getUnemployment() {
		return this.unemployment;
	}
}	
