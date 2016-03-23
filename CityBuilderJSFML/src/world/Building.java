package world;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2i;

import maths.Distance;
import world.Resource.ResourceType;
import world.Zone.ZoneClass;

/*
 * Represent a building
 */
public class Building {
	/**
	 * building type
	 */
	public static enum BuildingType {
		HOUSE,
		
		GROCERY_STORE,
		MALL,
		PUB,
		RESTAURANT,
		
		
		
		GENERATOR,
		HYDROLIC_STATION,
		ANTENNA_4G,
		
		ROAD,
		
		POLICE_STATION,
		HOSPITAL,
		FIRE_STATION,
		
		SCHOOL,
		CINEMA,
		STADIUM,
		CASINOS,
		
		NONE
	}
	
	/**
	 * Returns the building type generating the resource type given.
	 * @param type : the resource type
	 * @return the building type
	 */
	public static BuildingType getBuildingTypeGenerating(ResourceType type) {
		switch(type) {
			case ELECTRICITY:
				return BuildingType.GENERATOR;
			case WATER:
				return BuildingType.HYDROLIC_STATION;
			case FOOD:
				return BuildingType.GROCERY_STORE;
			case ROAD_PROXIMITY:
				return BuildingType.ROAD;
			case NETWORK_4G :
				return BuildingType.ANTENNA_4G;
			case ALCOHOL:
				return BuildingType.PUB;
			case BIG_FURNITURE:
				return BuildingType.MALL;
			case EDUCATION:
				return BuildingType.SCHOOL;
			case FIRE_PROTECTION:
				return BuildingType.FIRE_STATION;
			case HOBBIES:
				int rng = (int)(Math.random()*100);
				if(rng > 50) {
					return BuildingType.CINEMA;
				}
				else {
					return BuildingType.CASINOS;
				}
			case LUXURY_FOOD:
				return BuildingType.RESTAURANT;
			case MEDICAL_CARE:
				return BuildingType.HOSPITAL;
			case SECURITY:
				return BuildingType.POLICE_STATION;
			case SPORT:
				return BuildingType.STADIUM;
			default:
				return BuildingType.NONE;
		}
	}
	
	/**
	 * Returns the suitables zones for the given building type.
	 * 
	 * @param btype : the building type
	 * @return the suitable zones for the building type
	 */
	public static List<Zone.ZoneClass> getSuitableZones(Building.BuildingType btype) {
		List<Zone.ZoneClass> suitableZones = new ArrayList<Zone.ZoneClass>();
		
		switch(btype) {
			case GENERATOR:
				suitableZones.add(Zone.ZoneClass.INDUSTRY);
				break;
			case GROCERY_STORE:
				suitableZones.add(Zone.ZoneClass.COMMERCIAL);
				break;
			case HOUSE:
				suitableZones.add(Zone.ZoneClass.RESIDENTIAL);
				break;
			case HYDROLIC_STATION:
				suitableZones.add(Zone.ZoneClass.INDUSTRY);
				break;
			case ROAD:
				suitableZones.add(Zone.ZoneClass.ROAD);
				break;
			case ANTENNA_4G:
				suitableZones.add(ZoneClass.INDUSTRY);
				break;
			case CASINOS:
				suitableZones.add(Zone.ZoneClass.CULTURAL);
				break;
			case CINEMA:
				suitableZones.add(Zone.ZoneClass.CULTURAL);
				break;
			case FIRE_STATION:
				suitableZones.add(Zone.ZoneClass.PUBLIC_SERVICE);
				break;
			case HOSPITAL:
				suitableZones.add(Zone.ZoneClass.PUBLIC_SERVICE);
				break;
			case MALL:
				suitableZones.add(Zone.ZoneClass.COMMERCIAL);
				break;
			case POLICE_STATION:
				suitableZones.add(Zone.ZoneClass.PUBLIC_SERVICE);
				break;
			case PUB:
				suitableZones.add(Zone.ZoneClass.COMMERCIAL);
				break;
			case RESTAURANT:
				suitableZones.add(Zone.ZoneClass.COMMERCIAL);
				break;
			case SCHOOL:
				suitableZones.add(Zone.ZoneClass.CULTURAL);
				break;
			case STADIUM:
				suitableZones.add(Zone.ZoneClass.CULTURAL);
				break;
			default:
				break;
			}
		
		return suitableZones;
	}
	
	/** Last building id. */
	protected static int lastId = 1;
	
	/** Non-static. **/
	// Attributes
	protected int id;
	protected int level = 1;
	protected List<Need> needs;
	protected int range;
	protected IntRect hitbox;
	protected BuildingType type;
	protected List<Zone.ZoneClass> buildingClass;
	protected boolean halted;
	protected boolean haltWarning;
	protected List<Citizen> inhabitants;
	protected List<Citizen> clients;
	protected List<Citizen> employees;
	protected int minClients;
	protected int maxClients;
	protected int minEmployees;
	protected int maxEmployees;
	
	/**
	 * Constructor
	 * @param type : the building type
	 * @param position : the building position in tile coordinates
	 */
	public Building(BuildingType type, Vector2i position) {
		this.id = lastId;
		lastId++;
		
		this.minClients = 0;
		this.maxClients = 0;
		this.minEmployees = 0;
		this.maxEmployees = 0;
		this.inhabitants = new ArrayList<Citizen>();
		this.clients = new ArrayList<Citizen>();
		this.employees = new ArrayList<Citizen>();
		this.type = type;
		this.needs = new ArrayList<Need>();
		this.buildingClass = new ArrayList<Zone.ZoneClass>();
		this.halted = false;
		this.haltWarning = false;
		
		switch(this.type) {
			case GENERATOR:
				this.range = 18;
				this.hitbox = new IntRect(position.x, position.y, 1, 1);
				this.buildingClass.add(Zone.ZoneClass.INDUSTRY);
				break;
			case GROCERY_STORE:
				this.range = 28;
				this.hitbox = new IntRect(position.x, position.y, 4, 2);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minClients = 4;
				this.maxClients = 40;
				this.minEmployees = 1;
				this.maxEmployees = 1;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case HOUSE:
				this.range = 99;
				this.hitbox = new IntRect(position.x, position.y, 2, 2);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.FOOD, 40, 0.9f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				for(int i = 0; i < 4; i++) {
					this.inhabitants.add(new Citizen(this.id));
				}
				
				this.buildingClass = Building.getSuitableZones(this.type);

				break;
			case HYDROLIC_STATION:
				this.range = 18;
				this.hitbox = new IntRect(position.x, position.y, 1, 1);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case ROAD:
				this.range = 1;
				this.hitbox = new IntRect(position.x, position.y, 1, 1);
				
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case ANTENNA_4G:
				this.range = 50;
				this.hitbox = new IntRect(position.x, position.y, 1, 1);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case CASINOS:
				this.range = 50;
				this.hitbox = new IntRect(position.x, position.y, 4, 4);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minClients = 50;
				this.maxClients = 200;
				this.minEmployees = 15;
				this.maxEmployees = 30;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case CINEMA:
				this.range = 50;
				this.hitbox = new IntRect(position.x, position.y, 4, 4);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minClients = 50;
				this.maxClients = 300;
				this.minEmployees = 3;
				this.maxEmployees = 10;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case FIRE_STATION:
				this.range = 30;
				this.hitbox = new IntRect(position.x, position.y, 3, 4);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minEmployees = 15;
				this.maxEmployees = 30;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case HOSPITAL:
				this.range = 30;
				this.hitbox = new IntRect(position.x, position.y, 3, 4);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minEmployees = 15;
				this.maxEmployees = 30;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case MALL:
				this.range = 40;
				this.hitbox = new IntRect(position.x, position.y, 5, 5);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				
				this.minClients = 100;
				this.maxClients = 300;
				this.minEmployees = 15;
				this.maxEmployees = 30;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case POLICE_STATION:
				this.range = 30;
				this.hitbox = new IntRect(position.x, position.y, 4, 4);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				
				this.minEmployees = 15;
				this.maxEmployees = 30;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case PUB:
				this.range = 25;
				this.hitbox = new IntRect(position.x, position.y, 2, 3);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minClients = 10;
				this.maxClients = 60;
				this.minEmployees = 1;
				this.maxEmployees = 4;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				
				break;
			case RESTAURANT:
				this.range = 45;
				this.hitbox = new IntRect(position.x, position.y, 3, 3);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minClients = 15;
				this.maxClients = 60;
				this.minEmployees = 3;
				this.maxEmployees = 10;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case SCHOOL:
				this.range = 50;
				this.hitbox = new IntRect(position.x, position.y, 4, 4);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minEmployees = 3;
				this.maxEmployees = 10;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			case STADIUM:
				this.range = 100;
				this.hitbox = new IntRect(position.x, position.y, 6, 6);
				
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 23760, 0.9f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1.f));
				
				this.minClients = 1000;
				this.maxClients = 60000;
				this.minEmployees = 100;
				this.maxEmployees = 500;
				
				this.buildingClass = Building.getSuitableZones(this.type);
				break;
			default:
				break;
				
		}
	}
	
	/**
	 * Level up a house and add needs
	 */
	public boolean levelUp() {
		if(this.level >+4)
			return false;
		
		this.level++;
		
		switch(this.level) {
		case 2:
			this.needs.add(new Need(Resource.ResourceType.SECURITY, 100, 1.f));
			this.needs.add(new Need(Resource.ResourceType.MEDICAL_CARE, 100, 1.f));
			this.needs.add(new Need(Resource.ResourceType.FIRE_PROTECTION, 100, 1.f));
			break;
		case 3:
			this.needs.add(new Need(Resource.ResourceType.SPORT, 25, 1.f));
			this.needs.add(new Need(ResourceType.HOBBIES, 2, 1.f));
			break;
		case 4:
			this.needs.add(new Need(Resource.ResourceType.EDUCATION, 25, 1.f));
			this.needs.add(new Need(Resource.ResourceType.NETWORK_4G, 25, 1.f));
			break;
		case 5:
			this.needs.add(new Need(Resource.ResourceType.BIG_FURNITURE, 50, 1.f));
			this.needs.add(new Need(Resource.ResourceType.ALCOHOL, 2, 1.f));
			this.needs.add(new Need(Resource.ResourceType.LUXURY_FOOD, 10, 1.f));
			break;
		}
		return true;
		
	}
	
	/**
	 * Returns the level
	 * @return the level
	 */
	public int getLevel() {
		return this.level;
	}
	/**
	 * Returns the id.
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns the range of generation.
	 * @return the range
	 */
	public int getRange() {
		return this.range;
	}
	
	/**
	 * Returns the classes this building fits into.
	 * @return array of the classes
	 */
	public List<Zone.ZoneClass> getZoneClasses() {
		return this.buildingClass;
	}
	
	/**
	 * Returns the type.
	 * @return the type
	 */
	public BuildingType getType() {
		return this.type;
	}

	/**
	 * Returns the hitbox.
	 * @return the hitbox in tile coordinates
	 */
	public IntRect getHitbox() {
		return this.hitbox;
	}
	
	/**
	 * Returns the list of need
	 * @return the list of need
	 */
	public List<Need> getNeeds() {
		return this.needs;
	}
	
	/**
	 * Returns the halted statut
	 * @return halted statut
	 */
	public boolean isHalted() {
		return this.halted;
	}
	
	/**
	 * Returns the list of inhabitants.
	 * @return the list of inhabitants
	 */
	public List<Citizen> getInhabitants() {
		return this.inhabitants;
	}
	
	/**
	 * Returns the list of clients.
	 * @return the list of clients
	 */
	public List<Citizen> getClients() {
		return this.clients;
	}
	
	/**
	 * Returns the list of employees.
	 * @return the list of employees
	 */
	public List<Citizen> getEmployees() {
		return this.employees;
	}
	
	/**
	 * Returns the number of unemployed inhabitants of this building.
	 * 
	 * @return the number of unemployed inhabitants
	 */
	public int getUnemployedInhabitantCount() {
		int inhabitantsWorking = 0;
		
		for(Citizen c : this.inhabitants)
			if(c.getWorkBuildingId() != -1)
				inhabitantsWorking++;
		
		
		return this.inhabitants.size() - inhabitantsWorking;
	}
	
	/**
	 * Generates resources.
	 * @param resourcesMap : the resources map to place resources on
	 * @param buildings : the list of buildings
	 */
	public void generateResources(ResourcesMap resourcesMap, List<Building> buildings) {
		// Do not generate if halted.
		if(this.halted)
			return;
		
		// We use squared range and squared euclidean distance for performance.
		double squaredRange = Math.pow(range, 2);
		
		// Check all resource map in square range.
		for(int x = Math.max(0, this.hitbox.left - this.range) ; x < Math.min(resourcesMap.getSize().x, this.hitbox.left + this.hitbox.width + this.range + 1) ; ++x)
		{
			for(int y = Math.max(0, this.hitbox.top - this.range) ; y < Math.min(resourcesMap.getSize().y, this.hitbox.top + this.hitbox.height + this.range + 1) ; ++y)
			{
				// Check only in range.
				if(Distance.squaredEuclidean(new Vector2i(this.hitbox.left + this.hitbox.width / 2, this.hitbox.top + this.hitbox.height / 2), new Vector2i(x, y)) <= squaredRange)
				{
					ResourcesStack rStack;

					// Generate resources depending on the building type.
					switch(this.type) {
						case GENERATOR:
							// Generate 220V of electricity
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.ELECTRICITY, 220);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case HYDROLIC_STATION:
							// Generate water
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.WATER, 100);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case ROAD:
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.ROAD_PROXIMITY, 1);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case ANTENNA_4G:
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.NETWORK_4G, 100);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case FIRE_STATION:
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.FIRE_PROTECTION, 100);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case HOSPITAL:
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.MEDICAL_CARE, 100);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case POLICE_STATION:
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.SECURITY, 100);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case SCHOOL:
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.EDUCATION, 100);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
						default:
							break;
					}
				}
			}
		}
		
		// The building generates resource for each of its client.
		for(Citizen client : this.clients) {
			// Find the house related to the citizen.
			Building house = null;
			for(Building b : buildings) {
				if(b.getId() == client.getHouseId()) {
					house = b;
					break;
				}
			}
			
			// The house has not been found.
			if(house == null)
				continue;
			
			// Depose food at their door (the top left tile).
			ResourcesStack rStack = resourcesMap.getResources(new Vector2i(house.getHitbox().left, house.getHitbox().top));
			
			switch(this.type) {
			case CASINOS:
			case CINEMA:
				rStack.add(ResourceType.HOBBIES, 10);
				break;
			case GROCERY_STORE:
				rStack.add(ResourceType.FOOD, 10);
				break;
			case MALL:
				rStack.add(ResourceType.FOOD, 50);
				break;
			case PUB:
				rStack.add(ResourceType.ALCOHOL, 10);
				break;
			case RESTAURANT:
				rStack.add(ResourceType.LUXURY_FOOD, 30);
				break;
			case STADIUM:
				rStack.add(ResourceType.SPORT, 100);
				break;
			default:
				break;
			}
			
			resourcesMap.setResources(new Vector2i(house.getHitbox().left, house.getHitbox().top), rStack);
		}
	}
	
	/**
	 * Consumes one resource type on the resources map.
	 * @param resourcesMap : the resources map to place resources on
	 * @param resourceType : the type of resource to consume
	 * @param amount : the amount of resource to consume
	 * @param fillFactor : the fill factor to apply on the amount
	 */
	public void consumeResourcesForNeed(ResourcesMap resourcesMap, ResourceType resourceType, float amount, float fillFactor) {
		float neededAmount = amount * fillFactor;
		
		// We have to distribute the consummation on all the tiles.
		for(int x = this.hitbox.left ; x < this.hitbox.left + this.hitbox.width ; ++x) {
			for(int y = this.hitbox.top ; y < this.hitbox.top + this.hitbox.height ; ++y) {
				// Check what is available on this tile.
				ResourcesStack resourcesOnThisTile = resourcesMap.getResources(new Vector2i(x, y));
				
				// Enough ?
				float availableAmountOnThisTile = resourcesOnThisTile.get(resourceType);
				
				if(availableAmountOnThisTile > neededAmount) {
					// There is more than needed.
					// Consume all we need.
					resourcesOnThisTile.add(resourceType, -neededAmount);
					neededAmount = 0.f;
				}
				else {
					// Consume all available.
					resourcesOnThisTile.set(resourceType, 0.f);
					neededAmount -= availableAmountOnThisTile;
				}
				
				// Sets the resources back on the map.
				resourcesMap.setResources(new Vector2i(x, y), resourcesOnThisTile);
			}
		}
	}
	
	/**
	 * Check the number of clients and tries to get more clients if needed.
	 * 
	 * @param houses : list of the houses
	 */
	public void checkClients(List<Building> houses) {
		// Do we need any client ?
		if(this.clients.size() >= this.maxClients)
			return;
		
		// How many clients do we need ?
		int clientsNeeded = this.maxClients - this.clients.size();
		
		for(Building house : houses) {
			// Check if the house is in the range of the building.
			double squaredDistanceToHouse = Distance.squaredEuclidean(new Vector2i(house.getHitbox().left,  house.getHitbox().top), new Vector2i(this.getHitbox().left,  this.getHitbox().top));
			if(squaredDistanceToHouse > Math.pow(this.range, 2))
				continue;
			
			// Check if it has any inhabitant without grocery store attached to it.
			for(Citizen c : house.getInhabitants()) {
				switch(this.type) {
					case CASINOS:
					case CINEMA:
						if(c.getHobbiesBuildingId() == -1) {
							// We found a new client !
							c.setHobbiesBuildingId(this.getId());
							this.clients.add(c);
							clientsNeeded--;
						}
						break;
					case GROCERY_STORE:
						if(c.getSmallFurnitureBuildingId() == -1) {
							// We found a new client !
							c.setSmallFurnitureBuildingId(this.getId());
							this.clients.add(c);
							clientsNeeded--;
						}
						break;
					case MALL:
						if(c.getBigFurnitureBuildingId() == -1) {
							// We found a new client !
							c.setBigFurnitureBuildingId(this.getId());
							this.clients.add(c);
							clientsNeeded--;
						}
						break;
					case PUB:
						if(c.getPubId() == -1) {
							// We found a new client !
							c.setPubId(this.getId());
							this.clients.add(c);
							clientsNeeded--;
						}
						break;
					case RESTAURANT:
						if(c.getRestaurantBuildingId() == -1) {
							// We found a new client !
							c.setRestaurantBuildingId(this.getId());
							this.clients.add(c);
							clientsNeeded--;
						}
						break;
					case STADIUM:
						if(c.getSportBuildingId() == -1) {
							// We found a new client !
							c.setSportBuildingId(this.getId());
							this.clients.add(c);
							clientsNeeded--;
						}
						break;
					default:
						break;
				}
				
				
				// Did we got enough new clients ?
				if(clientsNeeded <= 0)
					return;
			}
		}
	}
	
	/**
	 * Check the number of employees and tries to employ more people if needed.
	 * 
	 * @param houses : list of the houses
	 */
	public void checkEmployees(List<Building> houses) {
		// Do we need any employees ?
		if(this.employees.size() >= this.maxEmployees)
			return;
		
		// How many employees do we need ?
		int employeesNeeded = this.maxEmployees - this.employees.size();
		
		for(Building house : houses) {
			// Check if there is inhabitants unemployed in this house.
			if(house.getUnemployedInhabitantCount() == 0)
				continue;
			
			// Check if it has any inhabitant without job attached to it.
			for(Citizen c : house.getInhabitants()) {
				if(c.getWorkBuildingId() == -1) {
					// We found a new employee !
					c.setWorkBuildingId(this.getId());
					this.employees.add(c);
					employeesNeeded--;
				}
				
				// Did we got enough new employees ?
				if(employeesNeeded <= 0)
					return;
			}
		}
	}
	
	/**
	 * Check for level up a house;
	 * @param resources : the resources map
	 */
	public boolean checkLevelUp(ResourcesMap resources) {
		int random = (int)(Math.random()*1000);
		boolean maxSatisfaction = true;
		
		ResourcesStack rstack = new ResourcesStack();
		
		for(int x = this.hitbox.left ; x < this.hitbox.left + this.hitbox.width ; ++x) {
			for(int y = this.hitbox.top ; y < this.hitbox.top + this.hitbox.height ; ++y) {
				rstack.add(resources.getResources(new Vector2i(x, y)));
			}
		}
		for(Need need : this.needs) {
			if(need.amount*need.fillFactor > rstack.get(need.type)) {
				maxSatisfaction = false;
				break;
			}
		}
		if(maxSatisfaction || random == 666) {
			if(this.levelUp())
				return true;
			else 
				return false;
		}
		return false;
	}
	
	/**
	 * Consumes resources for the building.
	 * @param resourcesMap : the resources map to place resources on
	 * @return the type of the building to build
	 */
	public BuildingType consumeResources(ResourcesMap resourcesMap) {
		// Get the resources available for the building.
		ResourcesStack availableResources = new ResourcesStack();
		
		for(int x = this.hitbox.left ; x < this.hitbox.left + this.hitbox.width ; ++x) {
			for(int y = this.hitbox.top ; y < this.hitbox.top + this.hitbox.height ; ++y) {
				availableResources.add(resourcesMap.getResources(new Vector2i(x, y)));
			}
		}
		
		// Check if enough resources for minimal (minimal = need * fill factor).
		boolean enoughForMinimal = true;
		
		for(Need need : this.needs) {
			// If only one is not fullfilled, we stop.
			if(availableResources.get(need.type) < need.amount * need.fillFactor) {
				enoughForMinimal = false;
				break;
			}
		}
		
		// If yes :
		if(enoughForMinimal) {
			// If there is enough resources for minimal, we resume the building production.
			this.halted = false;
			
			// Check if enough resources for 100%.
			boolean enoughForFull = true;
			
			for(Need need : this.needs) {
				// If only one is not fullfilled, we stop.
				if(availableResources.get(need.type) < need.amount) {
					enoughForFull = false;
					break;
				}
			}
			
			// If yes, perfect.
			if(enoughForFull) {
				// Consume all needed resources at 100% (-> fillFactor = 1).
				for(Need need : this.needs) {
					this.consumeResourcesForNeed(resourcesMap, need.type, need.amount, 1.f);
				}
			}
			// If no :
			else {
				// Consume all needed resources.
				for(Need need : this.needs) {
					this.consumeResourcesForNeed(resourcesMap, need.type, need.amount, need.fillFactor);
				}

				// Require new building(s) to satisfy needs at 100%.
				for(Need need : this.needs) {
					// If only one is not fullfilled, we require it.
					if(availableResources.get(need.type) < need.amount) {
						return getBuildingTypeGenerating(need.type);
					}
				}
			}
		}
		// If no :
		else {
			// Halt the building and don't consume anything.
			if(this.haltWarning)
				this.halted = false;
			else
				this.haltWarning = true;
			
			// Require new building(s) to satisfy needs.
			for(Need need : this.needs) {
				// If only one is not fullfilled, we require it.
				if(availableResources.get(need.type) < need.amount * need.fillFactor) {
					return getBuildingTypeGenerating(need.type);
				}
			}
		}
		
		return BuildingType.NONE;
	}
}
