package sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import graphics.Tile.TileType;
import graphics.BuildingProjector;
import graphics.FontManager;
import graphics.TextureManager;
import graphics.Tile;
import graphics.TileMap;
import graphics.ZoneMapLayer;
import gui.CheckBox;
import gui.StatsGui;
import gui.TileInfoGui;
import gui.TileSelector;
import gui.ZoneDrawingGui;
import world.Building;
import world.Building.BuildingType;
import world.CityStats;
import world.ResourcesMap;
import world.Zone;
import world.ZoneMap;

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
	protected List<ArrayList<Tile>> tiles;
	protected ResourcesMap resourcesMap;
	protected List<Building> buildings;
	protected CityStats cityStats;
	protected TextureManager textureManager;
	protected FontManager fontManager;
	protected StatsGui statsGui;
	protected TileSelector tileSelector;
	protected TileInfoGui tileInfoGui;
	protected boolean displayTileInfo;
	protected Map<Integer, Building.BuildingType> buildingsRequired;
	protected CheckBox checkbox1;
	protected ZoneMap zoneMap;
	protected ZoneMapLayer zoneMapLayer;
	protected ZoneDrawingGui zoneDrawingGui;
	
	/**
	 * Constructor
	 * @param width : width of the window
	 * @param height : height of the window
	 * @param title : title of the window
	 */
	public Sim(int width, int height, String title) {
		this.window = new RenderWindow(new VideoMode(width, height), title);
		this.displayTileInfo = false;
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
		
		// Create a checkbox
		this.checkbox1 = new CheckBox(10, 100 , this.textureManager, this.fontManager , "Afficher les zones", 0);
		
		// Create the city stats.
		this.cityStats = new CityStats();
		
		// Create the zoneMap
		this.zoneMap = new ZoneMap(TILEMAP_SIZE.x, TILEMAP_SIZE.y);
		
		// Create the zoneMapLayer
		this.zoneMapLayer = new ZoneMapLayer(this.zoneMap);
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.FREE, new Color(12, 52, 30, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.COMMERCIAL, new Color(125, 193, 129, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.CULTURAL, new Color(0, 70, 0, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.INDUSTRY, new Color(227, 168, 87, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.RESIDENTIAL, new Color(70, 0, 0, 170));
		
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
		
		// The map collecting the required buildings.
		this.buildingsRequired = new HashMap<Integer, Building.BuildingType>();

		// Instanciate the tileInfoGui
		this.tileInfoGui = new TileInfoGui(this.tiles, this.fontManager);
		
		// Instanciate the zone drawing GUI.
		this.zoneDrawingGui = new ZoneDrawingGui(this.textureManager, this.fontManager);
	}
	
	/**
	 * Spawns the new buildings.
	 */
	public void spawnBuildings() {
		// First count the buildings.
		Map<Building.BuildingType, Integer> buildingCounts = new HashMap<Building.BuildingType, Integer>();
		
		for(Map.Entry<Integer, Building.BuildingType> entry : this.buildingsRequired.entrySet()) {
			Building.BuildingType buildingType = entry.getValue();
			
			if(buildingCounts.containsKey(buildingType)) {
				Integer count = buildingCounts.get(buildingType);
				count = new Integer(count.intValue() + 1);
			}
			else {
				buildingCounts.put(buildingType, 1);
			}
		}
		
		// Get the most required.
		Map.Entry<Building.BuildingType, Integer> maxEntry = null;
		for(Map.Entry<Building.BuildingType, Integer> entry : buildingCounts.entrySet()) {
			if(maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		
		// We have a building type.
		if(maxEntry != null) {
			Building.BuildingType buildingType = maxEntry.getKey();
			Vector2i position = new Vector2i(0, 0);
			
			// Now get the position of everyone asking for that building type.
			for(Map.Entry<Integer, Building.BuildingType> entry : this.buildingsRequired.entrySet()) {
				Building.BuildingType btype = entry.getValue();
				
				if(btype == buildingType) {
					Building building = null;
					
					// Get the building.
					for(Building b : this.buildings) {
						if(b.getId() == entry.getKey()) {
							building = b;
							break;
						}
					}
					
					// Add its position.
					if(building != null) {
						Vector2i centerPosition = new Vector2i(building.getHitbox().left + building.getHitbox().width / 2, building.getHitbox().top + building.getHitbox().height / 2);
						position = Vector2i.add(position, centerPosition);
					}
				}
			}
			
			// Compute the average position.
			Vector2f fposition = new Vector2f(position.x, position.y);
			fposition = Vector2f.mul(fposition, 1.f / (float)(maxEntry.getValue()));
			
			position = new Vector2i((int)fposition.x, (int)fposition.y);
			
			// Look around the position to find a suitable spot.
			// Check if we are still in the range of the building.
				// Spawn the building.
			// Else, find another spot.
		}
	}
	
	/**
	 * Updates all the simulation.
	 * @param dt : frame of time to use
	 */
	public void update(Time dt) {
		// Reset the resources.
		this.resourcesMap.reset();
		
		// Generate resources.
		for(Building b : this.buildings) {
			b.generateResources(this.resourcesMap);
		}
		
		// update tile info
		if(this.displayTileInfo)
			this.tileInfoGui.update(resourcesMap, tileSelector, buildings);
		
		// Consume resources.
		buildingsRequired.clear();
		for(Building b : this.buildings) {
			BuildingType requiredBuilding = b.consumeResources(this.resourcesMap);
			buildingsRequired.put(b.getId(), requiredBuilding);
			
			spawnBuildings();
		}
		
		// Project buildings on the tilemap.
		BuildingProjector.project(this.buildings, this.tilemap);

		// Update the tilemap.
		this.tilemap.update();
		
		if(this.checkbox1.isChecked()) {
			this.zoneDrawingGui.update(this.zoneMap, this.tileSelector);
			this.zoneMapLayer.update();
		}
		
		//Update stats
		this.cityStats.update(buildings);
		this.statsGui.setMoney(this.cityStats.getMoney());
		this.statsGui.setPopulation(this.cityStats.getPopulation());
		this.tileSelector.update();
		
	}
	
	/**
	 * Renders all the simulation.
	 */
	public void render() {
		this.window.clear(Color.WHITE);
		/////////////

		this.window.draw(this.tilemap);
		if(this.checkbox1.isChecked()) {
			this.window.draw(this.zoneMapLayer);
			this.window.draw(this.zoneDrawingGui);
		}
		this.window.draw(this.tileSelector);
		this.window.draw(this.statsGui);
		this.window.draw(checkbox1);
		
		if(this.displayTileInfo)
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

	/**
	 * Handles the event.
	 * @param event : the JSFML event to handle
	 */
	public void handleEvent(Event event) {
		if(event.type == Event.Type.MOUSE_BUTTON_RELEASED && event.asMouseButtonEvent().button == Mouse.Button.MIDDLE) {
			this.displayTileInfo = !this.displayTileInfo;
		}
		
		this.checkbox1.handleEvent(event);
		
		if(this.checkbox1.isChecked())
			this.zoneDrawingGui.handleEvent(event);
	}
}
