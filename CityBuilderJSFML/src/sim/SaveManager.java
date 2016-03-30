package sim;

import java.util.ArrayList;
import java.util.List;

import graphics.Tile;
import world.Building;
import world.Zone;

public class SaveManager {
	protected List<List<Tile>> tiles;
	protected List<List<Zone>> zones;
	protected List<Building> buildings;
	
	public SaveManager() {
		this.tiles = new ArrayList<List<Tile>>();
		this.zones = new ArrayList<List<Zone>>();
		this.buildings = new ArrayList<Building>();
	}
	
	public boolean load(String path) {
		

		return true;
	}
	
	public List<Building> getBuildings() {
		return this.buildings;
	}
	
	public List<List<Zone>> getZones() {
		return this.zones;
	}
}
