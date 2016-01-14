package world;

import java.util.*;

/*
 * List of resources available on one tile.
 */
public class ResourceStack {
	protected Map<Resource.ResourceType, Resource> resources;
	
	public ResourceStack() {
		this.resources = new HashMap<Resource.ResourceType, Resource>();

		// Add all the resources types.
		reset();
	}
	
	// Resets all the resources to 0.
	public void reset() {
		// Constructor of Resource puts amount to 0.
		this.resources.put(Resource.ResourceType.ROAD_PROXIMITY, new Resource(Resource.ResourceType.ROAD_PROXIMITY));
		this.resources.put(Resource.ResourceType.ELECTRICITY, new Resource(Resource.ResourceType.ELECTRICITY));
		this.resources.put(Resource.ResourceType.PEOPLE, new Resource(Resource.ResourceType.PEOPLE));
		this.resources.put(Resource.ResourceType.WATER, new Resource(Resource.ResourceType.WATER));
		this.resources.put(Resource.ResourceType.FOOD, new Resource(Resource.ResourceType.FOOD));
	}
	
	// Returns the amount of the resource type.
	public float get(Resource.ResourceType type) {
		return this.resources.get(type).amount;
	}
	
	// Sets the amounts of the resource type.
	public void set(Resource.ResourceType type, float amount) {
		this.resources.put(type, new Resource(type, amount));
	}
}
