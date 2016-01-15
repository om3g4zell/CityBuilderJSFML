package sim;

import java.util.ArrayList;

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
	protected static final Vector2i TILEMAP_SIZE = new Vector2i(12, 8);
	protected static final Vector2f TILE_SIZE = new Vector2f(32.f, 32.f);
	
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
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(1, 1)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(3, 1)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(5, 1)));
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(7, 1)));
		
		// Road.
		this.buildings.add(new Building(BuildingType.GENERATOR, new Vector2i(9, 2)));
		
		// Generator.
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(1, 3)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(2, 3)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(3, 3)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(4, 3)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(5, 3)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(6, 3)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(7, 3)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(8, 3)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(9, 3)));
		
		// Inits the tilemap.
		this.tilemap = new TileMap(TILEMAP_SIZE, TILE_SIZE);
		this.tilemap.addTypeColor(TileType.TERRAIN_GRASS, new Color(0, 70, 0));
		this.tilemap.addTypeColor(TileType.BUILDING_HOUSE, new Color(70, 0, 0));
		this.tilemap.addTypeColor(TileType.BUILDING_ROAD, new Color(190, 190, 190));
		this.tilemap.addTypeColor(TileType.BUILDING_GENERATOR, new Color(227, 168, 87));
		this.tilemap.setTiles(this.tiles);
	}
	
	// Updates all the simulation.
	public void update(Time dt) {
		this.resourcesMap.reset();
		
		for(Building b : this.buildings) {
			
			// Generate resources.
			b.generateResources(this.resourcesMap);
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
