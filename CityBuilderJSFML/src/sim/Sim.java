package sim;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.ShaderSourceException;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import graphics.Tile.TileType;
import graphics.BuildingRenderer;
import graphics.FontManager;
import graphics.LightLayer;
import graphics.TextureManager;
import graphics.Tile;
import graphics.TileMap;
import graphics.ZoneMapLayer;
import gui.BlueprintGui;
import gui.CheckBox;
import gui.GameSpeedGui;
import gui.GraphStatsGui;
import gui.LogGui;
import gui.StatsGui;
import gui.TextInputPool;
import gui.TileInfoGui;
import gui.TileSelector;
import gui.ZoneDrawingGui;
import maths.Distance;
import world.Building;
import world.Building.BuildingType;
import world.Citizen;
import world.Resource.ResourceType;
import world.CityStats;
import world.Need;
import world.Resource;
import world.ResourcesMap;
import world.ResourcesStack;
import world.Zone;
import world.Zone.ZoneClass;
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
	protected ResourcesMap cachedResourceMap;
	protected List<Building> buildings;
	protected CityStats cityStats;
	protected TextureManager textureManager;
	protected FontManager fontManager;
	protected StatsGui statsGui;
	protected BuildingRenderer buildingRenderer;
	protected TileSelector tileSelector;
	protected TileInfoGui tileInfoGui;
	protected boolean displayTileInfo;
	protected Stack<Map<Integer, Building.BuildingType>> buildingStackRequired;
	protected ZoneMap zoneMap;
	protected ZoneMapLayer zoneMapLayer;
	protected ZoneDrawingGui zoneDrawingGui;
	protected GameSpeedGui gameSpeedGui;
	protected Time simulationSpeedTimer;
	protected View gameView;
	protected View staticView;
	protected GraphStatsGui graphStatsGui;
	protected LogGui logGui;
	protected List<CheckBox> checkboxes;
	protected int zoneDrawingCheckboxID;
	protected int cityGraphStatsCheckboxID;
	protected int blueprintCheckboxID;
	protected LightLayer lightLayer;
	protected int dayhour;
	protected int daycount;
	protected BlueprintGui blueprintGui;
	protected TextInputPool textInputPool;
	protected CheckBox logCheckBox;
	
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
		
		// Instanciate the building renderer.
		this.buildingRenderer = new BuildingRenderer(TILE_SIZE, this.textureManager);
		
		// Create the resources map.
		this.resourcesMap = new ResourcesMap(TILEMAP_SIZE);
		
		// Clone the resources map
		this.cachedResourceMap = this.resourcesMap.cloneResourcesMap();
		
		// Create the buildings list.
		this.buildings = new ArrayList<Building>();
		
		// Create the checkbox for the log
		this.logCheckBox = new CheckBox(10, 100, this.textureManager, this.fontManager, "Afficher les messages", 0);
		
		// Create the checkboxes.
		int checkboxID = 0;
		this.checkboxes = new ArrayList<CheckBox>();
		this.zoneDrawingCheckboxID = checkboxID;
		this.checkboxes.add(new CheckBox(10, 120, this.textureManager, this.fontManager, "Afficher les zones", zoneDrawingCheckboxID));
		
		checkboxID++;
		this.cityGraphStatsCheckboxID = checkboxID;
		this.checkboxes.add(new CheckBox(10, 140, this.textureManager, this.fontManager, "Afficher les statistiques", cityGraphStatsCheckboxID));
		
		checkboxID++;
		this.blueprintCheckboxID = checkboxID;
		this.checkboxes.add(new CheckBox(10, 160, this.textureManager, this.fontManager, "Gestion les plans", blueprintCheckboxID));
		
		// Create the city stats.
		this.cityStats = new CityStats();
		
		// Create the city graph stats gui.
		this.graphStatsGui = new GraphStatsGui(getWindow().getSize(), this.fontManager);
		
		// Create the zoneMap
		this.zoneMap = new ZoneMap(TILEMAP_SIZE.x, TILEMAP_SIZE.y);
		
		// Create the game speed GUI
		this.gameSpeedGui = new GameSpeedGui(this.textureManager, this.fontManager, this.window.getSize().x - 80, 20);
		
		// Create the logGui
		this.logGui = new LogGui(this.fontManager);
		
		// Instanciate the TextInputPool
		this.textInputPool = new TextInputPool(this.window);
				
		// Create the blueprint gui.
		try {
			this.blueprintGui = new BlueprintGui(this.fontManager, this.textInputPool);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Create the zoneMapLayer
		this.zoneMapLayer = new ZoneMapLayer(this.zoneMap);
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.FREE, new Color(12, 52, 30, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.COMMERCIAL, new Color(125, 193, 129, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.CULTURAL, new Color(51, 153, 255, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.INDUSTRY, new Color(227, 168, 87, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.ROAD, new Color(220, 220, 220, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.RESIDENTIAL, new Color(70, 0, 0, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.PUBLIC_SERVICE, new Color(255, 109, 201, 170));
		
		// Houses.
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(31, 20)));
		
		// Roads
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(31, 19)));
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(32, 19)));
		
		// Inits the tilemap.
		this.tilemap = new TileMap(TILEMAP_SIZE, TILE_SIZE);
		this.tilemap.addTypeColor(TileType.TERRAIN_GRASS, new Color(0, 70, 0));
		this.tilemap.setTiles(this.tiles);
		
		// Inits the building renderer.
		this.buildingRenderer.setColorPlaceholder(BuildingType.HOUSE, new Color(70, 0, 0));
		this.buildingRenderer.setColorPlaceholder(BuildingType.ROAD, new Color(190, 190, 190));
		this.buildingRenderer.setColorPlaceholder(BuildingType.GENERATOR, new Color(227, 168, 87));
		this.buildingRenderer.setColorPlaceholder(BuildingType.HYDROLIC_STATION, new Color(51, 153, 255));
		this.buildingRenderer.setColorPlaceholder(BuildingType.GROCERY_STORE, new Color(125, 193, 129));
		this.buildingRenderer.setColorPlaceholder(BuildingType.ANTENNA_4G, new Color(63, 63, 63));
		this.buildingRenderer.setColorPlaceholder(BuildingType.CASINOS, new Color(255, 122, 159));
		this.buildingRenderer.setColorPlaceholder(BuildingType.CINEMA, new Color(114, 210, 255));
		this.buildingRenderer.setColorPlaceholder(BuildingType.FIRE_STATION, new Color(255, 116, 2));
		this.buildingRenderer.setColorPlaceholder(BuildingType.HOSPITAL, new Color(255, 233, 229));
		this.buildingRenderer.setColorPlaceholder(BuildingType.MALL, new Color(255, 251, 33));
		this.buildingRenderer.setColorPlaceholder(BuildingType.POLICE_STATION, new Color(45, 84, 255));
		this.buildingRenderer.setColorPlaceholder(BuildingType.PUB, new Color(185, 255, 173));
		this.buildingRenderer.setColorPlaceholder(BuildingType.RESTAURANT, new Color(255, 250, 0));
		this.buildingRenderer.setColorPlaceholder(BuildingType.SCHOOL, new Color(255, 219, 137));
		this.buildingRenderer.setColorPlaceholder(BuildingType.STADIUM, new Color(192, 192, 192));
		
		this.buildingRenderer.setTextureRect(BuildingType.GROCERY_STORE, new FloatRect(0.f, 0.f, 64.f, 32.f));
		this.buildingRenderer.setTextureRect(BuildingType.HOUSE, new FloatRect(64.f, 0.f, 32.f, 32.f));
		this.buildingRenderer.setTextureRect(BuildingType.GENERATOR, new FloatRect(96.f, 0.f, 16.f, 16.f));
		this.buildingRenderer.setTextureRect(BuildingType.HYDROLIC_STATION, new FloatRect(96.f, 16.f, 16.f, 16.f));
		this.buildingRenderer.setTextureRect(BuildingType.ANTENNA_4G, new FloatRect(112.f, 0.f, 16.f, 16.f));
		this.buildingRenderer.setTextureRect(BuildingType.CASINOS, new FloatRect(128.f, 0.f, 64.f, 64.f));
		
		// The stack of the maps which contains the required buildings of everyone.
		this.buildingStackRequired = new Stack<Map<Integer, Building.BuildingType>>();

		// Instanciate the tileInfoGui
		this.tileInfoGui = new TileInfoGui(this.tiles, this.fontManager);
		
		// Instanciate the zone drawing GUI.
		this.zoneDrawingGui = new ZoneDrawingGui(this.textureManager, this.fontManager);
		
		// Building spawn timer.
		this.simulationSpeedTimer = Time.ZERO;
		
		// Views.
		this.staticView = new View();
		this.staticView.setSize(getWindow().getView().getSize());
		this.staticView.setCenter(getWindow().getView().getCenter());
		
		this.gameView = (View)getWindow().getView();
		
		// Day/night cycle.
		this.dayhour = 12;
		this.daycount = 0;
		
		// Light layer.
		try {
			this.lightLayer = new LightLayer(new Vector2i((int)(TILEMAP_SIZE.x * TILE_SIZE.x), (int)(TILEMAP_SIZE.y * TILE_SIZE.y)));
			this.lightLayer.setAlpha(255);
			this.lightLayer.virtualDraw();
		}
		catch (TextureCreationException | IOException | ShaderSourceException e) {
			this.logGui.write("Error: could not create the light layer.\n", LogGui.ERROR);
		}
	}
	
	/**
	 * Returns a reference to the checkbox.
	 * 
	 * @param id : the id of the checkbox
	 * @return the checkbox
	 */
	public CheckBox getCheckBox(int id) {
		for(CheckBox cb : this.checkboxes) {
			if(cb.getValue() == id)
				return cb;
		}
		
		return null;
	}
	
	/**
	 * Returns true if the checkbox is checked.
	 * 
	 * @param id : the id of the checkbox
	 * @return true if checked, fasle otherwise
	 */
	public boolean isCheckBoxChecked(int id) {
		return getCheckBox(id).isChecked();
	}
	
	/**
	 * Returns true if no checkbox is checked.
	 * 
	 * @return true if no checkbox is checked, false otherwise
	 */
	public boolean noCheckBoxChecked() {
		for(CheckBox cb : this.checkboxes)
			if(cb.isChecked())
				return false;
			
		return true;
	}
	
	/**
	 * Returns true if no checkbox except the given ID is checked.
	 * 
	 * @return true if the specified checkbox is the only one checked
	 */
	public boolean isOnlyChecked(int id) {
		for(CheckBox cb : this.checkboxes) {
			if(cb.isChecked() && cb.getValue() != id)
				return false;
		}
		
		return isCheckBoxChecked(id);
	}
	
	/**
	 * Returns the building object with the given id.
	 * 
	 * @param buildingId : id to look for
	 * @param buildingList : list to search in
	 * @return the building object, null if not found
	 */
	public Building getBuilding(int buildingId, List<Building> buildingList) {
		for(Building b : buildingList) {
			if(b.getId() == buildingId)
				return b;
		}
		
		return null;
	}
	
	/**
	 * Counts the number of buildings per building type.
	 * Type NONE is not counted.
	 * 
	 * @param buildings : the map of the buildings (association of building's ID and building type)
	 * @return the map of the building counts (association of building's type and number of buildings in that category)
	 */
	public Map<Building.BuildingType, Integer> countBuildingsPerType(Map<Integer, Building.BuildingType> buildings) {
		Map<Building.BuildingType, Integer> buildingCounts = new HashMap<Building.BuildingType, Integer>();
		
		for(Map.Entry<Integer, Building.BuildingType> entry : buildings.entrySet()) {
			Building.BuildingType buildingType = entry.getValue();
			
			// Do not count NONE.
			if(buildingType == Building.BuildingType.NONE)
				continue;
			
			if(buildingCounts.containsKey(buildingType)) {
				Integer count = buildingCounts.get(buildingType);
				count = new Integer(count.intValue() + 1);
				buildingCounts.put(buildingType, count);
			}
			else {
				buildingCounts.put(buildingType, 1);
			}
		}
		
		return buildingCounts;
	}
	
	/**
	 * Returns the map entry with the most buildings counted.
	 * 
	 * @param buildingCounts : the map of the building counts (association of building's type and number of buildings in that category)
	 * @return The max entry in the building counts map.
	 */
	public Map.Entry<Building.BuildingType, Integer> getMostRequiredBuildingType(Map<Building.BuildingType, Integer> buildingCounts) {
		Map.Entry<Building.BuildingType, Integer> maxEntry = null;
		for(Map.Entry<Building.BuildingType, Integer> entry : buildingCounts.entrySet()) {
			if(maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		
		return maxEntry;
	}
	
	/**
	 * Computes the average position of all the buildings of the given type (limited to the given number of buildings).
	 * 
	 * @param buildings : the list of all the buildings
	 * @param buildingType : the buildings' type to take in account
	 * @param numberOfBuildingsToCount : the number of buildings to count for the average position
	 * @return The average position in tile coordinates.
	 */
	public Vector2i getBuildingsAveragePosition(Map<Integer, Building.BuildingType> buildings, Building.BuildingType buildingType, int numberOfBuildingsToCount) {
		Vector2i position = new Vector2i(0, 0);
		
		// Now sum the position of every building of the type specified.
		for(Map.Entry<Integer, Building.BuildingType> entry : buildings.entrySet()) {
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
		
		// Divide by the number of buildings counted to get the average position.
		return new Vector2i((int)(position.x / numberOfBuildingsToCount), (int)(position.y / numberOfBuildingsToCount));
	}
	
	/**
	 * Returns the distance to the furthest building from the given point.
	 * 
	 * @param buildings : the list of all the buildings
	 * @param buildingType : the buildings' type to take in account
	 * @param point : the point to compute distance from
	 * @return The distance between the furthest building of the given type to the given point.
	 */
	public float getFurthestBuildingTo(Map<Integer, Building.BuildingType> buildings, Building.BuildingType buildingType, Vector2i point) {
		float radius = 0.f;
		
		for(Map.Entry<Integer, Building.BuildingType> entry : buildings.entrySet()) {
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
					float distance = (float)Distance.euclidean(point, centerPosition);
					
					if(distance > radius)
						radius = distance;
				}
			}
		}
		
		return radius;
	}
	
	/**
	 * Returns true if there is a collision with another building (or is outside the map).
	 * 
	 * @param hitbox : the hitbox to test
	 * @return true in case of collision, false otherwise
	 */
	public boolean collideWithOtherBuildings(IntRect hitbox) {
		// Must be fully inside the map.
		if(hitbox.left < 0 || hitbox.top < 0 || hitbox.left + hitbox.width >= TILEMAP_SIZE.x || hitbox.top + hitbox.height >= TILEMAP_SIZE.y)
			return true;
		
		for(Building b : this.buildings) {
			if(hitbox.intersection(b.getHitbox()) != null)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Starts from the given left top position and check if the whole hitbox is in allowed zone classes.
	 * 
	 * @param leftTop : the position to start checking from
	 * @param hitbox : the hitbox (only width and height are used)
	 * @param zoneClasses : the list of the zones allowed
	 * @return true if the hitbox is in allowed zone classes, false otherwise
	 */
	public boolean checkZoneCompatibility(Vector2i leftTop, IntRect hitbox, List<Zone.ZoneClass> zoneClasses) {
		return checkZoneCompatibility(leftTop.x, leftTop.y, hitbox, zoneClasses);
	}
	
	/**
	 * Starts from the given left top position and check if the whole hitbox is in allowed zone classes.
	 * 
	 * @param x : the x component of the position to start checking from
	 * @param y : the y component of the position to start checking from
	 * @param hitbox : the hitbox (only width and height are used)
	 * @param zoneClasses : the list of the zones allowed
	 * @return true if the hitbox is in allowed zone classes, false otherwise
	 */
	public boolean checkZoneCompatibility(int x, int y, IntRect hitbox, List<Zone.ZoneClass> zoneClasses) {
		for(int rx = x ; rx < Math.min(x + hitbox.width, TILEMAP_SIZE.x) ; rx++) {
			for(int ry = y ; ry < Math.min(y + hitbox.height, TILEMAP_SIZE.y) ; ry++) {
				// Get the zone.
				Zone zone = this.zoneMap.getZoneMap().get(ry).get(rx);
				
				// check if the zone is suitable
				for(ZoneClass zoneBuilding : zoneClasses) {
					if(!zone.getType().equals(zoneBuilding)) {
						return false;
					}
				}
			}

		}
		
		return true;
	}
	
	/**
	 * Sums the resources available under the whole hitbox.
	 * 
	 * @param leftTop : the position to start checking from
	 * @param hitbox : the hitbox (only width and height are used)
	 * @return the resources available under the hitbox
	 */
	public ResourcesStack getResourcesUnderHitbox(Vector2i leftTop, IntRect hitbox) {
		return getResourcesUnderHitbox(leftTop.x, leftTop.y, hitbox);
	}
	
	/**
	 * Sums the resources available under the whole hitbox.
	 * 
	 * @param x : the x component of the position to start checking from
	 * @param y : the y component of the position to start checking from
	 * @param hitbox : the hitbox (only width and height are used)
	 * @return he resources available under the hitbox
	 */
	public ResourcesStack getResourcesUnderHitbox(int x, int y, IntRect hitbox) {
		ResourcesStack rstack = new ResourcesStack();

		for(int rx = x ; rx < Math.min(x + hitbox.width, TILEMAP_SIZE.x) ; rx++) {
			for(int ry = y ; ry < Math.min(y + hitbox.height, TILEMAP_SIZE.y) ; ry++) {
				rstack.add(resourcesMap.getResources(rx, ry));
			}
		}

		return rstack;
	}
	
	/**
	 * Checks the needs and precise the missing resources if any.
	 * 
	 * @param needs : the list of the needs to check
	 * @param rstack : the resources stack of the available resources
	 * @param missingResources : reference to return the map of the missing resources
	 * @return true if all the needs are satisfied, else otherwise
	 */
	public boolean checkNeeds(List<Need> needs, ResourcesStack rstack, Map<Resource.ResourceType, Integer> missingResources) {
		boolean allNeedsSatisfied = true;
		
		for(Need n : needs) {
			float minAmount = n.amount * n.fillFactor;

			// If one need is not satisfied to its minimum, we quit.
			if(rstack.get(n.type) < minAmount) {
				allNeedsSatisfied = false;
				int count = missingResources.get(n.type).intValue();
				count += 1;
				missingResources.put(n.type, count);
			}
		}
		
		return allNeedsSatisfied;
	}
	
	/**
	 * Counts the number of buildings in the given area.
	 * 
	 * @param centerOfArea : the center of the area
	 * @param range : the radius of the area
	 * @param buildingList : the list of buildings to consider
	 * @return the number of buildings in the area
	 */
	public int countBuildingsInArea(Vector2i centerOfArea, int range, List<Building> buildingList) {
		int inRange = 0;
		
		for(Building building : buildingList) {
			Vector2i buildingCenter = new Vector2i(building.getHitbox().left + building.getHitbox().width / 2,
												   building.getHitbox().top + building.getHitbox().height / 2);

			int distance = (int)Distance.euclidean(buildingCenter, centerOfArea);
			if(distance < range) {
				inRange++;
			}
		}
		
		return inRange;
	}
	
	/**
	 * Returns the list of the buildings of the given type.
	 * 
	 * @param buildingList : the building list to filter
	 * @param buildingType : the type of buildings to keep
	 * @return the list filtered
	 */
	public List<Building> getBuildingsOfType(List<Building> buildingList, Building.BuildingType buildingType) {
		List<Building> buildingsOfGivenType = new ArrayList<Building>();
		
		for(Building b : buildingList) {
			if(b.getType() == buildingType)
				buildingsOfGivenType.add(b);
		}
		
		return buildingsOfGivenType;
	}
	
	public List<Building> getBuildingsOfType(Map<Integer, Building.BuildingType> buildingMap, List<Building> buildingList, Building.BuildingType buildingType) {
		List<Building> buildingsOfGivenType = new ArrayList<Building>();
		
		for(Map.Entry<Integer, Building.BuildingType> entry : buildingMap.entrySet()) {
			Building b = getBuilding(entry.getKey(), buildingList);
			
			if(b == null)
				continue;
			
			if(b.getType() == buildingType)
				buildingsOfGivenType.add(b);
		}
		
		return buildingsOfGivenType;
	}
	
	/**
	 * Spawns the new buildings.
	 */
	public void spawnBuildings() {
		// Look into the required buildings stack.
		if(this.buildingStackRequired.empty())
			return;
		
		// The map collecting the required buildings.
		// ID <-> Required building type.
		Map<Integer, Building.BuildingType> buildingsRequired = this.buildingStackRequired.peek();
		
		// First count the required buildings.
		// Building type <-> how many times required
		Map<Building.BuildingType, Integer> buildingCounts = countBuildingsPerType(buildingsRequired);
		
		// Get the most required building type.
		// Building type <-> how many times required
		Map.Entry<Building.BuildingType, Integer> mostRequiredBuildingTypeEntry = getMostRequiredBuildingType(buildingCounts);
		
		// If no building type has been requested, we leave.
		if(mostRequiredBuildingTypeEntry == null)
			return;
		
		// Create a fake building.
		Building requiredBuilding = new Building(mostRequiredBuildingTypeEntry.getKey(), new Vector2i(0, 0));
		
		// Check if there is enough unemployed people.
		if(this.cityStats.getUnemployment() < requiredBuilding.getMinEmployees()) {
			this.logGui.write("Not enough people to employ : " + this.cityStats.getUnemployment() + " / " + requiredBuilding.getMinEmployees(), LogGui.WARNING);
			return;
		}

		// Compute the average position, aka the center of the search area.
		Vector2i centerOfSearchArea = getBuildingsAveragePosition(buildingsRequired, mostRequiredBuildingTypeEntry.getKey(), mostRequiredBuildingTypeEntry.getValue());
		
		// Get the furthest building from the average position, to compute the radius of the search area.
		float radius = getFurthestBuildingTo(buildingsRequired, mostRequiredBuildingTypeEntry.getKey(), centerOfSearchArea);
		
		// We may need to expand the radius.
		if(requiredBuilding.getRange() > radius)
			radius = requiredBuilding.getRange();
		
		// We use squared radius and squared euclidean distance for performance.
		double squaredRadius = Math.pow(radius, 2);
		
		// Map of the considered positions with the number of requiring building in range.
		// Position <-> number of buildings in range
		Map<Vector2i, Integer> candidatesPositions = new HashMap<Vector2i, Integer>();
		
		// Map of the positions where it lacks resources only with the number of requiring building in range.
		// Position <-> number of buildings in range
		Map<Vector2i, Integer> candidatesPositionsLackingResources = new HashMap<Vector2i, Integer>();
		
		// Map of the positions (which are in valid zone) with the number of requiring building in range.
		// Position <-> number of buildings in range
		Map<Vector2i, Integer> candidatesPositionsWithValidZone = new HashMap<Vector2i, Integer>();
		
		// Missing resources for the required building.
		// Resource type <-> how many missing
		Map<Resource.ResourceType, Integer> missingResources = new HashMap<Resource.ResourceType, Integer>();
		
		// Initiates missing resources to 0.
		for(Resource.ResourceType rtype : Resource.ResourceType.values())
			missingResources.put(rtype, 0);
		
		// Check all resource map in square range.
		for(int x = Math.max(0, centerOfSearchArea.x - (int)radius) ; x < Math.min(resourcesMap.getSize().x, centerOfSearchArea.x + radius + 1) ; ++x) {
			for(int y = Math.max(0, centerOfSearchArea.y - (int)radius) ; y < Math.min(resourcesMap.getSize().y, centerOfSearchArea.y + radius + 1) ; ++y) {
				// Check only in radius.
				if(Distance.squaredEuclidean(centerOfSearchArea, new Vector2i(x, y)) <= squaredRadius) {
					// Check collision with other buildings.
					IntRect candidateHitbox = new IntRect(x, y, requiredBuilding.getHitbox().width, requiredBuilding.getHitbox().height);

					if(collideWithOtherBuildings(candidateHitbox)) {
						// This position is not suitable.
						continue;
					}

					// Check zone compatibility.
					if(!checkZoneCompatibility(x, y, requiredBuilding.getHitbox(), requiredBuilding.getZoneClasses())) {
						// This zone is not suitable
						continue;
					}

					// Check how many buildings (which required the building construction) are in range of the required building.
					List<Building> buildingsRequiring = getBuildingsOfType(buildingsRequired, this.buildings, requiredBuilding.getType());
					int inRange = countBuildingsInArea(centerOfSearchArea, requiredBuilding.getRange(), buildingsRequiring);

					candidatesPositionsWithValidZone.put(new Vector2i(x, y), inRange);
				}
			}
		}

		// If we don't find any suitable zone, we need to notify the player.
		if(candidatesPositionsWithValidZone.isEmpty()) {
			this.logGui.write("No suitable position found for : " + mostRequiredBuildingTypeEntry.getKey().toString(), LogGui.WARNING);
			this.logGui.write("You should create one of the following zone(s), (near position {" + centerOfSearchArea.x + ", " + centerOfSearchArea.y + "}) :", LogGui.NORMAL);

			List<Zone.ZoneClass> suitableZonesForRequiredBuilding = Building.getSuitableZones(mostRequiredBuildingTypeEntry.getKey());
			for(Zone.ZoneClass z : suitableZonesForRequiredBuilding)
				this.logGui.write("\t- " + z.toString(), false, LogGui.NORMAL);

			// We stop here.
			return;
		}
		
		for(Map.Entry<Vector2i, Integer> entry : candidatesPositionsWithValidZone.entrySet()) {
			// Decompose the map's entry.
			int x = entry.getKey().x;
			int y = entry.getKey().y;
			int inRange = entry.getValue();
			
			// Get the resources available for the building.
			ResourcesStack rstack = getResourcesUnderHitbox(x, y, requiredBuilding.getHitbox());

			// Check if they satisfy the needs.
			boolean allNeedsSatisfied = checkNeeds(requiredBuilding.getNeeds(), rstack, missingResources);
			
			// Add to the candidates positions if all resources are available.
			if(allNeedsSatisfied)
				candidatesPositions.put(new Vector2i(x, y), inRange);
			else
				candidatesPositionsLackingResources.put(new Vector2i(x, y), inRange);
		}
		
		// Check the position which reach the most buildings AND is the closer to the center of the search area.
		Map.Entry<Vector2i, Integer> bestPosition = null;
		double mindistance = Double.MAX_VALUE;
		
		for(Map.Entry<Vector2i, Integer> entry : candidatesPositions.entrySet()) {
			if((bestPosition == null) || (entry.getValue() >= bestPosition.getValue() && mindistance > Distance.euclidean(entry.getKey(), centerOfSearchArea))) {
				bestPosition = entry;
				mindistance = Distance.euclidean(bestPosition.getKey(), centerOfSearchArea);
			}
		}
		
		// Add the building to the position.
		if(bestPosition != null) {
			this.buildings.add(new Building(mostRequiredBuildingTypeEntry.getKey(), bestPosition.getKey()));
			
			// We spawned the building, so get it out of the stack.
			this.buildingStackRequired.pop();
			
			this.logGui.write("Spawning : " + mostRequiredBuildingTypeEntry.getKey().toString() + " @ " + bestPosition.getKey().x + ", " + bestPosition.getKey().y, LogGui.SUCCESS);
			this.logGui.write("\tdistance to CoSA: " + Distance.euclidean(bestPosition.getKey(), centerOfSearchArea), false, LogGui.SUCCESS);
			this.logGui.write("\tefficiency: " + bestPosition.getValue() + "/" + mostRequiredBuildingTypeEntry.getValue(), false, LogGui.SUCCESS);
		}
		else {
			// Get the most rare resource.
			Map.Entry<Resource.ResourceType, Integer> mostRareResourceEntry = null;
			for(Map.Entry<Resource.ResourceType, Integer> entry : missingResources.entrySet()) {
				if(mostRareResourceEntry == null || entry.getValue() > mostRareResourceEntry.getValue()) {
					mostRareResourceEntry = entry;
				}
			}
			
			Resource.ResourceType rareResource = mostRareResourceEntry.getKey();
			
			// Since the roads are manually spawned by the player, we can't ask to spawn them.
			if(rareResource != ResourceType.ROAD_PROXIMITY) {
				// Every building asking for the current building type should ask for its pre-requisite.
				Map<Integer, Building.BuildingType> prerequisiteBuildingMap = new HashMap<Integer, Building.BuildingType>();
				for(Map.Entry<Integer, Building.BuildingType> entry : buildingsRequired.entrySet()) {
					if(requiredBuilding.getType() == entry.getValue())
						prerequisiteBuildingMap.put(entry.getKey(), Building.getBuildingTypeGenerating(rareResource));
				}
				
				// We add a new building to build on top of the stack.
				// This way, once the prerequiste building built, it will be poped off the stack and the original building will be built.
				this.buildingStackRequired.push(prerequisiteBuildingMap);
				
				this.logGui.write("No suitable position found for : " + mostRequiredBuildingTypeEntry.getKey().toString(), LogGui.WARNING);
				this.logGui.write("Most rare resource : " + rareResource.toString(), false, LogGui.WARNING);
				this.logGui.write("Asking to spawn : " + Building.getBuildingTypeGenerating(rareResource).toString(), false, LogGui.WARNING);
			}
		}
	}
	
	/**
	 * Spawn road with the zone map
	 */
	public void spawnRoad() {
		// run the map
		for(int y = 0 ; y < this.zoneMap.getSize().y ; y++) {
			for(int x = 0 ; x < this.zoneMap.getSize().x ; x++) {
				// check if a zone type is road
				if(this.zoneMap.getZoneMap().get(y).get(x).getType().equals(ZoneClass.ROAD)) {
					// check if no building in this zone
					for(int i = 0 ; i < this.buildings.size() ;) {
						// if building remove it (ONLY if not a ROAD)
						if(this.buildings.get(i).getType() != Building.BuildingType.ROAD && this.buildings.get(i).getHitbox().contains(x, y)) {
							this.logGui.write("Removed building : " + this.buildings.get(i).getId(), LogGui.SUCCESS);
							this.buildings.remove(this.buildings.get(i));
						}
						else {
							i++;
						}
					}
					
					// we spawn the road
					this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(x, y)));
				}
			}
		}
		
		this.logGui.write("New road(s) added.", true, LogGui.SUCCESS);
		this.zoneDrawingGui.setNewRoadAdded(false);
	}
	
	/**
	 * Spawns new houses depending on the attractivity.
	 */
	public void spawnNewcomers() {
		// Check attractivity.
		if(this.cityStats.getAttractivity(Zone.ZoneClass.COMMERCIAL) < 1.f) {
			this.logGui.write("Attractivity too small.", LogGui.ERROR);
			return;
		}

		// Find a new valid zone, searching from the center of the city.
		Vector2f citycenterf = new Vector2f(0.f, 0.f);

		for(Building b : this.buildings) {
			citycenterf = Vector2f.add(citycenterf, new Vector2f(b.getHitbox().left, b.getHitbox().top));
		}

		citycenterf = Vector2f.mul(citycenterf, 1.f / this.buildings.size());
		Vector2i citycenter = new Vector2i((int)citycenterf.x, (int)citycenterf.y);

		// Create a fake building.
		Building fakehouse = new Building(Building.BuildingType.HOUSE, new Vector2i(0, 0));

		// List of positions already checked.
		List<Vector2i> exploredPositions = new ArrayList<Vector2i>();

		// Closest position found.
		Vector2i closestPosition = null;
		double closestPositionDistance = Double.MAX_VALUE;
		
		//Look for roads.
		for(Building b : this.buildings) {
			if(b.getType() != Building.BuildingType.ROAD)
				continue;

			// Look for valid zones around the road.
			for(int x = Math.max(0, b.getHitbox().left - 4) ; x < Math.min(resourcesMap.getSize().x, b.getHitbox().left + 4) ; ++x) {
				for(int y = Math.max(0, b.getHitbox().top - 4) ; y < Math.min(resourcesMap.getSize().y, b.getHitbox().top + 4) ; ++y) {
					// Check if the position has already been tested.
					boolean alreadyExplored = false;

					for(Vector2i exploredPosition : exploredPositions) {
						if(exploredPosition.x == x && exploredPosition.y == y)
							alreadyExplored = true;
					}

					if(alreadyExplored)
						continue;
					
					// Computes the distance to the center of the city.
					double distance = Distance.euclidean(citycenter.x, citycenter.y, x, y);

					// Check only positions that are closer to the one found.
					if(distance >= closestPositionDistance) {
						continue;
					}
					
					// Check collision with other buildings.
					IntRect candidateHitbox = new IntRect(x, y, fakehouse.getHitbox().width, fakehouse.getHitbox().height);

					if(collideWithOtherBuildings(candidateHitbox)) {
						// This position is not suitable.
						continue;
					}

					// Check zone compatibility.
					if(!checkZoneCompatibility(x, y, fakehouse.getHitbox(), fakehouse.getZoneClasses())) {
						// This zone is not suitable
						continue;
					}
					
					// Get the resources available for the building.
					ResourcesStack rstack = getResourcesUnderHitbox(x, y, fakehouse.getHitbox());

					// Keep this position if roads are available.
					if(rstack.get(Resource.ResourceType.ROAD_PROXIMITY) > 0.f) {
						closestPositionDistance = distance;
						closestPosition = new Vector2i(x, y);
					}
				}
			}
		}
		
		if(closestPosition != null) {
			Building house = new Building(Building.BuildingType.HOUSE, closestPosition);
	
			// Spawn the building.
			this.buildings.add(house);
			this.logGui.write("A new house has come, implemented at position {" + closestPosition.x + ", " + closestPosition.y + "}.", LogGui.SUCCESS);
		}
	}

	/**
	 * Sets the static view to draw GUI & static elements on screen.
	 */
	public void setStaticView() {
		// Save the game view.
		this.gameView = (View)getWindow().getView();
		
		// Set the static view.
		getWindow().setView(this.staticView);
	}
	
	/**
	 * Sets the game view to draw the world.
	 */
	public void setGameView() {
		// No need to save the static view, since it's always the same.
		// Set the game view.
		getWindow().setView(this.gameView);
	}
	
	/**
	 * Updates the day/night.
	 */
	public void updateDayNight() {
		this.dayhour += 3;
		this.dayhour %= 24;
		
		if(this.dayhour == 0)
			this.daycount++;
	}
	
	/**
	 * Updates the visual render of the day/night.
	 * @throws TextureCreationException 
	 */
	public void updateDayNightVisual(float simulationTime, float dt) throws TextureCreationException {
		int goal = 0;
		
		if(this.dayhour < 9) {
			goal = 255;
		}
		else if(this.dayhour <= 17) {
			goal = 255;
		}
		else if(this.dayhour <= 23) {
			goal = 0;
		}
		
		float timeRemaining = 1.f - simulationTime;
		
		// Due to float computation errors, we have to check the time.
		if(timeRemaining <= 0.f)
			return;
		
		int actual = this.lightLayer.getAlpha();
		int delta = goal - actual;
		float timestep = timeRemaining / dt;
		
		if(delta != 0) {
			int step = (int)(delta / timestep);
			actual += step;
			
			if(delta > 0 && actual > goal)
				actual = goal;
			else if(delta < 0 && actual < goal)
				actual = goal;

			this.lightLayer.setAlpha(actual);
			this.lightLayer.virtualDraw();
		}

	}
	
	/**
	 * Updates all the simulation.
	 * @param dt : frame of time to use
	 */
	public void update(Time dt) {
		// Update the simulation timer.
		if(!this.gameSpeedGui.isInPause()) {
				this.simulationSpeedTimer = Time.add(this.simulationSpeedTimer, Time.mul(dt, this.gameSpeedGui.getSpeedCoeff()));
		}
		
		// Take real-time input.
		handleInput(dt);
		
		// Spawn road
		if(!this.gameSpeedGui.isInPause() && this.simulationSpeedTimer.asSeconds() >= 1.f) {
			// If there has been new road zones added.
			if(this.zoneDrawingGui.newRoadAdded()) {
				spawnRoad();
			}
			
			// Spawn the newcomers.
			spawnNewcomers();
			
			// Reset the resources.
			this.resourcesMap.reset();
			
			// Get the list of the houses (to check clients and employees).
			List<Building> houses = getBuildingsOfType(this.buildings, Building.BuildingType.HOUSE);
			
			// Generate resources.
			for(Building b : this.buildings) {
				// Check the clients and employees.
				b.checkClients(houses);
				b.checkEmployees(houses);
				
				b.generateResources(this.resourcesMap, this.buildings);
			}
			
			for(Building b : this.buildings) {
				if(b.isEvolvable()) {
					if(b.checkLevelUp(this.resourcesMap)) {
						this.logGui.write("The building " + b.getId() + " leveled up to level " + b.getLevel(), LogGui.SUCCESS);
					}
				}
			}

			// Clone the resource map.
			this.cachedResourceMap = this.resourcesMap.cloneResourcesMap();
		}
		
		// We update tile infos after generate.
		if(this.displayTileInfo)
			this.tileInfoGui.update(this.cachedResourceMap, this.tileSelector, this.buildings);
			
		if(!this.gameSpeedGui.isInPause() && this.simulationSpeedTimer.asSeconds() >= 1.f) {
			// Consume resources and get required buildings.
			Map<Integer, Building.BuildingType> buildingsRequired = new HashMap<Integer, Building.BuildingType>();
			
			for(Building b : this.buildings) {
				BuildingType requiredBuilding = b.consumeResources(this.resourcesMap);
				
				// Don't do anything if none required.
				if(requiredBuilding != BuildingType.NONE && requiredBuilding != BuildingType.ROAD) {
					buildingsRequired.put(b.getId(), requiredBuilding);
				}
			}
			
			// We only push a requirement for a new building if there is none waiting.
			if(this.buildingStackRequired.isEmpty() && !buildingsRequired.isEmpty()) {
				this.buildingStackRequired.push(buildingsRequired);
			}
			
			// Spawn buildings.
			spawnBuildings();
			
			// Display the building stack size if > 0.
			if(this.buildingStackRequired.size() > 0) {
				this.logGui.write("" + this.buildingStackRequired.size() + " building(s) waiting to be built.", LogGui.NORMAL);
				this.logGui.write("Stack dump : " + this.buildingStackRequired.toString(), LogGui.NORMAL);
			}
			
			// Update the city stats.
			this.cityStats.update(this.buildings);
			
			// Update the stats graphs (even if not displayed).
			this.graphStatsGui.update(this.cityStats.getPopulation(), this.cityStats.getMoney(), this.buildings.size());
			
			List<Building> buildingToDelete = new ArrayList<Building>();
			
			// Update the lights positions.
			this.lightLayer.clearLights();
			for(Building b : this.buildings) {
				Vector2f center = new Vector2f((b.getHitbox().left  + b.getHitbox().width / 2.f) * TILE_SIZE.x, (b.getHitbox().top + b.getHitbox().height / 2.f) * TILE_SIZE.y);
				this.lightLayer.addLight(center, 1.25f * b.getHitbox().width * TILE_SIZE.x, new Color(255, 255, 255, 250), new Color(255, 255, 255, 50));
				
				if(b.isHalted())
					buildingToDelete.add(b);
			}
			
			for(Building b : buildingToDelete) {
				removeBuilding(b);
			}
			
			// Update day/night.
			updateDayNight();
		}
		
		// Update visual of day/night.
		if(!this.gameSpeedGui.isInPause()) {
			try {
				updateDayNightVisual(this.simulationSpeedTimer.asSeconds(), dt.asSeconds());
			}
			catch(TextureCreationException e) {
				this.logGui.write("Error: could not create a texture in the light layer.\n", LogGui.ERROR);
			}
		}
		
		// Do the time substraction here.
		if(!this.gameSpeedGui.isInPause() && this.simulationSpeedTimer.asSeconds() >= 1.f) {
			this.simulationSpeedTimer = Time.sub(this.simulationSpeedTimer, Time.getSeconds(1.f));
		}
		
		// Update the tilemap.
		this.tilemap.update();
		
		// Update GUI.
		if(isCheckBoxChecked(this.zoneDrawingCheckboxID)) {
			// Force pause during zone drawing.
			this.gameSpeedGui.setPaused(true);
			
			this.zoneDrawingGui.update(dt, this.window, this.zoneMap, this.tileSelector);
			this.zoneMapLayer.update();
		}
		
		if(isCheckBoxChecked(this.blueprintCheckboxID)) {
			// Force pause during blueprints management.
			this.gameSpeedGui.setPaused(true);
			this.zoneMapLayer.update();
			this.textInputPool.update();
			this.blueprintGui.update();
		}

		//Update stats
		this.gameSpeedGui.update(dt);
		this.statsGui.setMoney(this.cityStats.getMoney());
		this.statsGui.setPopulation(this.cityStats.getPopulation());
		this.statsGui.setUnemployedRate(this.cityStats.getUnemploymentRate());
		this.tileSelector.update();
		this.logGui.update(dt);
	}
	
	/**
	 * Renders all the simulation.
	 */
	public void render(Time elapsedSinceLastUpdate) {
		this.window.clear(Color.BLACK);
		/////////////
		
		this.window.draw(this.tilemap);
		this.buildingRenderer.setBuildingList(this.buildings);
		this.window.draw(this.buildingRenderer);
		
		if(isCheckBoxChecked(this.zoneDrawingCheckboxID) || isCheckBoxChecked(this.blueprintCheckboxID))
			this.window.draw(this.zoneMapLayer);
		else
			this.window.draw(this.lightLayer);

		this.window.draw(this.tileSelector);

		// Static elements.
		setStaticView();

		this.window.draw(this.statsGui);
		this.window.draw(this.gameSpeedGui);
		
		if(isOnlyChecked(this.cityGraphStatsCheckboxID)) {
			this.window.draw(this.graphStatsGui);
			this.window.draw(getCheckBox(this.cityGraphStatsCheckboxID));
		}
		else if(isOnlyChecked(this.zoneDrawingCheckboxID)) {
			this.window.draw(this.zoneDrawingGui);
			this.window.draw(getCheckBox(this.zoneDrawingCheckboxID));
		}
		else if(isOnlyChecked(this.blueprintCheckboxID)) {
			this.window.draw(this.blueprintGui);
			this.window.draw(getCheckBox(this.blueprintCheckboxID));
			this.window.draw(this.textInputPool);
		}
		else {
			for(CheckBox cb : this.checkboxes) {
				this.window.draw(cb);
			}
		}
		if(this.logCheckBox.isChecked()) {
			this.window.draw(this.logGui);
		}
		this.window.draw(this.logCheckBox);
		setGameView();
		// End of static elements.
		
		if(this.displayTileInfo)
			this.window.draw(tileInfoGui);
		
		/////////////
		this.window.display();
		
	}
	
	public void saveLog() {
		this.logGui.saveToFile();
	}
	
	/**
	 * Handles the real-time input from the player.
	 * @param dt : elapsed time since last tick
	 */
	public void handleInput(Time dt) {		
		View view = (View)getWindow().getView();

		// View movement.
		float viewMovementX = 0, viewMovementY = 0;
		
		// View movement via keyboard.
		if(Keyboard.isKeyPressed(Keyboard.Key.LEFT)) {
			viewMovementX -= 400.f;
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.RIGHT)) {
			viewMovementX += 400.f;
		}
		
		if(Keyboard.isKeyPressed(Keyboard.Key.UP)) {
			viewMovementY -= 400.f;
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.DOWN)) {
			viewMovementY += 400.f;
		}
		
		// Move the view.
		Vector2f viewMovement = new Vector2f(viewMovementX, viewMovementY);
		viewMovement = Vector2f.mul(viewMovement, dt.asSeconds());
		view.move(viewMovement);
		
		getWindow().setView(view);
	}
	
	/**
	 * Returns the window.
	 * @return the window used by the simulation
	 */
	public RenderWindow getWindow() {
		return this.window;
	}
	
	public void removeBuilding(Building toDelete) {
		List<Citizen> inhabitant = toDelete.getInhabitants();
		this.buildings.remove(toDelete);
		
		for(Building b : this.buildings) {
			for(Citizen c : inhabitant) {
				b.notifyCitizenRemoved(c);
			}
		}
	}
	
	/**
	 * Remove building if we left click and pressed d
	 * @param e : Event
	 */
	public void handleBuildingRemoval(Event e) {
		if(Keyboard.isKeyPressed(Key.D)) {
			if(e.type == Event.Type.MOUSE_BUTTON_RELEASED && e.asMouseButtonEvent().button == Mouse.Button.RIGHT) {
				for(int i = 0; i < this.buildings.size();){
					if(this.buildings.get(i).getHitbox().contains(this.tileSelector.getSelectedTile())) {
						this.logGui.write("Removed building : Id : " + this.buildings.get(i).getId(), LogGui.SUCCESS);
						removeBuilding(this.buildings.get(i));
						
					}
					else {
						i++;
					}
				}
			}
		}
	}
	
	/**
	 * Handles the event.
	 * @param event : the JSFML event to handle
	 */
	public void handleEvent(Event event) {
		if(event.type == Event.Type.MOUSE_BUTTON_RELEASED && event.asMouseButtonEvent().button == Mouse.Button.MIDDLE) {
			this.displayTileInfo = !this.displayTileInfo;
		}
		
		if(isOnlyChecked(this.cityGraphStatsCheckboxID)) {
			getCheckBox(this.cityGraphStatsCheckboxID).handleEvent(event);
		}
		else if(isOnlyChecked(this.zoneDrawingCheckboxID)) {
			CheckBox checkbox = getCheckBox(this.zoneDrawingCheckboxID);
			checkbox.handleEvent(event);
			
			this.zoneDrawingGui.handleEvent(event);
			this.gameSpeedGui.setPaused(checkbox.isChecked());
		}
		else if(isOnlyChecked(this.blueprintCheckboxID)) {
			getCheckBox(blueprintCheckboxID).handleEvent(event);
			this.blueprintGui.handleEvent(this.window, event, this.zoneMap, this.logGui, this.zoneDrawingGui);
			this.textInputPool.handleEvent(event);
			
		}
		else {
			for(CheckBox cb : this.checkboxes) {
				cb.handleEvent(event);
			}
		}
		this.logCheckBox.handleEvent(event);
		handleBuildingRemoval(event);

		this.logGui.handleEvent(new Vector2f(Mouse.getPosition(this.window).x, Mouse.getPosition(this.window).y), event);
		this.gameSpeedGui.handleEvent(event);
		
	}
	
	/**
	 * Sets the game on play when the game gains focus.
	 */
	public void onGainFocus() {
		this.gameSpeedGui.setPaused(false);
	}
	
	/**
	 * Sets the game on pause when the game loose focus.
	 */
	public void onLostFocus() {
		this.gameSpeedGui.setPaused(true);
	}
}

