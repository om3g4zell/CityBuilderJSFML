package world;

import java.util.ArrayList;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2i;

import maths.Distance;
import world.Resource.ResourceType;

/*
 * Represent a building
 */
public class Building {
	/** Static. **/
	// Building Type
	public static enum BuildingType {
		HOUSE,
		GROCERY_STORE,
		ROAD,
		GENERATOR,
		HYDROLIC_STATION,
		
		NONE
	}
	
	public static enum BuildingClass {
		RESIDENTIEL,
		CULTUR,
		INDUSTRY,
		COMMERCIAL,
		
		NONE
	}
	
	public static BuildingType getBuildingTypeGenerating(ResourceType type) {
		switch(type) {
			case PEOPLE:
				return BuildingType.HOUSE;
			case ELECTRICITY:
				return BuildingType.GENERATOR;
			case WATER:
				return BuildingType.HYDROLIC_STATION;
			case FOOD:
				return BuildingType.GROCERY_STORE;
			case ROAD_PROXIMITY:
				return BuildingType.ROAD;
			default:
				return BuildingType.NONE;
		}
	}
	
	// Last building id.
	protected static int lastId = 1;
	
	/** Non-static. **/
	// Attributes
	protected int id;
	protected ArrayList<Need> needs;
	protected int range;
	protected IntRect hitbox;
	protected BuildingType type;
	protected BuildingClass buildingClass;
	protected boolean halted;
	
	// Constructor
	public Building(BuildingType type, Vector2i position) {
		this.id = lastId;
		lastId++;
		
		this.type = type;
		this.needs = new ArrayList<Need>();
		this.halted = false;
		
		switch(this.type) {
			case GENERATOR:
				this.range = 18;
				this.hitbox = new IntRect(position.x, position.y, 1, 1);
				this.buildingClass = BuildingClass.INDUSTRY;
				break;
			case GROCERY_STORE:
				this.range = 28;
				this.hitbox = new IntRect(position.x, position.y, 4, 2);
				this.needs.add(new Need(Resource.ResourceType.PEOPLE, 40, 0.5f));
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1f));
				this.buildingClass = BuildingClass.COMMERCIAL;
				break;
			case HOUSE:
				this.range = 99;
				this.hitbox = new IntRect(position.x, position.y, 2, 2);
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1f));
				this.needs.add(new Need(Resource.ResourceType.FOOD, 10, 0.9f));
				this.buildingClass = BuildingClass.RESIDENTIEL;
				break;
			case ROAD:
				this.range = 2;
				this.hitbox = new IntRect(position.x, position.y, 1, 1);
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1f));
				this.buildingClass = BuildingClass.NONE;
				break;
			case HYDROLIC_STATION:
				this.range = 18;
				this.hitbox = new IntRect(position.x, position.y, 1, 1);
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.buildingClass = BuildingClass.INDUSTRY;
				break;
			default:
				break;
		}
	}
	
	// Returns the id.
	public int getId() {
		return this.id;
	}
	
	// Returns the type.
	public BuildingType getType() {
		return this.type;
	}

	// Returns the hitbox.
	public IntRect getHitbox() {
		return this.hitbox;
	}
	
	// Generates resources.
	public void generateResources(ResourcesMap resourcesMap) {
		// Do not generate if halted.
		if(this.halted)
			return;
		
		// We use squared range and squared euclidean distance for performance.
		double squaredRange = Math.pow(range, 2);
		
		// Check all resource map in square range.
		for(int x = Math.max(0,this.hitbox.left - this.range) ; x < Math.min(resourcesMap.getSize().x,this.hitbox.left + this.range) ; ++x)
		{
			for(int y = Math.max(0,this.hitbox.top - this.range) ; y < Math.min(resourcesMap.getSize().y,this.hitbox.top + this.range) ; ++y)
			{
				// Check only in range.
				if(Distance.squaredEuclidean(new Vector2i(this.hitbox.left, this.hitbox.top), new Vector2i(x, y)) <= squaredRange)
				{
					// Generate resources depending on the building type.
					switch(this.type) {
						case GENERATOR:
							// Generate 220V of electricity
							ResourcesStack rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.ELECTRICITY, 220);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case GROCERY_STORE:
							// Generate water and food
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.FOOD, 10);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case HOUSE:
							// Generate people
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.PEOPLE, 4);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case ROAD:
							// Generate road proximity
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.ROAD_PROXIMITY, 1);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case HYDROLIC_STATION:
							// Generate water
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.add(ResourceType.WATER, 100);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						default:
							break;
					}
				}
			}
		}
	}
	
	// Consumes one 
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
	
	// Consumes resources.
	public BuildingType consumeResources(ResourcesMap resourcesMap) {
		// Get the resources available for the building.
		ResourcesStack availableResources = new ResourcesStack();
		
		for(int x = this.hitbox.left ; x < this.hitbox.left + this.hitbox.width ; ++x) {
			for(int y = this.hitbox.top ; y < this.hitbox.top + this.hitbox.height ; ++y) {
				availableResources.add(resourcesMap.getResources(new Vector2i(x, y)));
			}
		}
		
		System.out.println("For building " + this.type.toString() + "[" + this.id + "] :");
		System.out.println("\t" + ResourceType.WATER.toString() + " " + availableResources.get(ResourceType.WATER));
		System.out.println("\t" + ResourceType.ELECTRICITY.toString() + " " + availableResources.get(ResourceType.ELECTRICITY));
		System.out.println("\t" + ResourceType.PEOPLE.toString() + " " + availableResources.get(ResourceType.PEOPLE));
		System.out.println("\t" + ResourceType.ROAD_PROXIMITY.toString() + " " + availableResources.get(ResourceType.ROAD_PROXIMITY));
		System.out.println("\t" + ResourceType.FOOD.toString() + " " + availableResources.get(ResourceType.FOOD));
		
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
			this.halted = true;
			
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
