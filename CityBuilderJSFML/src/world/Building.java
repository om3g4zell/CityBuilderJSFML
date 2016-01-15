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
	// Building Type
	public static enum BuildingType {
		HOUSE,
		GROCERY_STORE,
		ROAD,
		GENERATOR,
		HYDROLIC_STATION
	}
	
	// Attributs
	protected ArrayList<Need> needs;
	protected int range;
	protected IntRect hitbox;
	protected BuildingType type;
	
	// Constructor
	public Building(BuildingType type, Vector2i position) {
		this.type = type;
		this.needs = new ArrayList<Need>();
		
		switch(this.type) {
			case GENERATOR:
				this.range = 18;
				this.hitbox = new IntRect(position.x,position.y, 1, 1);
				this.needs.add(new Need(Resource.ResourceType.PEOPLE, 10, 1f));
				break;
			case GROCERY_STORE:
				this.range = 28;
				this.hitbox = new IntRect(position.x,position.y, 8, 4);
				this.needs.add(new Need(Resource.ResourceType.PEOPLE, 40, 0.5f));
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1f));
				break;
			case HOUSE:
				this.range = 99;
				this.hitbox = new IntRect(position.x,position.y, 2, 2);
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				this.needs.add(new Need(Resource.ResourceType.WATER, 100, 0.7f));
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1f));
				this.needs.add(new Need(Resource.ResourceType.FOOD, 10, 0.9f));
				break;
			case ROAD:
				this.range = 1;
				this.hitbox = new IntRect(position.x,position.y, 1, 1);
				this.needs.add(new Need(Resource.ResourceType.ROAD_PROXIMITY, 1, 1f));
				break;
			case HYDROLIC_STATION:
				this.range = 18;
				this.hitbox = new IntRect(position.x,position.y, 1, 1);
				this.needs.add(new Need(Resource.ResourceType.ELECTRICITY, 220, 0.8f));
				break;
			default:
				break;
		}
	}
	
	// Returns the hitbox.
	public IntRect getHitbox() {
		return this.hitbox;
	}
	
	// Returns the type.
	public BuildingType getType() {
		return this.type;
	}
	
	// Generates resources.
	public void generateResources(ResourcesMap resourcesMap) {
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
							// Generate 220 v of electricity
							ResourcesStack rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.set(ResourceType.ELECTRICITY, rStack.get(ResourceType.ELECTRICITY) + 220);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case GROCERY_STORE:
							// Generate Water and food
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.set(ResourceType.FOOD, rStack.get(ResourceType.FOOD) + 10);
							rStack.set(ResourceType.WATER, rStack.get(ResourceType.WATER) + 75);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case HOUSE:
							// Generate people
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.set(ResourceType.PEOPLE, rStack.get(ResourceType.PEOPLE) + 4);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						case ROAD:
							// Generate road proximity
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.set(ResourceType.ROAD_PROXIMITY, rStack.get(ResourceType.ROAD_PROXIMITY) + 1);
							resourcesMap.setResources(new Vector2i(x ,y), rStack);
							break;
						case HYDROLIC_STATION:
							// Generate water
							rStack = resourcesMap.getResources(new Vector2i(x, y));
							rStack.set(ResourceType.WATER, rStack.get(ResourceType.WATER) + 100);
							resourcesMap.setResources(new Vector2i(x, y), rStack);
							break;
						default:
							break;
					}
				}
			}
		}
	}
}
