package world;

import java.util.*;

/*
 * List of resources available on one tile.
 */
public class ResourcesStack {
	// Resources.
	protected Map<Resource.ResourceType, Resource> resources;
	
	public ResourcesStack() {
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
	
	// Adds given amount to a certain resource type to the current stack.
	public void add(Resource.ResourceType type, float amount) {
		this.set(type, this.get(type) + amount);
	}
	
	// Adds given resources to the current stack.
	public void add(Resource resource) {
		this.add(resource.type, resource.amount);
	}
	
	// Adds two resources stack to the current stack.
	public void add(ResourcesStack a) {
		this.add(Resource.ResourceType.ROAD_PROXIMITY, a.get(Resource.ResourceType.ROAD_PROXIMITY));
		this.add(Resource.ResourceType.ELECTRICITY, a.get(Resource.ResourceType.ELECTRICITY));
		this.add(Resource.ResourceType.PEOPLE, a.get(Resource.ResourceType.PEOPLE));
		this.add(Resource.ResourceType.WATER, a.get(Resource.ResourceType.WATER));
		this.add(Resource.ResourceType.FOOD, a.get(Resource.ResourceType.FOOD));
	}
}
