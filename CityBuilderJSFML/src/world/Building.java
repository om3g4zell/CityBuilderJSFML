package world;

import java.util.ArrayList;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2i;

import maths.Distance;

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
		}
	}
	
	// Generates resources.
	public void generateResources(ResourcesMap resourcesMap) {
		// We use squared range and squared euclidean distance for performance.
		double squaredRange = Math.pow(range, 2);
		
		// Check all resource map in square range.
		for(int x = this.hitbox.left - this.range ; x <= this.hitbox.left + this.range ; ++x)
		{
			for(int y = this.hitbox.top - this.range ; y <= this.hitbox.top + this.range ; ++y)
			{
				// Check only in range.
				if(Distance.squaredEuclidean(new Vector2i(this.hitbox.left, this.hitbox.top), new Vector2i(x, y)) <= squaredRange)
				{
					// Generate resources depending on the building type.
					switch(this.type) {
						case GENERATOR:

							break;
						case GROCERY_STORE:

							break;
						case HOUSE:

							break;
						case ROAD:

							break;
						case HYDROLIC_STATION:

							break;
					}
				}
			}
		}
	}
}
