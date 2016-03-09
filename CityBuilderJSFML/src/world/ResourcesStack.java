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
	// Provoque un enorme lag.
	public void reset() {
		// Constructor of Resource puts amount to 0.
		for(Resource.ResourceType rtype : Resource.ResourceType.values())
			this.resources.put(rtype, new Resource(rtype));
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
		for(Resource.ResourceType rtype : Resource.ResourceType.values())
			this.add(rtype, a.get(rtype));
	}
	
	/**
	 * clone the resourcesStack
	 * @return ResourcesStack :clone of the resourcesStack
	 */
	public ResourcesStack cloneResourcesStack() {
		ResourcesStack rStacks = new ResourcesStack();
		
		for(Map.Entry<Resource.ResourceType, Resource> r : this.resources.entrySet()) {
			rStacks.add(r.getValue().cloneResource());
		}
		return rStacks;
	}
}
