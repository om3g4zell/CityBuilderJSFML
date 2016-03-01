package sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
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
import gui.GameSpeedGui;
import gui.GraphStatsGui;
import gui.LogGui;
import gui.StatsGui;
import gui.TileInfoGui;
import gui.TileSelector;
import gui.ZoneDrawingGui;
import maths.Distance;
import world.Building;
import world.Building.BuildingType;
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
	protected TileSelector tileSelector;
	protected TileInfoGui tileInfoGui;
	protected boolean displayTileInfo;
	protected Stack<Map<Integer, Building.BuildingType>> buildingStackRequired;
	protected CheckBox zoneDrawingCheckbox;
	protected CheckBox cityGraphStatsCheckbox;
	protected ZoneMap zoneMap;
	protected ZoneMapLayer zoneMapLayer;
	protected ZoneDrawingGui zoneDrawingGui;
	protected GameSpeedGui gameSpeedGui;
	protected Time simulationSpeedTimer;
	protected View gameView;
	protected View staticView;
	protected GraphStatsGui graphStatsGui;
	protected LogGui logGui;
	
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
		
		// Clone the resources map
		this.cachedResourceMap = this.resourcesMap.cloneResourcesMap();
		
		// Create the buildings list.
		this.buildings = new ArrayList<Building>();
		
		// Create the checkboxes.
		this.zoneDrawingCheckbox = new CheckBox(10, 100, this.textureManager, this.fontManager, "Afficher les zones", 0);
		this.cityGraphStatsCheckbox = new CheckBox(10, 120, this.textureManager, this.fontManager, "Afficher les statistiques", 0);
		
		// Create the city stats.
		this.cityStats = new CityStats();
		
		// Create the city graph stats gui.
		this.graphStatsGui = new GraphStatsGui(getWindow().getSize(), this.fontManager);
		
		// Create the zoneMap
		this.zoneMap = new ZoneMap(TILEMAP_SIZE.x, TILEMAP_SIZE.y);
		
		// Create the game speed GUI
		this.gameSpeedGui = new GameSpeedGui(textureManager, fontManager, this.window.getSize().x - 80, 20);
		
		// Create the logGui
		this.logGui = new LogGui(this.fontManager);
		
		// Create the zoneMapLayer
		this.zoneMapLayer = new ZoneMapLayer(this.zoneMap);
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.FREE, new Color(12, 52, 30, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.COMMERCIAL, new Color(125, 193, 129, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.CULTURAL, new Color(51, 153, 255, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.INDUSTRY, new Color(227, 168, 87, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.ROAD, new Color(220, 220, 220, 170));
		this.zoneMapLayer.addTypeColor(Zone.ZoneClass.RESIDENTIAL, new Color(70, 0, 0, 170));
		
		// Houses.
		this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(31, 20)));
		/*this.buildings.add(new Building(BuildingType.HOUSE, new Vector2i(33, 20)));
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
		
		// Grossery store
		this.buildings.add(new Building(BuildingType.GROCERY_STORE, new Vector2i(40, 21)));
		
		// Roads
		this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(30, 20)));
		
		this.buildings.add(new Building(BuildingType.ANTENNA_4G, new Vector2i(35, 20)));*/
		
		// Inits the tilemap.
		this.tilemap = new TileMap(TILEMAP_SIZE, TILE_SIZE);
		this.tilemap.addTypeColor(TileType.TERRAIN_GRASS, new Color(0, 70, 0));
		this.tilemap.addTypeColor(TileType.BUILDING_HOUSE, new Color(70, 0, 0));
		this.tilemap.addTypeColor(TileType.BUILDING_ROAD, new Color(190, 190, 190));
		this.tilemap.addTypeColor(TileType.BUILDING_GENERATOR, new Color(227, 168, 87));
		this.tilemap.addTypeColor(TileType.BUILDING_HYDROLIC_STATION, new Color(51, 153, 255));
		this.tilemap.addTypeColor(TileType.BUILDING_SUPERMARKET, new Color(125, 193, 129));
		this.tilemap.addTypeColor(TileType.BUILDING_ROAD, new Color(220, 220, 220));
		this.tilemap.addTypeColor(TileType.BUILDING_ANTENNA_4G, new Color(63, 63, 63));
		this.tilemap.setTiles(this.tiles);
		
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
		
		// Project buildings on the tilemap at least one time.
		BuildingProjector.project(this.buildings, this.tilemap);
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
	 * Spawns the new buildings.
	 * 
	 * TODO: Separate the algorithm in sub-functions.
	 */
	public void spawnBuildings() {
		// Look into the required buildings stack.
		if(this.buildingStackRequired.empty())
			return;
		
		// The map collecting the required buildings.
		Map<Integer, Building.BuildingType> buildingsRequired = this.buildingStackRequired.peek();
		
		// First count the required buildings.
		Map<Building.BuildingType, Integer> buildingCounts = countBuildingsPerType(buildingsRequired);
		
		// Get the most required building type.
		Map.Entry<Building.BuildingType, Integer> mostRequiredBuildingTypeEntry = getMostRequiredBuildingType(buildingCounts);
		
		// If no building type has been requested, we leave.
		if(mostRequiredBuildingTypeEntry == null)
			return;

		// Compute the average position, aka the center of the search area.
		Vector2i centerOfSearchArea = getBuildingsAveragePosition(buildingsRequired, mostRequiredBuildingTypeEntry.getKey(), mostRequiredBuildingTypeEntry.getValue());
		
		// Get the furthest building from the average position, to compute the radius of the search area.
		float radius = getFurthestBuildingTo(buildingsRequired, mostRequiredBuildingTypeEntry.getKey(), centerOfSearchArea);
		
		// Create a fake building.
		Building requiredBuilding = new Building(mostRequiredBuildingTypeEntry.getKey(), new Vector2i(0, 0));
		
		// We may need to expand the radius.
		radius = Math.max(radius, requiredBuilding.getRange());
		
		// We use squared radius and squared euclidean distance for performance.
		double squaredRadius = Math.pow(radius, 2);
		
		// Map of the considered positions with the number of requiring building in range.
		HashMap<Vector2i, Integer> candidatesPositions = new HashMap<Vector2i, Integer>();
		
		// Map of the positions where it lacks resources only with the number of requiring building in range.
		HashMap<Vector2i, Integer> candidatesPositionsLackingResources = new HashMap<Vector2i, Integer>();
		
		// Missing resources for the required building.
		HashMap<Resource.ResourceType, Integer> missingResources = new HashMap<Resource.ResourceType, Integer>();
		
		// Initiates missing resources to 0.
		for(Resource.ResourceType rtype : Resource.ResourceType.values())
			missingResources.put(rtype, 0);
		
		// Check all resource map in square range.
		for(int x = Math.max(0, centerOfSearchArea.x - (int)radius) ; x < Math.min(resourcesMap.getSize().x, centerOfSearchArea.x + radius + 1) ; ++x)
		{
			for(int y = Math.max(0, centerOfSearchArea.y - (int)radius) ; y < Math.min(resourcesMap.getSize().y, centerOfSearchArea.y + radius + 1) ; ++y)
			{
				// Check only in radius.
				if(Distance.squaredEuclidean(centerOfSearchArea, new Vector2i(x, y)) <= squaredRadius)
				{
					// Check collision with other buildings.
					boolean collide = false;
					IntRect candidateHitbox = new IntRect(x, y, requiredBuilding.getHitbox().width, requiredBuilding.getHitbox().height);
					
					if(candidateHitbox.left < 0 || candidateHitbox.top < 0 || candidateHitbox.left + candidateHitbox.width >= TILEMAP_SIZE.x || candidateHitbox.top + candidateHitbox.height >= TILEMAP_SIZE.y)
						collide = true;
					
					for(Building b : this.buildings) {
						if(candidateHitbox.intersection(b.getHitbox()) != null)
							collide = true;
					}
					
					if(collide) {
						// This position is not suitable.
						continue;
					}
					
					// Check zone compatibility.
					boolean validZone = true;
					
					for(int rx = x ; rx < Math.min(x + requiredBuilding.getHitbox().width, TILEMAP_SIZE.x) ; rx++) {
						for(int ry = y ; ry < Math.min(y + requiredBuilding.getHitbox().height, TILEMAP_SIZE.y) ; ry++) {
							// Get the zone.
							Zone zone = this.zoneMap.getZoneMap().get(ry).get(rx);
							
							// check if the zone is suitable
							for(ZoneClass zoneBuilding : requiredBuilding.getZoneClasses()) {
								if(!zone.getType().equals(zoneBuilding)) {
									validZone = false;
								}
								else {
									validZone = true;
									break;
								}
							}
							
							// if the building contain the free zone it's ok
							if(requiredBuilding.getZoneClasses().contains(ZoneClass.FREE)) {
								validZone = true;
							}
							
							// if isn't a valid zone break
							if(!validZone)
								break;
							
						}
						// if isn't a valid zone break
						if(!validZone)
							break;
					}
					
					if(!validZone) {
						// This zone is not suitable
						continue;
					}

					// Get the resources available for the building.
					ResourcesStack rstack = resourcesMap.getResources(x, y);
					
					for(int rx = x ; rx < Math.min(x + requiredBuilding.getHitbox().width, TILEMAP_SIZE.x) ; rx++) {
						for(int ry = y ; ry < Math.min(y + requiredBuilding.getHitbox().height, TILEMAP_SIZE.y) ; ry++) {
							rstack.add(resourcesMap.getResources(rx, ry));
						}
					}
					
					// Check if they satisfy the needs.
					boolean allNeedsSatisfied = true;
					for(Need n : requiredBuilding.getNeeds()) {
						float minAmount = n.amount * n.fillFactor;
						
						// If one need is not satisfied to its minimum, we quit.
						if(rstack.get(n.type) < minAmount) {
							allNeedsSatisfied = false;
							int count = missingResources.get(n.type).intValue();
							count += 1;
							missingResources.put(n.type, count);
						}
					}
					
					// Check how many buildings (which required the building construction) are in range of the required building.
					int inRange = 0;
					for(Map.Entry<Integer, Building.BuildingType> buildingRequiredEntry : buildingsRequired.entrySet()) {
						if(buildingRequiredEntry.getValue() == requiredBuilding.getType()) {
							Building building = null;
							
							// Get the building.
							for(Building b : this.buildings) {
								if(b.getId() == buildingRequiredEntry.getKey()) {
									building = b;
									break;
								}
							}
							
							// Check if in range.
							if(building != null) {
								Vector2i buildingCenter = new Vector2i(building.getHitbox().left + building.getHitbox().width / 2,
																		building.getHitbox().top + building.getHitbox().height / 2);
								int distance = (int)Distance.euclidean(buildingCenter, new Vector2i(x, y));
								
								if(distance < requiredBuilding.getRange()) {
									inRange++;
								}
							}
						}
					}
					
					// Add to the candidates positions if all resources are available.
					if(allNeedsSatisfied)
						candidatesPositions.put(new Vector2i(x, y), inRange);
					else
						candidatesPositionsLackingResources.put(new Vector2i(x, y), inRange);
				}
			}
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
			
			System.out.println("Spawning : " + mostRequiredBuildingTypeEntry.getKey().toString() + " @ " + bestPosition.getKey().x + ", " + bestPosition.getKey().y);
			System.out.println("\tdistance to CoSA: " + Distance.euclidean(bestPosition.getKey(), centerOfSearchArea));
			System.out.println("\tefficiency: " + bestPosition.getValue() + "/" + mostRequiredBuildingTypeEntry.getValue());
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
				
				System.out.println("No suitable position found for : " + mostRequiredBuildingTypeEntry.getKey().toString());
				System.out.println("Most rare resource : " + rareResource.toString());
				System.out.println("Asking to spawn : " + Building.getBuildingTypeGenerating(rareResource).toString());
			}
		}
	}
	
	/**
	 * Spawn road with the zone map
	 */
	public void spawnRoad() {
		boolean collisionFlag = false;
		
		// run the map
		for(int y = 0 ; y < this.zoneMap.getSize().y ; y++) {
			
			for(int x = 0 ; x < this.zoneMap.getSize().x ; x++) {
				
				// reset the collision flag
				collisionFlag = false;
				
				// check if a zoneType is road
				if(this.zoneMap.getZoneMap().get(y).get(x).getType().equals(ZoneClass.ROAD)) {
					
					// check if not building in this zone
					for(int i = 0 ; i < this.buildings.size() ; i++) {
						
						// if building stop
						if(this.buildings.get(i).getHitbox().contains(x, y)) {
							collisionFlag = true;
							break;
						}
					}
					
					// we spawn the road
					if(!collisionFlag) {
						this.buildings.add(new Building(BuildingType.ROAD, new Vector2i(x,y)));
					}
				}
			}
		}
		this.zoneDrawingGui.setNewRoadAdded(false);
		this.logGui.write("Spawn : Road");
		this.logGui.write("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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
	 * Updates all the simulation.
	 * @param dt : frame of time to use
	 */
	public void update(Time dt) {
		// Update the simulation timer.
		if(!this.gameSpeedGui.isInPause())
				this.simulationSpeedTimer = Time.add(this.simulationSpeedTimer, Time.mul(dt, this.gameSpeedGui.getSpeedCoeff()));
		
		// Take real-time input.
		handleInput(dt);
		
		// Spawn road
		if(!this.gameSpeedGui.isInPause() && this.simulationSpeedTimer.asSeconds() >= 1.f) {
			// If there has been new road zones added.
			if(this.zoneDrawingGui.newRoadAdded()) {
				spawnRoad();
			}
			
			// Reset the resources.
			this.resourcesMap.reset();
			
			// Generate resources.
			for(Building b : this.buildings) {
				b.generateResources(this.resourcesMap);
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
				if(requiredBuilding != BuildingType.NONE) {
					buildingsRequired.put(b.getId(), requiredBuilding);
				}
			}
			
			if(this.buildingStackRequired.isEmpty()) {
				this.buildingStackRequired.push(buildingsRequired);
			}
			
			// Spawn buildings.
			spawnBuildings();
			
			// Project buildings on the tilemap.
			BuildingProjector.project(this.buildings, this.tilemap);
			
			// Update the city stats.
			this.cityStats.update(this.buildings);
			
			// Update the stats graphs (even if not displayed).
			this.graphStatsGui.update(this.cityStats.getPopulation(), this.cityStats.getMoney(), this.buildings.size());
		}
		
		// Do the time substraction here.
		if(!this.gameSpeedGui.isInPause() && this.simulationSpeedTimer.asSeconds() >= 1.f) {
			this.simulationSpeedTimer = Time.sub(this.simulationSpeedTimer, Time.getSeconds(1.f));
		}
		
		// Update the tilemap.
		this.tilemap.update();
		
		if(this.zoneDrawingCheckbox.isChecked()) {
			this.zoneDrawingGui.update(dt, this.window, this.zoneMap, this.tileSelector);
			this.zoneMapLayer.update();
		}

		//Update stats
		this.gameSpeedGui.update(dt);
		this.statsGui.setMoney(this.cityStats.getMoney());
		this.statsGui.setPopulation(this.cityStats.getPopulation());
		this.tileSelector.update();
		this.logGui.update(dt);
	}
	
	/**
	 * Renders all the simulation.
	 */
	public void render() {
		this.window.clear(Color.BLACK);
		/////////////
		
		this.window.draw(this.tilemap);
		
		if(this.zoneDrawingCheckbox.isChecked())
			this.window.draw(this.zoneMapLayer);
		
		this.window.draw(this.tileSelector);

		// Static elements.
		setStaticView();

		this.window.draw(this.statsGui);
		this.window.draw(this.gameSpeedGui);
		
		if(this.cityGraphStatsCheckbox.isChecked())
			this.window.draw(this.graphStatsGui);
		else
			this.window.draw(this.zoneDrawingCheckbox);
		
		if(this.zoneDrawingCheckbox.isChecked())
			this.window.draw(this.zoneDrawingGui);
		else
			this.window.draw(cityGraphStatsCheckbox);
		this.window.draw(logGui);
		setGameView();
		// End of static elements.
		
		if(this.displayTileInfo)
			this.window.draw(tileInfoGui);
		
		/////////////
		this.window.display();
		
	}
	
	/**
	 * Handles the real-time input from the player.
	 * @param dt : elapsed time since last tick
	 */
	public void handleInput(Time dt) {
		// View movement.
		View view = (View)getWindow().getView();
		float viewMovementX = 0, viewMovementY = 0;
		
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

	/**
	 * Handles the event.
	 * @param event : the JSFML event to handle
	 */
	public void handleEvent(Event event) {
		if(event.type == Event.Type.MOUSE_BUTTON_RELEASED && event.asMouseButtonEvent().button == Mouse.Button.MIDDLE) {
			this.displayTileInfo = !this.displayTileInfo;
		}
		
		if(!this.cityGraphStatsCheckbox.isChecked())
			this.zoneDrawingCheckbox.handleEvent(event);
		
		if(this.zoneDrawingCheckbox.isChecked())
			this.zoneDrawingGui.handleEvent(event);
		else
			this.cityGraphStatsCheckbox.handleEvent(event);
		
		this.gameSpeedGui.handleEvent(event);
	}
}
