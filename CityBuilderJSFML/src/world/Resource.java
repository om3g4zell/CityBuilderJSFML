package world;

/*
 * Represents a resource.
 */
public class Resource {
	// Resource type.
	public static enum ResourceType {
		WATER,
		ELECTRICITY,
		FOOD,
		ROAD_PROXIMITY,
		PEOPLE,
		
		// Need to be the last Type to count how many type we have
		COUNT
	}
	
	// Constructor.
	public Resource(ResourceType type) {
		this.type = type;
		this.amount = 0.f;
	}
	
	// Constructor with specific amount.
	public Resource(ResourceType type, float amount) {
		this.type = type;
		this.amount = amount;
	}
	
	// Resource type and amount.
	public ResourceType type;
	public float amount;
}
