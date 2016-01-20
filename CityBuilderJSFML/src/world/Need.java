package world;

/**
 * Represents a need.
 */
public class Need {
	// Attributes.
	public Resource.ResourceType type;
	public int amount;
	public float fillFactor;
	
	/**
	 * Constructor
	 * @param type : type of the resource needed
	 * @param amount : amount needed
	 * @param fillFactor : fill factor to separate minimum and maximum amount
	 */
	public Need(Resource.ResourceType type, int amount, float fillFactor) {
		this.type = type;
		this.amount = amount;
		this.fillFactor = fillFactor;
	}
}
