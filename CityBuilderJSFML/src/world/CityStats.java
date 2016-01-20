package world;

import java.util.List;

public class CityStats {
	
	protected int population;
	protected int money;
	
	/**
	 * 
	 * @param buildings
	 */
	public void update(List<Building> buildings) {
		this.population = 0;
		this.money = 0;
		for(Building b :  buildings) {
			if(b.getType().equals(Building.BuildingType.HOUSE)) {
				this.population += 4;
			}
		}
	}
	
	public int getPopulation() {
		return this.population;
	}
	
	public int getMoney() {
		return this.money;
	}
}	
