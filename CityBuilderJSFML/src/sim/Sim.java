package sim;

import java.util.ArrayList;
import java.util.Map;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;

import graphics.Tile.TileType;
import graphics.BuildingProjector;
import graphics.Tile;
import graphics.TileMap;
import world.Building;
import world.Building.BuildingType;
import world.ResourcesMap;

/*
 * Simulation class.
 * Contains init, update and render.
 */
public class Sim {
	// Constants.
	protected static final Vector2i TILEMAP_SIZE = new Vector2i(80, 45);
	protected static final Vector2f TILE_SIZE = new Vector2f(16.f, 16.f);
	
	// Attributes.
	protected RenderWindow window;
	protected TileMap tilemap;
	protected ArrayList<ArrayList<Tile>> tiles;
	protected ResourcesMap resourcesMap;
	protected ArrayList<Building> buildings;
	
	// Constructor
	public Sim(int width, int height, String title) {
		this.window = new RenderWindow(new VideoMode(width, height), title);
	}
	
	// Initialization
	public void init() {
		// Inits the tiles array.
		this.tiles = new ArrayList<ArrayList<Tile>>();
		
		for(int i = 0 ; i < TILEMAP_SIZE.y ; ++i) {
			ArrayList<Tile> row = new ArrayList<Tile>();
			
			for(int j = 0 ; j < TILEMAP_SIZE.x ; ++j) {
				row.add(new Tile(TileType.TERRAIN_GRASS, new Vector2i(j, i)));
			}
			
			this.tiles.add(row);
		}
		
		// Create the resources map.
		this.resourcesMap = new ResourcesMap(TILEMAP_SIZE);
		
		// Create the buildings list.
		this.buildings = new ArrayList<Building>();
		
		// Houses.
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(31, 20)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(33, 20)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(35, 20)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(37, 20)));
		
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(31, 23)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(33, 23)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(35, 23)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(37, 23)));
		
		// Generator.
		this.buildings.add(new Building(BuildingType.GENERATOR, new Vector2i(39, 21)));
		
		// Water station.
		this.buildings.add(new Building(BuildingType.HYDROLIC_STATION, new Vector2i(39, 23)));
		
		// Road.
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(31, 22)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(32, 22)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(33, 22)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(34, 22)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(35, 22)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(36, 22)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(37, 22)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(38, 22)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(39, 22)));
		
		// Grossery store
		this.buildings.add(new Building(BuildingType.GROCERY_STORE, new Vector2i(40, 21)));
		
		// Inits the tilemap.
		this.tilemap = new TileMap(TILEMAP_SIZE, TILE_SIZE);
		this.tilemap.addTypeColor(TileType.TERRAIN_GRASS, new Color(0, 70, 0));
		this.tilemap.addTypeColor(TileType.BUILDING_HOUSE, new Color(70, 0, 0));
		this.tilemap.addTypeColor(TileType.BUILDING_ROAD, new Color(190, 190, 190));
		this.tilemap.addTypeColor(TileType.BUILDING_GENERATOR, new Color(227, 168, 87));
		this.tilemap.addTypeColor(TileType.BUILDING_HYDROLIC_STATION, new Color(51, 153, 255));
		this.tilemap.addTypeColor(TileType.BUILDING_SUPERMARKET, new Color(194, 195, 98));
		this.tilemap.setTiles(this.tiles);
	}
	
	// Updates all the simulation.
	public void update(Time dt) {
		System.out.println("Update:");

		// Reset the resources.
		this.resourcesMap.reset();
		
		// Generate resources.
		for(Building b : this.buildings) {
			b.generateResources(this.resourcesMap);
		}
		
		// Consume resources.
		for(Building b : this.buildings) {
			BuildingType requiredBuilding = b.consumeResources(this.resourcesMap);
			
			System.out.println("\t" + b.getType().toString() + " wants to build " + requiredBuilding.toString());
		}
		
		// Project buildings on the tilemap.
		BuildingProjector.project(this.buildings, this.tilemap);

		// Update the tilemap.
		this.tilemap.update();
	}
	
	// Renders all the simulation.
	public void render() {
		this.window.clear(Color.WHITE);
		/////////////

		this.window.draw(tilemap);
		
		/////////////
		this.window.display();
		
	}
	
	// Returns the window.
	public RenderWindow getWindow() {
		return this.window;
	}
}
