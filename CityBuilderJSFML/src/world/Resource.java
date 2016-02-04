package world;

/**
 * Represents a resource.
 */
public class Resource {
	/** Resource type. */
	public static enum ResourceType {
		WATER,
		ELECTRICITY,
		FOOD,
		PEOPLE,
		
		// Need to be the last Type to count how many type we have
		//COUNT
	}
	
	/**
	 * Constructor.
	 * @param type : type of the resource
	 */
	public Resource(ResourceType type) {
		this.type = type;
		this.amount = 0.f;
	}
	
	/**
	 * Constructor with specific amount.
	 * @param type : type of the resource
	 * @param amount : amount of the resource
	 */
	public Resource(ResourceType type, float amount) {
		this.type = type;
		this.amount = amount;
	}
	
	/** Resource type. */
	public ResourceType type;
	
	/** Resource amount. */
	public float amount;
	
	/**
	 * Clone the resource
	 * @return Resource : a clone a Resource
	 */
	public Resource cloneResource() {
		Resource r = new Resource(this.type,this.amount);
		return r;
	}
}
