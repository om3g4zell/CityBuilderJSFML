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
import graphics.FontManager;
import graphics.TextureManager;
import graphics.Tile;
import graphics.TileMap;
import gui.StatsGui;
import gui.TileInfoGui;
import gui.TileSelector;
import world.Building;
import world.Building.BuildingType;
import world.CityStats;
import world.ResourcesMap;

/**
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
	protected CityStats cityStats;
	protected TextureManager textureManager;
	protected FontManager fontManager;
	protected StatsGui statsGui;
	protected TileSelector tileSelector;
	protected TileInfoGui tileInfoGui;
	
	/**
	 * Constructor
	 * @param width : width of the window
	 * @param height : height of the window
	 * @param title : title of the window
	 */
	public Sim(int width, int height, String title) {
		this.window = new RenderWindow(new VideoMode(width, height), title);
	}
	
	/**
	 * Inits the simulation.
	 */
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
		
		// Instanciate the TextureManager
		this.textureManager = new TextureManager();
		
		// Instanciate the fontManager
		this.fontManager = new FontManager();
		
		//Instanciate the GUI
		this.statsGui = new StatsGui(textureManager, fontManager);
		this.tileSelector = new TileSelector(this.window, this.textureManager, TILEMAP_SIZE, TILE_SIZE);
		
		// Create the resources map.
		this.resourcesMap = new ResourcesMap(TILEMAP_SIZE);
		
		// Create the buildings list.
		this.buildings = new ArrayList<Building>();
		
		//
		this.cityStats = new CityStats();
		
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
		this.tilemap.addTypeColor(TileType.BUILDING_SUPERMARKET, new Color(125, 193, 129));
		this.tilemap.setTiles(this.tiles);
		

		// Instanciate the tileInfoGui
		this.tileInfoGui = new TileInfoGui(this.tiles,this.fontManager);
	}
	
	/**
	 * Updates all the simulation.
	 * @param dt : frame of time to use
	 */
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
		
		//Update stats
		this.cityStats.update(buildings);
		this.statsGui.setMoney(this.cityStats.getMoney());
		this.statsGui.setPopulation(this.cityStats.getPopulation());
		this.tileSelector.update();
		this.tileInfoGui.update(resourcesMap, tileSelector, buildings);
	}
	
	/**
	 * Renders all the simulation.
	 */
	public void render() {
		this.window.clear(Color.WHITE);
		/////////////

		this.window.draw(this.tilemap);
		this.window.draw(this.tileSelector);
		this.window.draw(this.statsGui);
		this.window.draw(tileInfoGui);
		
		/////////////
		this.window.display();
		
	}
	
	/**
	 * Returns the window.
	 * @return the window used by the simulation
	 */
	public RenderWindow getWindow() {
		return this.window;
	}
}
